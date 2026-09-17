package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.model.AssetGroup
import com.example.data.model.ZakatRecord

@Database(
    entities = [ZakatRecord::class, AssetGroup::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(AssetGroupConverters::class)
abstract class ZakatDatabase : RoomDatabase() {
    abstract fun zakatDao(): ZakatDao
    abstract fun assetGroupDao(): AssetGroupDao

    companion object {
        @Volatile
        private var INSTANCE: ZakatDatabase? = null

        fun getDatabase(context: Context): ZakatDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ZakatDatabase::class.java,
                    "zakat_companion_vault.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
