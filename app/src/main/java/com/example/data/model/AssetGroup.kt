package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a user-defined collection of hotlinked images representing
 * assets, verification proofs, receipts, or gold/silver holdings.
 */
@Entity(tableName = "asset_groups")
data class AssetGroup(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val category: String = "Zakat Verification",
    val imageUrls: List<String> = emptyList(),
    val coverImageUrl: String = "",
    val tags: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val itemCount: Int
        get() = imageUrls.size

    val effectiveCoverUrl: String
        get() = coverImageUrl.ifBlank { imageUrls.firstOrNull() ?: "" }
}

/**
 * Predefined categories for organizing asset groups.
 */
object AssetGroupCategories {
    const val ALL = "All"
    const val ZAKAT_VERIFICATION = "Zakat Verification"
    const val GOLD_SILVER = "Gold & Silver"
    const val CHARITY_RECEIPTS = "Charity Receipts"
    const val PHYSICAL_ASSETS = "Physical Assets"
    const val HALAL_PORTFOLIO = "Halal Portfolio"

    val list = listOf(
        ZAKAT_VERIFICATION,
        GOLD_SILVER,
        CHARITY_RECEIPTS,
        PHYSICAL_ASSETS,
        HALAL_PORTFOLIO
    )
}
