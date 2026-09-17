package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Zakat Companion", appName)
  }

  @Test
  fun `assetGroupConverters serializes and deserializes correctly`() {
    val converters = com.example.data.local.AssetGroupConverters()
    val originalList = listOf("https://example.com/img1.jpg", "https://example.com/img2.png")
    val json = converters.fromStringList(originalList)
    val result = converters.toStringList(json)
    assertEquals(originalList, result)

    // Test empty list
    val emptyJson = converters.fromStringList(emptyList())
    val emptyResult = converters.toStringList(emptyJson)
    assertEquals(emptyList<String>(), emptyResult)

    // Test blank string
    val blankResult = converters.toStringList("")
    assertEquals(emptyList<String>(), blankResult)
  }

  @Test
  fun `pdf report file is generated with correct naming and non-empty content`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val record = com.example.data.model.ZakatRecord(
        refNumber = "ZC-TEST-001",
        timestamp = System.currentTimeMillis(),
        hijriYear = "1448 AH Hawl",
        label = "Annual Assessment Test",
        cashOnHand = 5000.0,
        bankCurrent = 12000.0,
        bankSavings = 25000.0,
        goldGrams = 20.0,
        goldRate = 75.0,
        silverGrams = 100.0,
        silverRate = 1.0,
        stocksEquities = 8000.0,
        mutualFunds = 4000.0,
        businessCash = 3000.0,
        businessInventory = 6000.0,
        moneyOwed = 1500.0,
        receivablesRecoverable = true,
        debtorName = "Client A",
        deductibleDebts = 2000.0,
        debtDescription = "Monthly obligations",
        nisabStandard = "Silver",
        grossAssets = 64600.0,
        deductions = 2000.0,
        netWealth = 62600.0,
        zakatDue = 1565.0,
        hashDigest = "ZC-TEST-DIGEST-ABC123XYZ",
        currencyCode = "USD",
        currencySymbol = "$",
        profileName = "Personal",
        madhab = "Hanafi",
        goldKarat = "24K",
        weightUnit = "Grams",
        jewelryExemptApplied = false,
        customAssetsTotal = 0.0,
        customDebtsTotal = 0.0,
        hawlDateMillis = System.currentTimeMillis() + 86400000L * 354
    )
    val pdfFile = com.example.util.PdfReportGenerator.generateZakatPdf(context, record)
    org.junit.Assert.assertTrue(pdfFile.exists())
    org.junit.Assert.assertTrue(pdfFile.name.contains("ZC-TEST-001"))
    org.junit.Assert.assertTrue(pdfFile.length() > 0)
  }
}
