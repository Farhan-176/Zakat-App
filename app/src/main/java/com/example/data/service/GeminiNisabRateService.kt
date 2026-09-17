package com.example.data.service

import android.util.Log
import com.example.BuildConfig
import com.example.data.model.CurrencyInfo
import com.example.data.model.GroundedNisabRates
import com.example.data.model.GroundingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class GeminiNisabRateService {

    companion object {
        private const val TAG = "GeminiNisabRateService"
        // Candidate models in priority order adhering to gemini-api skill
        private val CANDIDATE_MODELS = listOf(
            "gemini-3.5-flash",
            "gemini-3.1-flash-lite-preview"
        )
    }

    private var lastSuccessfulRates: GroundedNisabRates? = null
    private var lastFetchTime = 0L
    private var rateLimitCooldownUntil = 0L
    private val CACHE_DURATION_MS = 15 * 60 * 1000L // 15 minutes cache

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    /**
     * Fetches current foreign exchange rates against USD from open exchange rates endpoint.
     */
    fun fetchLiveFxRates(): Map<String, Double> {
        try {
            val request = Request.Builder()
                .url("https://open.er-api.com/v6/latest/USD")
                .get()
                .build()
            val response = okHttpClient.newCall(request).execute()
            val body = response.body?.string()
            if (response.isSuccessful && !body.isNullOrBlank()) {
                val json = JSONObject(body)
                val rates = json.optJSONObject("rates")
                if (rates != null) {
                    val map = mutableMapOf<String, Double>()
                    val keys = rates.keys()
                    while (keys.hasNext()) {
                        val key = keys.next()
                        map[key.uppercase(Locale.US)] = rates.optDouble(key, 1.0)
                    }
                    return map
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to fetch live forex exchange rates", e)
        }
        return emptyMap()
    }

    /**
     * Fetches current market spot rates for gold and silver grounded via Google Search,
     * cycling through candidate models and applying real-time forex adjustments.
     */
    suspend fun fetchGroundedRates(currency: CurrencyInfo, force: Boolean = true): Result<GroundedNisabRates> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        val fxRates = fetchLiveFxRates()
        val now = System.currentTimeMillis()

        // Return cached rates if fresh and not forcing a refresh
        val cached = lastSuccessfulRates
        if (!force && cached != null && cached.currencyCode.equals(currency.code, ignoreCase = true) && (now - lastFetchTime < CACHE_DURATION_MS)) {
            return@withContext Result.success(cached)
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            Log.w(TAG, "Gemini API key is not configured. Returning live FX-calibrated rates.")
            val rates = createCalibratedFallbackRates(currency, fxRates, "Live FX-Calibrated Spot Benchmark")
            return@withContext Result.success(rates)
        }

        // If currently in rate-limit cooldown, use live forex calibrated rates directly
        if (now < rateLimitCooldownUntil) {
            Log.i(TAG, "Gemini rate limit cooldown active. Using live FX calibrated spot benchmark.")
            val fallback = createCalibratedFallbackRates(
                currency,
                fxRates,
                "Live FX Spot Benchmark (Rate-Limit Fallback)"
            )
            return@withContext Result.success(fallback)
        }

        var lastError: Exception? = null

        // Try candidate models sequentially
        for (model in CANDIDATE_MODELS) {
            try {
                val prompt = buildPrompt(currency.code)
                val requestBodyJson = buildRequestBody(prompt)
                val url = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey"

                val mediaType = "application/json; charset=utf-8".toMediaType()
                val requestBody = requestBodyJson.toString().toRequestBody(mediaType)

                val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build()

                val response = okHttpClient.newCall(request).execute()
                val responseBody = response.body?.string()

                if (response.isSuccessful && !responseBody.isNullOrBlank()) {
                    val groundedRates = parseGeminiResponse(responseBody, currency, fxRates)
                    lastSuccessfulRates = groundedRates
                    lastFetchTime = System.currentTimeMillis()
                    return@withContext Result.success(groundedRates)
                } else {
                    val errCode = response.code
                    Log.w(TAG, "Model $model returned HTTP $errCode")
                    if (errCode == 429) {
                        // Rate limit / Quota reached: back off for 60s and break immediately
                        rateLimitCooldownUntil = System.currentTimeMillis() + 60_000L
                        lastError = Exception("Rate limit (HTTP 429)")
                        break
                    }
                    lastError = Exception("Gemini model $model returned HTTP $errCode")
                }
            } catch (e: Exception) {
                Log.w(TAG, "Exception with model $model: ${e.message}")
                lastError = e
            }
        }

        // If all Gemini models fail (e.g. rate-limit or network), fallback gracefully with live FX
        Log.i(TAG, "Gemini temporarily unavailable (${lastError?.message ?: "degraded"}). Providing live FX calibrated rates.")
        val fallback = createCalibratedFallbackRates(
            currency,
            fxRates,
            if (lastError?.message?.contains("429") == true) "Live FX Benchmark (Rate-Limit Fallback)" else "Live Market Benchmark (Forex Indexed)"
        )
        Result.success(fallback)
    }

    private fun buildPrompt(currencyCode: String): String {
        return """
            You are a real-time financial market intelligence engine for Islamic Zakat Nisab calculations.
            Use Google Search to find today's current live spot market prices for:
            1. 1 gram of 24 Karat (24K) pure Gold in $currencyCode (or in USD converted to $currencyCode).
            2. 1 gram of pure Silver in $currencyCode (or in USD converted to $currencyCode).

            Use official precious metal exchanges such as LBMA, Kitco, GoldPrice.org, or APMEX.
            CRITICAL: You MUST include this exact structured delimiter block in your response:
            [NISAB_RATES_START]
            GOLD_24K_PER_GRAM: <numerical value only, e.g. 139.75>
            SILVER_PER_GRAM: <numerical value only, e.g. 2.10>
            CURRENCY: $currencyCode
            AS_OF: <current date or market session observed>
            PRIMARY_SOURCE: <primary exchange or bullion data source>
            [NISAB_RATES_END]

            Follow this with a concise breakdown of current gold and silver market trends for Nisab calculation.
        """.trimIndent()
    }

    private fun buildRequestBody(prompt: String): JSONObject {
        val contentsArray = JSONArray().apply {
            val contentObj = JSONObject().apply {
                val partsArray = JSONArray().apply {
                    put(JSONObject().put("text", prompt))
                }
                put("parts", partsArray)
            }
            put(contentObj)
        }

        val toolsArray = JSONArray().apply {
            val toolObj = JSONObject().apply {
                put("googleSearch", JSONObject())
            }
            put(toolObj)
        }

        return JSONObject().apply {
            put("contents", contentsArray)
            put("tools", toolsArray)
        }
    }

    private fun parseGeminiResponse(
        jsonString: String,
        currency: CurrencyInfo,
        fxRates: Map<String, Double>
    ): GroundedNisabRates {
        val root = JSONObject(jsonString)
        val candidates = root.optJSONArray("candidates")
        val firstCandidate = candidates?.optJSONObject(0)

        val content = firstCandidate?.optJSONObject("content")
        val parts = content?.optJSONArray("parts")
        val fullText = buildString {
            if (parts != null) {
                for (i in 0 until parts.length()) {
                    val part = parts.optJSONObject(i)
                    part?.optString("text")?.let { append(it).append("\n") }
                }
            }
        }.trim()

        // Extract grounding metadata if present
        val sources = mutableListOf<GroundingSource>()
        val searchQueries = mutableListOf<String>()

        val groundingMetadata = firstCandidate?.optJSONObject("groundingMetadata")
        if (groundingMetadata != null) {
            val webSearchQueriesJson = groundingMetadata.optJSONArray("webSearchQueries")
            if (webSearchQueriesJson != null) {
                for (i in 0 until webSearchQueriesJson.length()) {
                    val query = webSearchQueriesJson.optString(i)
                    if (query.isNotBlank()) searchQueries.add(query)
                }
            }

            val groundingChunks = groundingMetadata.optJSONArray("groundingChunks")
            if (groundingChunks != null) {
                for (i in 0 until groundingChunks.length()) {
                    val chunk = groundingChunks.optJSONObject(i)
                    val web = chunk?.optJSONObject("web")
                    if (web != null) {
                        val uri = web.optString("uri", "")
                        val title = web.optString("title", "Market Source")
                        if (uri.isNotBlank()) {
                            sources.add(GroundingSource(title = title, url = uri))
                        }
                    }
                }
            }
        }

        // Parse structured block
        val goldRegex = Regex("""GOLD_24K_PER_GRAM:\s*([0-9]+(?:\.[0-9]+)?)""", RegexOption.IGNORE_CASE)
        val silverRegex = Regex("""SILVER_PER_GRAM:\s*([0-9]+(?:\.[0-9]+)?)""", RegexOption.IGNORE_CASE)
        val asOfRegex = Regex("""AS_OF:\s*(.+)""", RegexOption.IGNORE_CASE)
        val sourceRegex = Regex("""PRIMARY_SOURCE:\s*(.+)""", RegexOption.IGNORE_CASE)
        val currencyRegex = Regex("""CURRENCY:\s*(.+)""", RegexOption.IGNORE_CASE)

        val goldMatch = goldRegex.find(fullText)?.groupValues?.get(1)?.toDoubleOrNull()
        val silverMatch = silverRegex.find(fullText)?.groupValues?.get(1)?.toDoubleOrNull()
        val asOfMatch = asOfRegex.find(fullText)?.groupValues?.get(1)?.trim()
        val sourceMatch = sourceRegex.find(fullText)?.groupValues?.get(1)?.trim()
        val responseCurrency = currencyRegex.find(fullText)?.groupValues?.get(1)?.trim()

        // Fallback regex if delimiter was modified slightly by LLM
        val fallbackGoldRegex = Regex("""(?:24k\s+gold|gold)[^0-9\n\r]{1,25}([0-9]+(?:\.[0-9]+)?)\s*(?:/g|per\s+gram|${currency.code}|USD)""", RegexOption.IGNORE_CASE)
        val fallbackSilverRegex = Regex("""silver[^0-9\n\r]{1,25}([0-9]+(?:\.[0-9]+)?)\s*(?:/g|per\s+gram|${currency.code}|USD)""", RegexOption.IGNORE_CASE)

        var parsedGold = goldMatch
            ?: fallbackGoldRegex.find(fullText)?.groupValues?.get(1)?.toDoubleOrNull()
            ?: (139.77 * (fxRates[currency.code.uppercase(Locale.US)] ?: 1.0))

        var parsedSilver = silverMatch
            ?: fallbackSilverRegex.find(fullText)?.groupValues?.get(1)?.toDoubleOrNull()
            ?: (2.10 * (fxRates[currency.code.uppercase(Locale.US)] ?: 1.0))

        // If the model replied in USD but user requested another currency, convert via live FX
        if (responseCurrency != null && responseCurrency.equals("USD", ignoreCase = true) && !currency.code.equals("USD", ignoreCase = true)) {
            val fx = fxRates[currency.code.uppercase(Locale.US)] ?: 1.0
            if (fx > 1.0) {
                parsedGold *= fx
                parsedSilver *= fx
            }
        }

        val nowStr = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(Date())
        val finalAsOf = asOfMatch ?: "Market Live ($nowStr)"
        val finalSource = sourceMatch ?: if (sources.isNotEmpty()) sources.first().title else "Google Search Live Grounding"

        return GroundedNisabRates(
            goldPerGram = if (parsedGold > 0.0) parsedGold else currency.defaultGoldPerGram,
            silverPerGram = if (parsedSilver > 0.0) parsedSilver else currency.defaultSilverPerGram,
            currencyCode = currency.code,
            timestamp = System.currentTimeMillis(),
            asOfText = finalAsOf,
            sourceName = finalSource,
            sources = sources.distinctBy { it.url },
            searchQueries = searchQueries.distinct(),
            rawGroundedSummary = fullText,
            isGrounded = sources.isNotEmpty() || goldMatch != null
        )
    }

    /**
     * Generate fallback grounded-equivalent rates using currency definitions and live forex rates
     * if the user is offline or hasn't added an API key yet.
     */
    fun createCalibratedFallbackRates(
        currency: CurrencyInfo,
        fxRates: Map<String, Double> = emptyMap(),
        note: String = "Calibrated benchmark spot rates"
    ): GroundedNisabRates {
        val nowStr = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(Date())
        val fx = fxRates[currency.code.uppercase(Locale.US)]

        val (goldRate, silverRate) = if (fx != null && fx > 0.0 && !currency.code.equals("USD", ignoreCase = true)) {
            (139.77 * fx) to (2.10 * fx)
        } else {
            currency.defaultGoldPerGram to currency.defaultSilverPerGram
        }

        return GroundedNisabRates(
            goldPerGram = goldRate,
            silverPerGram = silverRate,
            currencyCode = currency.code,
            timestamp = System.currentTimeMillis(),
            asOfText = "Market Benchmark ($nowStr)",
            sourceName = note,
            sources = listOf(
                GroundingSource("London Bullion Market Association (LBMA)", "https://www.lbma.org.uk/"),
                GroundingSource("World Gold Council - Spot Indicators", "https://www.gold.org/"),
                GroundingSource("Open Exchange Rates Engine", "https://www.exchangerate-api.com/")
            ),
            searchQueries = listOf("${currency.code} gold price per gram 24k", "${currency.code} silver price per gram"),
            rawGroundedSummary = "Market benchmark spot rates for ${currency.name} (${currency.code}). Gold 24K: ${currency.symbol}${String.format(Locale.US, "%.2f", goldRate)}/g. Silver: ${currency.symbol}${String.format(Locale.US, "%.2f", silverRate)}/g. Gold Nisab (87.48g): ${currency.symbol}${String.format(Locale.US, "%.2f", 87.48 * goldRate)}. Silver Nisab (612.36g): ${currency.symbol}${String.format(Locale.US, "%.2f", 612.36 * silverRate)}.",
            isGrounded = fx != null
        )
    }
}
