package com.example.data.model

data class CurrencyInfo(
    val code: String,
    val name: String,
    val symbol: String,
    val defaultGoldPerGram: Double,
    val defaultSilverPerGram: Double,
    val defaultSilverNisabGrams: Double = 612.36,
    val defaultGoldNisabGrams: Double = 87.48
) {
    fun getSilverNisabFloor(silverRate: Double): Double = defaultSilverNisabGrams * silverRate
    fun getGoldNisabFloor(goldRate: Double): Double = defaultGoldNisabGrams * goldRate
}

object AvailableCurrencies {
    val all = listOf(
        CurrencyInfo("PKR", "Pakistani Rupee", "₨", 25000.0, 280.0),
        CurrencyInfo("USD", "US Dollar", "$", 85.0, 1.05),
        CurrencyInfo("GBP", "British Pound", "£", 65.0, 0.82),
        CurrencyInfo("EUR", "Euro", "€", 78.0, 0.98),
        CurrencyInfo("SAR", "Saudi Riyal", "SR", 318.0, 3.95),
        CurrencyInfo("AED", "UAE Dirham", "AED", 312.0, 3.86),
        CurrencyInfo("INR", "Indian Rupee", "₹", 7100.0, 88.0),
        CurrencyInfo("CAD", "Canadian Dollar", "CA$", 115.0, 1.42),
        CurrencyInfo("AUD", "Australian Dollar", "A$", 128.0, 1.58),
        CurrencyInfo("TRY", "Turkish Lira", "₺", 2750.0, 34.0),
        CurrencyInfo("MYR", "Malaysian Ringgit", "RM", 375.0, 4.65),
        CurrencyInfo("IDR", "Indonesian Rupiah", "Rp", 1320000.0, 16500.0)
    )

    fun find(code: String): CurrencyInfo {
        return all.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: all.first()
    }
}
