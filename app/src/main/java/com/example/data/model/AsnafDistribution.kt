package com.example.data.model

import java.util.Locale

enum class AsnafCategory(
    val id: String,
    val arabicName: String,
    val englishName: String,
    val shortDescription: String,
    val detailedFiqhRules: String,
    val recommendedSharePct: Double = 0.0
) {
    FUQARA(
        id = "fuqara",
        arabicName = "الفقراء",
        englishName = "Al-Fuqara (The Poor)",
        shortDescription = "Individuals with no income or property, or less than half of their basic necessities.",
        detailedFiqhRules = "Classical definition: One who possesses neither property nor lawful earning sufficient to meet the basic needs of life (food, clothing, shelter) for themselves and their dependents.",
        recommendedSharePct = 0.25
    ),
    MASAKIN(
        id = "masakin",
        arabicName = "المساكين",
        englishName = "Al-Masakin (The Destitute / Needy)",
        shortDescription = "Individuals who earn or possess something, but it falls short of basic sustenance.",
        detailedFiqhRules = "Those who have some income or assets, but it satisfies only half or more of their essential living costs, remaining below the Nisab threshold of independent sufficiency.",
        recommendedSharePct = 0.25
    ),
    AMILINA(
        id = "amilina",
        arabicName = "العاملين عليها",
        englishName = "Al-'Amilina 'Alayha (Administrators)",
        shortDescription = "Authorized collectors, accountants, and distributors of Zakat.",
        detailedFiqhRules = "Trustees and workers appointed by legitimate community authorities to collect, audit, safeguard, and disburse Zakat. Their compensation is based on market labor value, not exceeding an equitable wage.",
        recommendedSharePct = 0.05
    ),
    MUALLAFA(
        id = "muallafa",
        arabicName = "المؤلفة قلوبهم",
        englishName = "Al-Mu'allafati Qulubuhum (Reconciling Hearts)",
        shortDescription = "New Muslims, those inclined towards Islam, or to alleviate hostility.",
        detailedFiqhRules = "Persons whose hearts are drawn towards Islam, or new converts facing economic hardship, family ostracization, or requiring social and financial rehabilitation.",
        recommendedSharePct = 0.05
    ),
    RIQAB(
        id = "riqab",
        arabicName = "في الرقاب",
        englishName = "Fir-Riqab (Freeing Captives)",
        shortDescription = "Liberating human beings from bondage, unjust detention, or human trafficking.",
        detailedFiqhRules = "Traditionally freeing enslaved persons under contract (Mukatab). In contemporary times, applied by major Fiqh councils to ransoming prisoners of war, victims of modern human trafficking, and bonded laborers.",
        recommendedSharePct = 0.05
    ),
    GHARIMIN(
        id = "gharimin",
        arabicName = "الغارمين",
        englishName = "Al-Gharimin (Debtors in Distress)",
        shortDescription = "Individuals overwhelmed by essential debts incurred for lawful survival or community welfare.",
        detailedFiqhRules = "Debtors unable to repay debts incurred for permissible necessities (medical treatments, essential housing, funeral costs, or arbitrating peaceful community disputes), provided the debt was not for haram or luxury.",
        recommendedSharePct = 0.15
    ),
    FISABILILLAH(
        id = "fisabilillah",
        arabicName = "في سبيل الله",
        englishName = "Fi Sabilillah (In the Cause of Allah)",
        shortDescription = "Defense of the faith, Islamic education, dawah, and humanitarian struggle.",
        detailedFiqhRules = "Classical view: Striving and volunteer fighters defending Muslim communities. Majority contemporary scholars include institutional efforts that preserve Islam, establish authentic dawah, and build educational welfare frameworks for vulnerable believers.",
        recommendedSharePct = 0.15
    ),
    IBNUSSABIL(
        id = "ibnussabil",
        arabicName = "ابن السبيل",
        englishName = "Ibn As-Sabil (The Stranded Traveler / Wayfarer)",
        shortDescription = "Travelers stranded away from their home and resources without funds to return.",
        detailedFiqhRules = "A traveler who is cut off from their wealth and needs financial assistance to return safely to their homeland or sustain themselves during permissible travel, even if wealthy in their hometown.",
        recommendedSharePct = 0.05
    );

    val englishTitle: String get() = englishName
    val quranicDescription: String get() = shortDescription
    val eligibilityConditions: String get() = detailedFiqhRules

    companion object {
        fun fromId(id: String): AsnafCategory {
            return entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: FUQARA
        }
    }
}

data class AsnafAllocation(
    val category: AsnafCategory,
    val allocatedAmount: Double = 0.0,
    val beneficiaryNote: String = "",
    val isCompleted: Boolean = false
) {
    fun getPercentageOfTotal(totalZakatDue: Double): Double {
        if (totalZakatDue <= 0.0) return 0.0
        return (allocatedAmount / totalZakatDue) * 100.0
    }
}

data class AsnafDistributionPlan(
    val totalZakatDue: Double,
    val currencySymbol: String,
    val currencyCode: String,
    val allocations: Map<String, AsnafAllocation> = emptyMap(),
    val notes: String = ""
) {
    val totalAllocated: Double
        get() = allocations.values.sumOf { it.allocatedAmount }

    val remainingUnallocated: Double
        get() = (totalZakatDue - totalAllocated).coerceAtLeast(0.0)

    val allocationProgressPct: Double
        get() = if (totalZakatDue > 0.0) (totalAllocated / totalZakatDue).coerceIn(0.0, 1.0) else 0.0

    val isFullyAllocated: Boolean
        get() = totalZakatDue > 0.0 && totalAllocated >= (totalZakatDue - 0.01)

    fun formattedSummary(): String {
        val sb = StringBuilder()
        sb.append("Surah At-Tawbah 9:60 Distribution Plan\n")
        sb.append("Total Zakat: $currencySymbol ${String.format(Locale.US, "%,.2f", totalZakatDue)}\n")
        sb.append("Allocated  : $currencySymbol ${String.format(Locale.US, "%,.2f", totalAllocated)} (${String.format(Locale.US, "%.1f%%", allocationProgressPct * 100)})\n")
        sb.append("Remaining  : $currencySymbol ${String.format(Locale.US, "%,.2f", remainingUnallocated)}\n\n")
        allocations.values.filter { it.allocatedAmount > 0.0 }.forEach { a ->
            sb.append("• ${a.category.arabicName} ${a.category.englishName}: $currencySymbol ${String.format(Locale.US, "%,.2f", a.allocatedAmount)}")
            if (a.beneficiaryNote.isNotBlank()) {
                sb.append(" (${a.beneficiaryNote})")
            }
            sb.append("\n")
        }
        return sb.toString()
    }
}
