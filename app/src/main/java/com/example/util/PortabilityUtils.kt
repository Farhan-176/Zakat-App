package com.example.util

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import com.example.data.model.ZakatRecord
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PortabilityUtils {

    fun exportRecordsToJson(records: List<ZakatRecord>): String {
        val jsonArray = JSONArray()
        records.forEach { r ->
            val obj = JSONObject().apply {
                put("id", r.id)
                put("refNumber", r.refNumber)
                put("timestamp", r.timestamp)
                put("hijriYear", r.hijriYear)
                put("label", r.label)
                put("currencyCode", r.currencyCode)
                put("currencySymbol", r.currencySymbol)
                put("profileName", r.profileName)
                put("madhab", r.madhab)
                put("netWealth", r.netWealth)
                put("zakatDue", r.zakatDue)
                put("nisabStandard", r.nisabStandard)
                put("goldGrams", r.goldGrams)
                put("goldRate", r.goldRate)
                put("goldKarat", r.goldKarat)
                put("silverGrams", r.silverGrams)
                put("silverRate", r.silverRate)
                put("grossAssets", r.grossAssets)
                put("deductions", r.deductions)
                put("hashDigest", r.hashDigest)
            }
            jsonArray.put(obj)
        }
        return jsonArray.toString(2)
    }

    fun importRecordsFromJson(jsonString: String): List<ZakatRecord> {
        val list = mutableListOf<ZakatRecord>()
        try {
            val arr = JSONArray(jsonString)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                val rec = ZakatRecord(
                    refNumber = obj.optString("refNumber", "ZC-RESTORED-${(1000..9999).random()}"),
                    timestamp = obj.optLong("timestamp", System.currentTimeMillis()),
                    hijriYear = obj.optString("hijriYear", "1448 AH Hawl"),
                    label = obj.optString("label", "Restored Record"),
                    cashOnHand = obj.optDouble("cashOnHand", 0.0),
                    bankCurrent = obj.optDouble("bankCurrent", 0.0),
                    bankSavings = obj.optDouble("bankSavings", 0.0),
                    goldGrams = obj.optDouble("goldGrams", 0.0),
                    goldRate = obj.optDouble("goldRate", 0.0),
                    silverGrams = obj.optDouble("silverGrams", 0.0),
                    silverRate = obj.optDouble("silverRate", 0.0),
                    stocksEquities = obj.optDouble("stocksEquities", 0.0),
                    mutualFunds = obj.optDouble("mutualFunds", 0.0),
                    businessCash = obj.optDouble("businessCash", 0.0),
                    businessInventory = obj.optDouble("businessInventory", 0.0),
                    moneyOwed = obj.optDouble("moneyOwed", 0.0),
                    receivablesRecoverable = obj.optBoolean("receivablesRecoverable", true),
                    debtorName = obj.optString("debtorName", ""),
                    deductibleDebts = obj.optDouble("deductibleDebts", 0.0),
                    debtDescription = obj.optString("debtDescription", ""),
                    nisabStandard = obj.optString("nisabStandard", "Silver"),
                    grossAssets = obj.optDouble("grossAssets", 0.0),
                    deductions = obj.optDouble("deductions", 0.0),
                    netWealth = obj.optDouble("netWealth", 0.0),
                    zakatDue = obj.optDouble("zakatDue", 0.0),
                    hashDigest = obj.optString("hashDigest", ""),
                    currencyCode = obj.optString("currencyCode", "PKR"),
                    currencySymbol = obj.optString("currencySymbol", "₨"),
                    profileName = obj.optString("profileName", "Personal"),
                    madhab = obj.optString("madhab", "Hanafi"),
                    goldKarat = obj.optString("goldKarat", "24K"),
                    weightUnit = obj.optString("weightUnit", "Grams"),
                    jewelryExemptApplied = obj.optBoolean("jewelryExemptApplied", false),
                    customAssetsTotal = obj.optDouble("customAssetsTotal", 0.0),
                    customDebtsTotal = obj.optDouble("customDebtsTotal", 0.0),
                    hawlDateMillis = obj.optLong("hawlDateMillis", 0L)
                )
                list.add(rec)
            }
        } catch (_: Exception) {}
        return list
    }

    fun exportRecordsToCsv(records: List<ZakatRecord>): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
        val sb = StringBuilder()
        sb.append("Reference Number,Date,Profile,Madhab,Label,Currency,Net Wealth,Zakat Due (2.5%),Nisab Standard,Hash Digest\n")
        records.forEach { r ->
            val dateStr = sdf.format(Date(r.timestamp))
            sb.append("\"${r.refNumber}\",\"$dateStr\",\"${r.profileName}\",\"${r.madhab}\",\"${r.label.replace("\"", "\"\"")}\",\"${r.currencyCode}\",${r.netWealth},${r.zakatDue},\"${r.nisabStandard}\",\"${r.hashDigest}\"\n")
        }
        return sb.toString()
    }

    fun shareText(context: Context, title: String, content: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, content)
        }
        val chooser = Intent.createChooser(intent, title)
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }

    fun addHawlMilestoneToCalendar(context: Context, hawlDateMillis: Long, zakatDueFormatted: String) {
        try {
            val intent = Intent(Intent.ACTION_INSERT).apply {
                data = CalendarContract.Events.CONTENT_URI
                putExtra(CalendarContract.Events.TITLE, "Zakat Hawl Milestone (Zakat Companion)")
                putExtra(
                    CalendarContract.Events.DESCRIPTION,
                    "Annual Hawl cycle completion. Your recorded Zakat obligation is ready for discharge: $zakatDueFormatted. Calculated with 100% sovereign offline privacy."
                )
                putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, hawlDateMillis)
                putExtra(CalendarContract.EXTRA_EVENT_END_TIME, hawlDateMillis + 3600000)
                putExtra(CalendarContract.Events.ALL_DAY, true)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (_: Exception) {}
    }
}
