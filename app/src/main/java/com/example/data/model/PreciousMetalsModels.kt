package com.example.data.model

enum class Karat(val label: String, val purityRatio: Double) {
    K24("24K (99.9% Pure)", 1.0),
    K22("22K (91.6% Pure)", 0.9167),
    K21("21K (87.5% Pure)", 0.875),
    K18("18K (75.0% Pure)", 0.75),
    K14("14K (58.3% Pure)", 0.5833);

    val shortName: String get() = name.removePrefix("K") + "K"
    val purityPercent: String get() = "${kotlin.math.round(purityRatio * 100).toInt()}%"

    companion object {
        fun fromLabel(label: String): Karat {
            return entries.firstOrNull { it.name == label || it.label.startsWith(label) } ?: K24
        }
    }

    fun getRatePerGram(base24kRate: Double): Double = base24kRate * purityRatio
    fun getRatePerTola(base24kRate: Double): Double = (base24kRate * purityRatio) * WeightUnit.TOLAS.toGramsMultiplier
}

data class KaratRateItem(
    val karat: Karat,
    val purityPercentage: String,
    val ratePerGram: Double,
    val ratePerTola: Double,
    val formattedRatePerGram: String,
    val ratePerOunce: Double = 0.0,
    val formattedRatePerTola: String = "",
    val formattedRatePerOunce: String = ""
) {
    val purityPercent: String get() = purityPercentage
    val formattedPerGram: String get() = formattedRatePerGram
    val formattedPerTola: String get() = formattedRatePerTola.ifBlank { formattedRatePerGram }
    val formattedPerOunce: String get() = formattedRatePerOunce.ifBlank { formattedRatePerGram }
}

object KaratRateCalculator {
    fun calculateMatrix(base24kPerGram: Double, currencySymbol: String): List<KaratRateItem> {
        return Karat.entries.map { k ->
            val perGram = k.getRatePerGram(base24kPerGram)
            val perTola = k.getRatePerTola(base24kRate = base24kPerGram)
            val perOunce = (base24kPerGram * k.purityRatio) * WeightUnit.TROY_OUNCES.toGramsMultiplier
            val pct = String.format(java.util.Locale.US, "%.1f%%", k.purityRatio * 100)
            val fg = "$currencySymbol ${String.format(java.util.Locale.US, "%,.1f", perGram)}"
            val ft = "$currencySymbol ${String.format(java.util.Locale.US, "%,.1f", perTola)}"
            val fo = "$currencySymbol ${String.format(java.util.Locale.US, "%,.1f", perOunce)}"
            KaratRateItem(
                karat = k,
                purityPercentage = pct,
                ratePerGram = perGram,
                ratePerTola = perTola,
                formattedRatePerGram = fg,
                ratePerOunce = perOunce,
                formattedRatePerTola = ft,
                formattedRatePerOunce = fo
            )
        }
    }
}

enum class WeightUnit(val label: String, val toGramsMultiplier: Double) {
    GRAMS("Grams (g)", 1.0),
    TOLAS("Tolas (11.66g)", 11.6638),
    TROY_OUNCES("Troy Ounces (31.10g)", 31.1035);

    companion object {
        fun fromLabel(label: String): WeightUnit {
            return entries.firstOrNull { it.name == label || it.label.startsWith(label) } ?: GRAMS
        }
    }
}
