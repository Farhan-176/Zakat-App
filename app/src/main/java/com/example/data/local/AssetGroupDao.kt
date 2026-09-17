package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AssetGroup
import kotlinx.coroutines.flow.Flow

@Dao
interface AssetGroupDao {

    @Query("SELECT * FROM asset_groups ORDER BY updatedAt DESC")
    fun getAllAssetGroups(): Flow<List<AssetGroup>>

    @Query("SELECT * FROM asset_groups WHERE id = :id")
    suspend fun getAssetGroupById(id: Long): AssetGroup?

    @Query("SELECT * FROM asset_groups WHERE id = :id")
    fun getAssetGroupByIdFlow(id: Long): Flow<AssetGroup?>

    @Query("SELECT * FROM asset_groups WHERE category = :category ORDER BY updatedAt DESC")
    fun getAssetGroupsByCategory(category: String): Flow<List<AssetGroup>>

    @Query("SELECT * FROM asset_groups WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR tags LIKE '%' || :query || '%' ORDER BY updatedAt DESC")
    fun searchAssetGroups(query: String): Flow<List<AssetGroup>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssetGroup(assetGroup: AssetGroup): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(assetGroups: List<AssetGroup>)

    @Update
    suspend fun updateAssetGroup(assetGroup: AssetGroup)

    @Delete
    suspend fun deleteAssetGroup(assetGroup: AssetGroup)

    @Query("DELETE FROM asset_groups WHERE id = :id")
    suspend fun deleteAssetGroupById(id: Long)

    @Query("SELECT COUNT(*) FROM asset_groups")
    suspend fun getAssetGroupCount(): Int
}
