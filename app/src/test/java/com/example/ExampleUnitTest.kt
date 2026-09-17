package com.example

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun zakatRate_isCorrect() {
        val netZakatable = 100000.0
        val zakatDue = netZakatable * 0.025
        assertEquals(2500.0, zakatDue, 0.001)
    }

    // Helper validation function mimicking ZakatViewModel.validateAmount
    private fun validateAmount(input: String): String? {
        val trimmed = input.trim()
        return when {
            trimmed.isEmpty() -> null
            trimmed.contains("-") -> "Amount cannot be negative"
            trimmed.count { it == '.' } > 1 -> "Invalid decimal format"
            trimmed.any { !it.isDigit() && it != '.' } -> "Only non-negative numbers are permitted"
            trimmed.toDoubleOrNull() == null -> "Please enter a valid numerical amount"
            (trimmed.toDoubleOrNull() ?: 0.0) < 0.0 -> "Amount cannot be negative"
            (trimmed.toDoubleOrNull() ?: 0.0) > 1e14 -> "Amount exceeds maximum supported limit"
            else -> null
        }
    }

    @Test
    fun validation_rejectsNegativeValues() {
        assertNotNull(validateAmount("-1"))
        assertNotNull(validateAmount("-0.01"))
        assertNotNull(validateAmount("-5000"))
        assertTrue(validateAmount("-500")?.contains("negative", ignoreCase = true) == true)
    }

    @Test
    fun validation_rejectsInvalidCharacters() {
        assertNotNull(validateAmount("abc"))
        assertNotNull(validateAmount("12a34"))
        assertNotNull(validateAmount("$100"))
        assertNotNull(validateAmount("50,000"))
        assertNotNull(validateAmount("1.2.3"))
    }

    @Test
    fun validation_acceptsValidNonNegativeNumbers() {
        assertNull(validateAmount(""))
        assertNull(validateAmount("0"))
        assertNull(validateAmount("0.0"))
        assertNull(validateAmount("100"))
        assertNull(validateAmount("25000.50"))
        assertNull(validateAmount("1000000000"))
    }

    @Test
    fun validation_rejectsExtremelyLargeNumbers() {
        assertNotNull(validateAmount("1000000000000000"))
    }

    @Test
    fun groundedNisabRates_calculatesThresholdsCorrectly() {
        val goldPerGram = 75.0
        val silverPerGram = 0.95
        val expectedGoldNisab = 87.48 * goldPerGram
        val expectedSilverNisab = 612.36 * silverPerGram

        val rates = com.example.data.model.GroundedNisabRates(
            goldPerGram = goldPerGram,
            silverPerGram = silverPerGram,
            currencyCode = "USD",
            isGrounded = true,
            sources = listOf(
                com.example.data.model.GroundingSource(
                    title = "Reuters Gold",
                    url = "https://reuters.com"
                )
            )
        )

        assertEquals(expectedGoldNisab, rates.goldNisabThreshold, 0.01)
        assertEquals(expectedSilverNisab, rates.silverNisabThreshold, 0.001)
        assertTrue(rates.isGrounded)
        assertEquals(1, rates.sources.size)
    }

    @Test
    fun calibratedFallbackRates_returnsValidValuesForSupportedCurrencies() {
        val currencies = com.example.data.model.AvailableCurrencies.all
        currencies.forEach { currency ->
            val fallbackGold = currency.defaultGoldPerGram
            val fallbackSilver = currency.defaultSilverPerGram
            assertTrue("Gold rate should be positive for ${currency.code}", fallbackGold > 0.0)
            assertTrue("Silver rate should be positive for ${currency.code}", fallbackSilver > 0.0)

            val goldNisab = currency.getGoldNisabFloor(fallbackGold)
            val silverNisab = currency.getSilverNisabFloor(fallbackSilver)
            assertTrue("Gold Nisab floor should be positive for ${currency.code}", goldNisab > 0.0)
            assertTrue("Silver Nisab floor should be positive for ${currency.code}", silverNisab > 0.0)
        }
    }

    @Test
    fun assetGroup_coverUrlFallback_worksAsExpected() {
        val groupWithCover = com.example.data.model.AssetGroup(
            name = "Test Group",
            category = "Gold",
            coverImageUrl = "https://example.com/cover.jpg",
            imageUrls = listOf("https://example.com/item1.jpg")
        )
        assertEquals("https://example.com/cover.jpg", groupWithCover.effectiveCoverUrl)

        val groupWithoutCover = com.example.data.model.AssetGroup(
            name = "Test Group 2",
            category = "Gold",
            coverImageUrl = "",
            imageUrls = listOf("https://example.com/item1.jpg", "https://example.com/item2.jpg")
        )
        assertEquals("https://example.com/item1.jpg", groupWithoutCover.effectiveCoverUrl)
    }

    @Test
    fun karat_shortNameAndPurityPercent_areCorrect() {
        assertEquals("24K", com.example.data.model.Karat.K24.shortName)
        assertEquals("100%", com.example.data.model.Karat.K24.purityPercent)

        assertEquals("22K", com.example.data.model.Karat.K22.shortName)
        assertEquals("92%", com.example.data.model.Karat.K22.purityPercent)

        assertEquals("21K", com.example.data.model.Karat.K21.shortName)
        assertEquals("88%", com.example.data.model.Karat.K21.purityPercent)

        assertEquals("18K", com.example.data.model.Karat.K18.shortName)
        assertEquals("75%", com.example.data.model.Karat.K18.purityPercent)

        assertEquals("14K", com.example.data.model.Karat.K14.shortName)
        assertEquals("58%", com.example.data.model.Karat.K14.purityPercent)
    }

    @Test
    fun zakatCalculation_assetsAndDebtsValidation_rejectsNegatives() {
        val testDebts = listOf("-100", "-0.5", "-50000")
        testDebts.forEach { debt ->
            val error = validateAmount(debt)
            assertNotNull("Debt $debt must produce a validation error", error)
            assertTrue(error!!.contains("negative", ignoreCase = true))
        }

        val testAssets = listOf("-1000", "-25.5")
        testAssets.forEach { asset ->
            val error = validateAmount(asset)
            assertNotNull("Asset $asset must produce a validation error", error)
            assertTrue(error!!.contains("negative", ignoreCase = true))
        }
    }
}

