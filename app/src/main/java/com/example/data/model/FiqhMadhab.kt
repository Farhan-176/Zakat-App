package com.example.data.model

enum class FiqhMadhab(
    val title: String,
    val recommendedNisab: String,
    val personalJewelryExemptAllowed: Boolean,
    val description: String,
    val keyScholarlySource: String
) {
    HANAFI(
        title = "Hanafi",
        recommendedNisab = "Silver",
        personalJewelryExemptAllowed = false,
        description = "Recommends Silver Nisab (benefits the poor / Anfa' lil-Fuqara). All gold and silver items (including personal jewelry) are strictly Zakatable.",
        keyScholarlySource = "Al-Hidaya, Fatawa Alamgiri"
    ),
    SHAFII(
        title = "Shafi'i",
        recommendedNisab = "Gold",
        personalJewelryExemptAllowed = true,
        description = "Recommends Gold Nisab for currency. Permissible customary personal jewelry worn by women is exempt from Zakat unless kept as bullion investment.",
        keyScholarlySource = "Kitab Al-Umm, Minhaj At-Talibin"
    ),
    MALIKI(
        title = "Maliki",
        recommendedNisab = "Gold",
        personalJewelryExemptAllowed = true,
        description = "Recommends Gold standard. Customary personal jewelry is exempt. Business inventory evaluated at realistic liquid market liquidation value.",
        keyScholarlySource = "Al-Mudawwana, Mukhtasar Khalil"
    ),
    HANBALI(
        title = "Hanbali",
        recommendedNisab = "Silver",
        personalJewelryExemptAllowed = true,
        description = "Permissible personal jewelry exempt. Debts owed to you only zakatable if borrower is solvent and capable of immediate repayment.",
        keyScholarlySource = "Al-Mughni (Ibn Qudamah)"
    );

    companion object {
        fun fromName(name: String): FiqhMadhab {
            return entries.firstOrNull { it.name.equals(name, ignoreCase = true) || it.title.equals(name, ignoreCase = true) } ?: HANAFI
        }
    }
}
