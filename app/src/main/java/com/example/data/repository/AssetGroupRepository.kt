package com.example.data.repository

import com.example.data.local.AssetGroupDao
import com.example.data.model.AssetGroup
import com.example.data.model.AssetGroupCategories
import kotlinx.coroutines.flow.Flow

class AssetGroupRepository(private val assetGroupDao: AssetGroupDao) {

    val allAssetGroups: Flow<List<AssetGroup>> = assetGroupDao.getAllAssetGroups()

    fun getAssetGroupsByCategory(category: String): Flow<List<AssetGroup>> {
        return if (category == AssetGroupCategories.ALL) {
            assetGroupDao.getAllAssetGroups()
        } else {
            assetGroupDao.getAssetGroupsByCategory(category)
        }
    }

    fun searchAssetGroups(query: String): Flow<List<AssetGroup>> {
        return assetGroupDao.searchAssetGroups(query)
    }

    suspend fun getById(id: Long): AssetGroup? {
        return assetGroupDao.getAssetGroupById(id)
    }

    fun getByIdFlow(id: Long): Flow<AssetGroup?> {
        return assetGroupDao.getAssetGroupByIdFlow(id)
    }

    suspend fun insert(assetGroup: AssetGroup): Long {
        return assetGroupDao.insertAssetGroup(assetGroup)
    }

    suspend fun update(assetGroup: AssetGroup) {
        assetGroupDao.updateAssetGroup(assetGroup.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun delete(assetGroup: AssetGroup) {
        assetGroupDao.deleteAssetGroup(assetGroup)
    }

    suspend fun deleteById(id: Long) {
        assetGroupDao.deleteAssetGroupById(id)
    }

    suspend fun addImageUrl(groupId: Long, imageUrl: String) {
        val group = assetGroupDao.getAssetGroupById(groupId) ?: return
        val trimmedUrl = imageUrl.trim()
        if (trimmedUrl.isBlank() || group.imageUrls.contains(trimmedUrl)) return
        val updatedList = group.imageUrls + trimmedUrl
        val cover = if (group.coverImageUrl.isBlank()) trimmedUrl else group.coverImageUrl
        assetGroupDao.updateAssetGroup(
            group.copy(
                imageUrls = updatedList,
                coverImageUrl = cover,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun removeImageUrl(groupId: Long, imageUrl: String) {
        val group = assetGroupDao.getAssetGroupById(groupId) ?: return
        val updatedList = group.imageUrls.filter { it != imageUrl }
        val cover = if (group.coverImageUrl == imageUrl) {
            updatedList.firstOrNull() ?: ""
        } else {
            group.coverImageUrl
        }
        assetGroupDao.updateAssetGroup(
            group.copy(
                imageUrls = updatedList,
                coverImageUrl = cover,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun setCoverImageUrl(groupId: Long, imageUrl: String) {
        val group = assetGroupDao.getAssetGroupById(groupId) ?: return
        assetGroupDao.updateAssetGroup(
            group.copy(
                coverImageUrl = imageUrl.trim(),
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun seedDefaultGroupsIfEmpty() {
        if (assetGroupDao.getAssetGroupCount() > 0) return

        val sampleGroups = listOf(
            AssetGroup(
                name = "Gold Bullion & Vault Holdings",
                description = "Photographic documentation of 24K pure bullion bars and certified hallmarks for annual Hawl valuation.",
                category = AssetGroupCategories.GOLD_SILVER,
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1610375461246-83df859d849d?auto=format&fit=crop&w=800&q=80",
                    "https://images.unsplash.com/photo-1624365168968-f283b5d96379?auto=format&fit=crop&w=800&q=80",
                    "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?auto=format&fit=crop&w=800&q=80"
                ),
                coverImageUrl = "https://images.unsplash.com/photo-1610375461246-83df859d849d?auto=format&fit=crop&w=800&q=80",
                tags = "gold, bullion, vault, 24k"
            ),
            AssetGroup(
                name = "Ramadan 1447 Distribution Receipts",
                description = "Verification receipts, ration hampers, and handover confirmation records for local Asnaf beneficiaries.",
                category = AssetGroupCategories.CHARITY_RECEIPTS,
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?auto=format&fit=crop&w=800&q=80",
                    "https://images.unsplash.com/photo-1532629345422-7515f3d16bb9?auto=format&fit=crop&w=800&q=80",
                    "https://images.unsplash.com/photo-1593113598332-cd288d649433?auto=format&fit=crop&w=800&q=80"
                ),
                coverImageUrl = "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?auto=format&fit=crop&w=800&q=80",
                tags = "charity, receipts, asnaf, distribution"
            ),
            AssetGroup(
                name = "Commercial Wholesale Inventory",
                description = "Physical audit photos of raw stock, warehouse goods, and salable trade inventory for current Zakat cycle.",
                category = AssetGroupCategories.PHYSICAL_ASSETS,
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1586528116311-ad8dd3c8310d?auto=format&fit=crop&w=800&q=80",
                    "https://images.unsplash.com/photo-1553413077-190dd305871c?auto=format&fit=crop&w=800&q=80"
                ),
                coverImageUrl = "https://images.unsplash.com/photo-1586528116311-ad8dd3c8310d?auto=format&fit=crop&w=800&q=80",
                tags = "inventory, warehouse, trade, wholesale"
            )
        )

        assetGroupDao.insertAll(sampleGroups)
    }
}
