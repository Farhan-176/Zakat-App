package com.example.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class HijriMilestone(
    val id: String,
    val title: String,
    val hijriDateText: String,
    val significance: String,
    val approximateDayOfYear: Int // Approximate day offset in a standard lunar year
) {
    RAMADAN_FIRST(
        id = "ramadan_1",
        title = "1st of Ramadan",
        hijriDateText = "1 Ramadan",
        significance = "Commencement of the holy month; premier time for multiplied rewards.",
        approximateDayOfYear = 236
    ),
    RAMADAN_TWENTY_SEVENTH(
        id = "ramadan_27",
        title = "27th of Ramadan",
        hijriDateText = "27 Ramadan",
        significance = "Laylatul Qadr (Night of Decree); auspicious day for discharging charity.",
        approximateDayOfYear = 263
    ),
    SHAWWAL_FIRST(
        id = "shawwal_1",
        title = "1st of Shawwal",
        hijriDateText = "1 Shawwal (Eid-ul-Fitr)",
        significance = "Annual festival milestone; completion of Ramadan fasting cycle.",
        approximateDayOfYear = 266
    ),
    MUHARRAM_TENTH(
        id = "muharram_10",
        title = "10th of Muharram",
        hijriDateText = "10 Muharram (Day of Ashura)",
        significance = "Traditional prophetic milestone for historical community zakat accounting.",
        approximateDayOfYear = 10
    ),
    RAJAB_TWENTY_SEVENTH(
        id = "rajab_27",
        title = "27th of Rajab",
        hijriDateText = "27 Rajab (Isra & Mi'raj)",
        significance = "Honored sacred month marker; spiritual preparation for Sha'ban and Ramadan.",
        approximateDayOfYear = 203
    ),
    CUSTOM(
        id = "custom",
        title = "Custom Hawl Anniversary",
        hijriDateText = "Personal Date",
        significance = "Exact date your net wealth first reached or exceeded the Nisab threshold.",
        approximateDayOfYear = 0
    );

    companion object {
        fun fromId(id: String): HijriMilestone {
            return entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: RAMADAN_FIRST
        }
    }
}

enum class HawlContinuityRuling(
    val title: String,
    val madhabName: String,
    val summary: String,
    val detailedRuling: String
) {
    HANAFI_START_END(
        title = "Start & End Benchmark",
        madhabName = "Hanafi School",
        summary = "Nisab must be met at the start and the end of the 354-day cycle.",
        detailedRuling = "According to Imam Abu Hanifah and the Hanafi consensus, if wealth reaches Nisab at the beginning of the year and remains at or above Nisab at the conclusion of the Hawl, Zakat is due on the closing wealth. Intermediate fluctuations during the year do not interrupt the Hawl unless total wealth drops to absolute zero."
    ),
    SHAFI_CONTINUOUS(
        title = "Continuous Year-Long Nisab",
        madhabName = "Shafi'i & Hanbali Schools",
        summary = "Nisab must be maintained continuously throughout the entire lunar year.",
        detailedRuling = "According to the Shafi'i and Hanbali jurists, if net wealth drops below the Nisab threshold at any point during the 354 lunar days, the Hawl is immediately interrupted. A new Hawl cycle begins only on the date wealth reaches Nisab once again."
    )
}

data class HawlCycleState(
    val startDateMillis: Long,
    val selectedMilestone: HijriMilestone = HijriMilestone.RAMADAN_FIRST,
    val selectedRuling: HawlContinuityRuling = HawlContinuityRuling.HANAFI_START_END,
    val didWealthDropBelowNisab: Boolean = false,
    val notes: String = ""
) {
    val totalLunarDays: Long = 354L

    val daysElapsed: Long
        get() {
            val now = System.currentTimeMillis()
            val diff = (now - startDateMillis).coerceAtLeast(0L)
            return (diff / 86_400_000L)
        }

    val currentCycleDay: Long
        get() = (daysElapsed % totalLunarDays) + 1L

    val daysRemainingInCycle: Long
        get() = (totalLunarDays - (daysElapsed % totalLunarDays)).coerceIn(0L, totalLunarDays)

    val progressFraction: Float
        get() = (currentCycleDay.toFloat() / totalLunarDays.toFloat()).coerceIn(0f, 1f)

    val isCycleDueNow: Boolean
        get() = daysRemainingInCycle <= 7L

    val formattedDueDate: String
        get() {
            val dueMillis = startDateMillis + ((((daysElapsed / totalLunarDays) + 1) * totalLunarDays) * 86_400_000L)
            val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.US)
            return sdf.format(Date(dueMillis))
        }

    val formattedStartDate: String
        get() {
            val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.US)
            return sdf.format(Date(startDateMillis))
        }
}
