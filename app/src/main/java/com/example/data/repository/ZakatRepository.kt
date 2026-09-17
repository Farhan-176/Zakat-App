package com.example.data.repository

import com.example.data.local.ZakatDao
import com.example.data.model.ZakatRecord
import kotlinx.coroutines.flow.Flow
import java.security.MessageDigest

class ZakatRepository(private val zakatDao: ZakatDao) {

    val allRecords: Flow<List<ZakatRecord>> = zakatDao.getAllRecords()

    suspend fun insertRecord(record: ZakatRecord): Long {
        return zakatDao.insertRecord(record)
    }

    suspend fun insertRecords(records: List<ZakatRecord>) {
        zakatDao.insertAll(records)
    }

    suspend fun deleteRecord(record: ZakatRecord) {
        zakatDao.deleteRecord(record)
    }

    suspend fun getRecordById(id: Long): ZakatRecord? {
        return zakatDao.getRecordById(id)
    }

    suspend fun seedSampleIfEmpty() {
        if (zakatDao.getRecordCount() == 0) {
            val sample = ZakatRecord(
                refNumber = "ZC-2026-000123",
                timestamp = System.currentTimeMillis() - 86400000L * 14,
                hijriYear = "1447 AH Hawl",
                label = "Ramadan 2026 / Business Vault",
                cashOnHand = 80000.0,
                bankCurrent = 120000.0,
                bankSavings = 300000.0,
                goldGrams = 45.0,
                goldRate = 25000.0,
                silverGrams = 200.0,
                silverRate = 280.0,
                stocksEquities = 95000.0,
                mutualFunds = 55000.0,
                businessCash = 120000.0,
                businessInventory = 300000.0,
                moneyOwed = 0.0,
                receivablesRecoverable = true,
                debtorName = "",
                deductibleDebts = 50000.0,
                debtDescription = "Current Month Utility & Rent",
                nisabStandard = "Silver",
                grossAssets = 850000.0,
                deductions = 50000.0,
                netWealth = 800000.0,
                zakatDue = 20000.0,
                hashDigest = generateHash("ZC-2026-000123", 800000.0, 20000.0)
            )
            zakatDao.insertRecord(sample)
        }
    }

    companion object {
        fun generateHash(ref: String, net: Double, due: Double): String {
            val raw = "$ref:$net:$due:SOVEREIGN_OFFLINE"
            val bytes = MessageDigest.getInstance("SHA-256").digest(raw.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        }
    }
}
