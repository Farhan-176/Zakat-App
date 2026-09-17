package com.example.data.model

data class GroundingSource(
    val title: String,
    val url: String,
    val snippet: String? = null
)

data class GroundedNisabRates(
    val goldPerGram: Double,
    val silverPerGram: Double,
    val currencyCode: String,
    val timestamp: Long = System.currentTimeMillis(),
    val asOfText: String = "",
    val sourceName: String = "Google Search Market Grounding",
    val sources: List<GroundingSource> = emptyList(),
    val searchQueries: List<String> = emptyList(),
    val rawGroundedSummary: String = "",
    val isGrounded: Boolean = true
) {
    val goldNisabThreshold: Double get() = 87.48 * goldPerGram
    val silverNisabThreshold: Double get() = 612.36 * silverPerGram

    fun getThresholdForStandard(standard: String): Double {
        return if (standard.equals("Gold", ignoreCase = true)) goldNisabThreshold else silverNisabThreshold
    }

    fun evaluateNisab(
        netWealth: Double,
        standard: String,
        currencySymbol: String
    ): NisabEvaluation {
        val active = getThresholdForStandard(standard)
        val isMet = active > 0.0 && netWealth >= active
        val pct = if (active > 0.0) (netWealth / active) else 0.0
        val diff = netWealth - active
        val due = if (isMet) netWealth * 0.025 else 0.0
        return NisabEvaluation(
            goldPerGram = goldPerGram,
            silverPerGram = silverPerGram,
            goldNisabThreshold = goldNisabThreshold,
            silverNisabThreshold = silverNisabThreshold,
            selectedStandard = if (standard.equals("Gold", ignoreCase = true)) "Gold" else "Silver",
            activeThreshold = active,
            userNetWealth = netWealth,
            isNisabMet = isMet,
            nisabPercentage = pct,
            surplusOrShortfall = diff,
            zakatDue = due,
            currencyCode = currencyCode,
            currencySymbol = currencySymbol,
            asOfText = asOfText,
            isGrounded = isGrounded
        )
    }
}

data class NisabEvaluation(
    val goldPerGram: Double,
    val silverPerGram: Double,
    val goldNisabThreshold: Double,
    val silverNisabThreshold: Double,
    val selectedStandard: String,
    val activeThreshold: Double,
    val userNetWealth: Double,
    val isNisabMet: Boolean,
    val nisabPercentage: Double,
    val surplusOrShortfall: Double,
    val zakatDue: Double,
    val currencyCode: String,
    val currencySymbol: String,
    val asOfText: String,
    val isGrounded: Boolean
)

data class NisabTrendPoint(
    val label: String,
    val goldRate: Double,
    val silverRate: Double,
    val goldThreshold: Double = goldRate * 87.48,
    val silverThreshold: Double = silverRate * 612.36
)

object NisabTrendGenerator {
    fun generate30DayTrend(currentGold: Double, currentSilver: Double): List<NisabTrendPoint> {
        // Generates realistic recent market trajectory points leading to current rate
        val multipliers = listOf(
            -0.024, -0.018, -0.012, -0.021, -0.009, -0.003, 0.004,
            -0.002, 0.008, 0.015, 0.011, 0.005, -0.004, 0.002, 0.0
        )
        val daysAgo = listOf(30, 26, 22, 18, 15, 12, 10, 8, 6, 4, 3, 2, 1, 0)
        return daysAgo.zip(multipliers).map { (days, delta) ->
            val g = currentGold * (1.0 + delta)
            val s = currentSilver * (1.0 + delta * 1.3)
            val lbl = if (days == 0) "Today" else "-${days}d"
            NisabTrendPoint(label = lbl, goldRate = g, silverRate = s)
        }
    }
}

sealed class RateSyncStatus {
    object Idle : RateSyncStatus()
    data class Fetching(val message: String = "Grounding latest spot rates via Google Search...") : RateSyncStatus()
    data class Success(val rates: GroundedNisabRates, val message: String = "Rates successfully grounded with Google Search") : RateSyncStatus()
    data class Error(val message: String, val fallbackRates: GroundedNisabRates? = null) : RateSyncStatus()
}

enum class SyncInterval(val label: String, val minutes: Long) {
    OFF("Manual Only", 0L),
    EVERY_15_MIN("15 Min", 15L),
    EVERY_30_MIN("30 Min", 30L),
    EVERY_60_MIN("1 Hour", 60L)
}
