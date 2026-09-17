package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "zakat_records")
data class ZakatRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val refNumber: String,
    val timestamp: Long,
    val hijriYear: String,
    val label: String,
    val cashOnHand: Double,
    val bankCurrent: Double,
    val bankSavings: Double,
    val goldGrams: Double,
    val goldRate: Double,
    val silverGrams: Double,
    val silverRate: Double,
    val stocksEquities: Double,
    val mutualFunds: Double,
    val businessCash: Double,
    val businessInventory: Double,
    val moneyOwed: Double,
    val receivablesRecoverable: Boolean,
    val debtorName: String,
    val deductibleDebts: Double,
    val debtDescription: String,
    val nisabStandard: String, // "Silver" or "Gold"
    val grossAssets: Double,
    val deductions: Double,
    val netWealth: Double,
    val zakatDue: Double,
    val hashDigest: String,
    val currencyCode: String = "PKR",
    val currencySymbol: String = "₨",
    val profileName: String = "Personal",
    val madhab: String = "Hanafi",
    val goldKarat: String = "24K",
    val weightUnit: String = "Grams",
    val jewelryExemptApplied: Boolean = false,
    val customAssetsTotal: Double = 0.0,
    val customDebtsTotal: Double = 0.0,
    val hawlDateMillis: Long = 0L
)
