package com.example.data.model

data class CustomAssetItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val grossValue: Double,
    val zakatablePercentage: Double = 1.0 // 1.0 = 100%, 0.3 = 30%, etc.
) {
    val zakatableValue: Double
        get() = grossValue * zakatablePercentage
}

data class CustomDebtItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val description: String,
    val amount: Double,
    val categoryTag: String = "Short-Term Obligation"
)
