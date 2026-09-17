package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.ZakatRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface ZakatDao {
    @Query("SELECT * FROM zakat_records ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<ZakatRecord>>

    @Query("SELECT * FROM zakat_records WHERE id = :id")
    suspend fun getRecordById(id: Long): ZakatRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: ZakatRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(records: List<ZakatRecord>)

    @Delete
    suspend fun deleteRecord(record: ZakatRecord)

    @Query("SELECT COUNT(*) FROM zakat_records")
    suspend fun getRecordCount(): Int
}
