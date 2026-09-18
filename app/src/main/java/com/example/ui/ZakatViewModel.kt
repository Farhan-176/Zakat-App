package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.AssetGroup
import com.example.data.model.AssetGroupCategories
import com.example.data.model.AvailableCurrencies
import com.example.data.model.CurrencyInfo
import com.example.data.model.CustomAssetItem
import com.example.data.model.CustomDebtItem
import com.example.data.model.DefaultProfiles
import com.example.data.model.FiqhMadhab
import com.example.data.model.GroundedNisabRates
import com.example.data.model.AsnafAllocation
import com.example.data.model.AsnafCategory
import com.example.data.model.AsnafDistributionPlan
import com.example.data.model.GroundingSource
import com.example.data.model.HawlContinuityRuling
import com.example.data.model.HawlCycleState
import com.example.data.model.HijriMilestone
import com.example.data.model.Karat
import com.example.data.model.KaratRateCalculator
import com.example.data.model.KaratRateItem
import com.example.data.model.NisabEvaluation
import com.example.data.model.NisabTrendGenerator
import com.example.data.model.NisabTrendPoint
import com.example.data.model.PortfolioProfile
import com.example.data.model.RateSyncStatus
import com.example.data.model.SyncInterval
import com.example.data.model.WeightUnit
import com.example.data.model.ZakatRecord
import com.example.data.repository.AssetGroupRepository
import com.example.data.repository.ZakatRepository
import com.example.data.service.GeminiNisabRateService
import com.example.util.PortabilityUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class AppTab {
    HOME,
    CALCULATION,
    HISTORY,
    GUIDANCE,
    ASSETS
}

enum class AppFlowState {
    AUTH,
    LANGUAGE_SETUP,
    MAIN_APP
}

class ZakatViewModel(
    private val repository: ZakatRepository,
    private val assetGroupRepository: AssetGroupRepository? = null
) : ViewModel() {

    val savedRecords: StateFlow<List<ZakatRecord>> = repository.allRecords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // App Flow Navigation (Auth -> Language Setup -> Main App)
    private val _flowState = MutableStateFlow(AppFlowState.AUTH)
    val flowState: StateFlow<AppFlowState> = _flowState.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _isGuestUser = MutableStateFlow(false)
    val isGuestUser: StateFlow<Boolean> = _isGuestUser.asStateFlow()

    private val _currentUserEmail = MutableStateFlow("")
    val currentUserEmail: StateFlow<String> = _currentUserEmail.asStateFlow()

    private val _currentUserName = MutableStateFlow("")
    val currentUserName: StateFlow<String> = _currentUserName.asStateFlow()

    fun setFlowState(state: AppFlowState) {
        _flowState.value = state
    }

    fun login(email: String, pass: String): Boolean {
        if (email.isBlank() || pass.length < 4) return false
        val cleanEmail = email.trim()
        val derivedName = cleanEmail.substringBefore("@").replace(".", " ")
            .split(" ")
            .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
        _currentUserEmail.value = cleanEmail
        _currentUserName.value = derivedName.ifBlank { "Account" }
        _isLoggedIn.value = true
        _isGuestUser.value = false

        val profile = PortfolioProfile(
            id = "user_${cleanEmail.hashCode()}",
            name = derivedName.ifBlank { "Personal" },
            iconName = "Person",
            description = "Primary account ($cleanEmail)"
        )
        if (_profiles.value.none { it.id == profile.id }) {
            _profiles.value = listOf(profile) + _profiles.value
        }
        _activeProfile.value = profile
        _flowState.value = AppFlowState.LANGUAGE_SETUP
        return true
    }

    fun signUp(name: String, email: String, pass: String): Boolean {
        if (name.isBlank() || email.isBlank() || pass.length < 4) return false
        val cleanName = name.trim()
        val cleanEmail = email.trim()
        _currentUserName.value = cleanName
        _currentUserEmail.value = cleanEmail
        _isLoggedIn.value = true
        _isGuestUser.value = false

        val profile = PortfolioProfile(
            id = "user_${cleanEmail.hashCode()}",
            name = cleanName,
            iconName = "Person",
            description = "Registered account ($cleanEmail)"
        )
        if (_profiles.value.none { it.id == profile.id }) {
            _profiles.value = listOf(profile) + _profiles.value
        }
        _activeProfile.value = profile
        _flowState.value = AppFlowState.LANGUAGE_SETUP
        return true
    }

    fun continueAsGuest() {
        _isLoggedIn.value = false
        _isGuestUser.value = true
        _currentUserName.value = "Guest"
        _currentUserEmail.value = ""

        val guestProfile = PortfolioProfile(
            id = "guest_user",
            name = "Guest",
            iconName = "Person",
            description = "Anonymous Sovereign Session"
        )
        if (_profiles.value.none { it.id == guestProfile.id }) {
            _profiles.value = listOf(guestProfile) + _profiles.value
        }
        _activeProfile.value = guestProfile
        _flowState.value = AppFlowState.LANGUAGE_SETUP
    }

    fun completeLanguageSetup() {
        _flowState.value = AppFlowState.MAIN_APP
    }

    fun logout() {
        _isLoggedIn.value = false
        _isGuestUser.value = false
        _currentUserEmail.value = ""
        _currentUserName.value = ""
        _flowState.value = AppFlowState.AUTH
    }

    // Active Navigation Tab
    private val _currentTab = MutableStateFlow(AppTab.HOME)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    // Language: "en" or "ur"
    private val _language = MutableStateFlow("en")
    val language: StateFlow<String> = _language.asStateFlow()

    fun setLanguage(lang: String) {
        _language.value = lang
    }

    // Theme Mode: Light / Dark mode toggle (null = follow system, true = dark, false = light)
    // Defaulting to false (Light) or toggleable directly
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    fun setDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    // 1. Multi-Currency Engine & Google Search Grounding Rate Engine
    val rateService = GeminiNisabRateService()

    private val _selectedCurrency = MutableStateFlow(AvailableCurrencies.all.first())
    val selectedCurrency: StateFlow<CurrencyInfo> = _selectedCurrency.asStateFlow()

    var goldRateInput = MutableStateFlow(AvailableCurrencies.all.first().defaultGoldPerGram.toString())
    var silverRateInput = MutableStateFlow(AvailableCurrencies.all.first().defaultSilverPerGram.toString())

    private val _rateSyncStatus = MutableStateFlow<RateSyncStatus>(RateSyncStatus.Idle)
    val rateSyncStatus: StateFlow<RateSyncStatus> = _rateSyncStatus.asStateFlow()

    private val _groundedRates = MutableStateFlow<GroundedNisabRates?>(null)
    val groundedRates: StateFlow<GroundedNisabRates?> = _groundedRates.asStateFlow()

    private val _syncInterval = MutableStateFlow(SyncInterval.EVERY_30_MIN)
    val syncInterval: StateFlow<SyncInterval> = _syncInterval.asStateFlow()

    private val _autoApplyRates = MutableStateFlow(true)
    val autoApplyRates: StateFlow<Boolean> = _autoApplyRates.asStateFlow()

    private var periodicSyncJob: Job? = null

    fun startPeriodicRateSync() {
        periodicSyncJob?.cancel()
        periodicSyncJob = viewModelScope.launch {
            // Initial grounded fetch on app launch
            fetchLiveGroundedRates(force = false)
            while (isActive) {
                val intervalMins = _syncInterval.value.minutes
                if (intervalMins <= 0L) {
                    delay(30_000L)
                    continue
                }
                delay(intervalMins * 60_000L)
                fetchLiveGroundedRates(force = false)
            }
        }
    }

    fun setSyncInterval(interval: SyncInterval) {
        _syncInterval.value = interval
        startPeriodicRateSync()
    }

    fun setAutoApplyRates(autoApply: Boolean) {
        _autoApplyRates.value = autoApply
        if (autoApply) {
            applyGroundedRatesToCalculator(notify = false)
        }
    }

    fun fetchLiveGroundedRates(force: Boolean = true) {
        viewModelScope.launch {
            _rateSyncStatus.value = RateSyncStatus.Fetching("Fetching current gold/silver prices via Google Search & LBMA benchmarks...")
            val result = rateService.fetchGroundedRates(_selectedCurrency.value, force = force)
            result.onSuccess { rates ->
                _groundedRates.value = rates
                _rateSyncStatus.value = RateSyncStatus.Success(rates, "Live market spot rates verified via ${rates.sourceName}")
                if (_autoApplyRates.value) {
                    applyGroundedRatesToCalculator(notify = false)
                }
                val gVal = if (rates.goldPerGram >= 100) rates.goldPerGram.toInt() else String.format(Locale.US, "%.1f", rates.goldPerGram)
                val sVal = if (rates.silverPerGram >= 100) rates.silverPerGram.toInt() else String.format(Locale.US, "%.1f", rates.silverPerGram)
                _simulatedToast.value = "Nisab updated: Gold ${_selectedCurrency.value.symbol}$gVal/g • Silver ${_selectedCurrency.value.symbol}$sVal/g"
            }.onFailure { error ->
                val fallback = rateService.createCalibratedFallbackRates(
                    _selectedCurrency.value,
                    emptyMap(),
                    "Calibrated benchmark spot rates"
                )
                _groundedRates.value = fallback
                _rateSyncStatus.value = RateSyncStatus.Error(
                    error.message ?: "Unable to connect to Google Search grounding service. Calibrated rates active.",
                    fallback
                )
                if (_autoApplyRates.value && (goldRateInput.value.isBlank() || goldRateInput.value == "0")) {
                    applyGroundedRatesToCalculator(notify = false)
                }
            }
        }
    }

    fun applyGroundedRatesToCalculator(notify: Boolean = true) {
        val rates = _groundedRates.value ?: return
        val gStr = if (rates.goldPerGram >= 100) rates.goldPerGram.toInt().toString() else String.format(Locale.US, "%.2f", rates.goldPerGram)
        val sStr = if (rates.silverPerGram >= 100) rates.silverPerGram.toInt().toString() else String.format(Locale.US, "%.2f", rates.silverPerGram)
        goldRateInput.value = gStr
        silverRateInput.value = sStr
        if (notify) {
            _simulatedToast.value = "✨ Applied verified rates: Gold ${_selectedCurrency.value.symbol}$gStr/g • Silver ${_selectedCurrency.value.symbol}$sStr/g"
        }
    }

    fun selectCurrency(currency: CurrencyInfo) {
        _selectedCurrency.value = currency
        goldRateInput.value = if (currency.defaultGoldPerGram >= 100) currency.defaultGoldPerGram.toInt().toString() else currency.defaultGoldPerGram.toString()
        silverRateInput.value = if (currency.defaultSilverPerGram >= 100) currency.defaultSilverPerGram.toInt().toString() else currency.defaultSilverPerGram.toString()
        fetchLiveGroundedRates(force = true)
    }

    fun getGoldRate(): Double = goldRateInput.value.trim().toDoubleOrNull()?.takeIf { it >= 0.0 } ?: _selectedCurrency.value.defaultGoldPerGram
    fun getSilverRate(): Double = silverRateInput.value.trim().toDoubleOrNull()?.takeIf { it >= 0.0 } ?: _selectedCurrency.value.defaultSilverPerGram

    // 2. Multi-Profile Portfolio
    private val _profiles = MutableStateFlow(DefaultProfiles.defaults)
    val profiles: StateFlow<List<PortfolioProfile>> = _profiles.asStateFlow()

    private val _activeProfile = MutableStateFlow(DefaultProfiles.defaults.first())
    val activeProfile: StateFlow<PortfolioProfile> = _activeProfile.asStateFlow()

    private val _historyProfileFilter = MutableStateFlow<String?>(null)
    val historyProfileFilter: StateFlow<String?> = _historyProfileFilter.asStateFlow()

    fun selectProfile(profile: PortfolioProfile) {
        _activeProfile.value = profile
    }

    fun addCustomProfile(name: String) {
        if (name.isBlank()) return
        val newProfile = PortfolioProfile(
            id = "custom_${System.currentTimeMillis()}",
            name = name.trim(),
            iconName = "Badge",
            description = "Custom family/entity portfolio"
        )
        _profiles.value = _profiles.value + newProfile
        _activeProfile.value = newProfile
    }

    fun setHistoryProfileFilter(profileName: String?) {
        _historyProfileFilter.value = profileName
    }

    // 3. Fiqh & Madhab Preset
    private val _selectedMadhab = MutableStateFlow(FiqhMadhab.HANAFI)
    val selectedMadhab: StateFlow<FiqhMadhab> = _selectedMadhab.asStateFlow()

    var exemptPersonalJewelry = MutableStateFlow(false)

    fun setMadhab(madhab: FiqhMadhab) {
        _selectedMadhab.value = madhab
        nisabStandard.value = madhab.recommendedNisab
        if (!madhab.personalJewelryExemptAllowed) {
            exemptPersonalJewelry.value = false
        }
    }

    // 4. Gold Purity / Karats & Weight Units
    var selectedGoldKarat = MutableStateFlow(Karat.K24)
    var selectedWeightUnit = MutableStateFlow(WeightUnit.GRAMS)

    // Calculation Step (1 to 6)
    private val _calcStep = MutableStateFlow(1)
    val calcStep: StateFlow<Int> = _calcStep.asStateFlow()

    fun setCalcStep(step: Int) {
        _calcStep.value = step.coerceIn(1, 6)
    }

    // Inputs: Step 1 Liquid
    var cashOnHand = MutableStateFlow("80000")
    var bankCurrent = MutableStateFlow("120000")
    var bankSavings = MutableStateFlow("300000")

    // Inputs: Step 2 Metals & Business
    var goldWeightInput = MutableStateFlow("45")
    var silverWeightInput = MutableStateFlow("200")

    // Compatibility properties
    val goldGrams: MutableStateFlow<String> get() = goldWeightInput
    val silverGrams: MutableStateFlow<String> get() = silverWeightInput

    var businessCash = MutableStateFlow("120000")
    var businessInventory = MutableStateFlow("300000")

    // Inputs: Step 2 Investments
    var stocksEquities = MutableStateFlow("95000")
    var mutualFunds = MutableStateFlow("55000")

    // Validation
    /**
     * Strict validation for all asset and liability input fields.
     * Ensures only non-negative numerical values (>= 0) are accepted.
     * Returns an explicit human-readable error message or null if valid.
     */
    fun validateAmount(input: String): String? {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return null // Empty field is treated as 0 without error
        if (trimmed.contains("-")) return "Negative values are not permitted. Amount must be 0 or greater."
        if (trimmed.count { it == '.' } > 1) return "Invalid decimal format. Only one decimal point is permitted."
        if (trimmed.any { !it.isDigit() && it != '.' }) return "Only numerical digits and a decimal point are permitted."
        val num = trimmed.toDoubleOrNull() ?: return "Please enter a valid numerical amount."
        if (num < 0.0) return "Negative values are not permitted. Amount must be 0 or greater."
        if (num > 1e14) return "Amount exceeds maximum supported numerical limit."
        return null
    }

    fun isNonNegative(input: String): Boolean = validateAmount(input) == null

    fun getStep1Errors(): List<Pair<String, String>> {
        val errors = mutableListOf<Pair<String, String>>()
        validateAmount(cashOnHand.value)?.let { errors.add("Cash on Hand" to it) }
        validateAmount(bankCurrent.value)?.let { errors.add("Bank Current Account" to it) }
        validateAmount(bankSavings.value)?.let { errors.add("Bank Savings Account" to it) }
        return errors
    }

    fun getStep2Errors(): List<Pair<String, String>> {
        val errors = mutableListOf<Pair<String, String>>()
        validateAmount(goldWeightInput.value)?.let { errors.add("Gold Weight" to it) }
        validateAmount(silverWeightInput.value)?.let { errors.add("Silver Weight" to it) }
        validateAmount(goldRateInput.value)?.let { errors.add("Gold Rate" to it) }
        validateAmount(silverRateInput.value)?.let { errors.add("Silver Rate" to it) }
        validateAmount(businessCash.value)?.let { errors.add("Business Liquid Cash" to it) }
        validateAmount(businessInventory.value)?.let { errors.add("Business Inventory" to it) }
        validateAmount(stocksEquities.value)?.let { errors.add("Stocks & Equities" to it) }
        validateAmount(mutualFunds.value)?.let { errors.add("Mutual Funds & Sukuk" to it) }
        _customAssets.value.filter { it.grossValue < 0.0 }.forEach {
            errors.add("Custom Asset (${it.name})" to "Negative values are not permitted. Amount must be 0 or greater.")
        }
        return errors
    }

    fun getStep3Errors(): List<Pair<String, String>> {
        val errors = mutableListOf<Pair<String, String>>()
        validateAmount(moneyOwed.value)?.let { errors.add("Receivables (Money Owed)" to it) }
        validateAmount(deductibleDebts.value)?.let { errors.add("Deductible Liabilities" to it) }
        _customDebts.value.filter { it.amount < 0.0 }.forEach {
            errors.add("Custom Liability (${it.description})" to "Negative values are not permitted. Amount must be 0 or greater.")
        }
        return errors
    }

    fun isStep1Valid(): Boolean = getStep1Errors().isEmpty()
    fun isStep2Valid(): Boolean = getStep2Errors().isEmpty()
    fun isStep3Valid(): Boolean = getStep3Errors().isEmpty()

    // Dynamic Custom Assets
    private val _customAssets = MutableStateFlow<List<CustomAssetItem>>(listOf(
        CustomAssetItem(name = "Crypto / Bitcoin", grossValue = 0.0, zakatablePercentage = 1.0)
    ))
    val customAssets: StateFlow<List<CustomAssetItem>> = _customAssets.asStateFlow()

    fun addCustomAsset(name: String, grossValue: Double, zakatableRatio: Double) {
        if (name.isBlank() || grossValue < 0.0) return
        val item = CustomAssetItem(
            name = name.trim(),
            grossValue = grossValue.coerceAtLeast(0.0),
            zakatablePercentage = zakatableRatio.coerceIn(0.0, 1.0)
        )
        _customAssets.value = _customAssets.value + item
    }

    fun removeCustomAsset(id: String) {
        _customAssets.value = _customAssets.value.filterNot { it.id == id }
    }

    // Inputs: Step 3 Receivables
    var debtorName = MutableStateFlow("Personal loan to Ahmed")
    var moneyOwed = MutableStateFlow("0")
    var receivablesRecoverability = MutableStateFlow("confirmed") // confirmed, bad, doubtful

    // Inputs: Step 3 Debts & Deductions
    var debtType = MutableStateFlow("Current Month Utility & Rent")
    var deductibleDebts = MutableStateFlow("50000")

    // Dynamic Custom Debts
    private val _customDebts = MutableStateFlow<List<CustomDebtItem>>(emptyList())
    val customDebts: StateFlow<List<CustomDebtItem>> = _customDebts.asStateFlow()

    fun addCustomDebt(description: String, amount: Double, tag: String) {
        if (description.isBlank() || amount < 0.0) return
        val item = CustomDebtItem(
            description = description.trim(),
            amount = amount.coerceAtLeast(0.0),
            categoryTag = tag
        )
        _customDebts.value = _customDebts.value + item
    }

    fun removeCustomDebt(id: String) {
        _customDebts.value = _customDebts.value.filterNot { it.id == id }
    }

    // Step 4 Nisab Standard
    var nisabStandard = MutableStateFlow("Silver") // Silver or Gold

    // Step 5 Vault Label
    var vaultLabel = MutableStateFlow("Ramadan 2026 / Business Vault")
    private val _isSavedToVault = MutableStateFlow(false)
    val isSavedToVault: StateFlow<Boolean> = _isSavedToVault.asStateFlow()

    // 5. Hawl Anniversary & Precise Date Tracking
    var annualRamadanReminder = MutableStateFlow(true)
    var reminderCadence = MutableStateFlow("ramadan") // "ramadan" or "hawl"
    var hawlStartDateMillis = MutableStateFlow(System.currentTimeMillis() - 86400000L * 310) // ~310 days ago default

    private val _selectedHijriMilestone = MutableStateFlow(HijriMilestone.RAMADAN_FIRST)
    val selectedHijriMilestone: StateFlow<HijriMilestone> = _selectedHijriMilestone.asStateFlow()

    private val _selectedHawlRuling = MutableStateFlow(HawlContinuityRuling.HANAFI_START_END)
    val selectedHawlRuling: StateFlow<HawlContinuityRuling> = _selectedHawlRuling.asStateFlow()

    private val _wealthDippedBelowNisab = MutableStateFlow(false)
    val wealthDippedBelowNisab: StateFlow<Boolean> = _wealthDippedBelowNisab.asStateFlow()

    val hawlCycleState: StateFlow<HawlCycleState> = combine(
        hawlStartDateMillis,
        _selectedHijriMilestone,
        _selectedHawlRuling,
        _wealthDippedBelowNisab
    ) { start, milestone, ruling, dipped ->
        HawlCycleState(
            startDateMillis = start,
            selectedMilestone = milestone,
            selectedRuling = ruling,
            didWealthDropBelowNisab = dipped
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        HawlCycleState(hawlStartDateMillis.value)
    )

    fun setHijriMilestone(milestone: HijriMilestone) {
        _selectedHijriMilestone.value = milestone
    }

    fun setHawlRuling(ruling: HawlContinuityRuling) {
        _selectedHawlRuling.value = ruling
    }

    fun setWealthDippedBelowNisab(dipped: Boolean) {
        _wealthDippedBelowNisab.value = dipped
    }

    fun resetHawlToNow() {
        hawlStartDateMillis.value = System.currentTimeMillis()
        _wealthDippedBelowNisab.value = false
        showToast("Hawl cycle restarted from today.")
    }

    fun getHawlDaysElapsed(): Long {
        val diff = System.currentTimeMillis() - hawlStartDateMillis.value
        return (diff / 86400000L).coerceAtLeast(0L)
    }

    fun getHawlDaysRemaining(): Long {
        val elapsed = getHawlDaysElapsed()
        val remaining = 354L - (elapsed % 354L)
        return remaining.coerceIn(0L, 354L)
    }

    fun setHawlStartDate(millis: Long) {
        hawlStartDateMillis.value = millis
    }

    // 6. 8 Quranic Asnaf Distribution Planner
    private val _asnafAllocations = MutableStateFlow<Map<String, AsnafAllocation>>(
        AsnafCategory.entries.associate { it.id to AsnafAllocation(category = it, allocatedAmount = 0.0) }
    )
    val asnafAllocations: StateFlow<Map<String, AsnafAllocation>> = _asnafAllocations.asStateFlow()

    fun setAsnafAllocation(categoryId: String, amount: Double, note: String? = null) {
        val current = _asnafAllocations.value.toMutableMap()
        val cat = AsnafCategory.fromId(categoryId)
        val existing = current[cat.id] ?: AsnafAllocation(category = cat)
        current[cat.id] = existing.copy(
            allocatedAmount = amount.coerceAtLeast(0.0),
            beneficiaryNote = note ?: existing.beneficiaryNote
        )
        _asnafAllocations.value = current
    }

    fun autoDistributeAsnafRecommended() {
        val totalDue = getZakatDue()
        if (totalDue <= 0.0) {
            showToast("Enter your assets first to calculate Zakat before distributing.")
            return
        }
        val current = _asnafAllocations.value.toMutableMap()
        AsnafCategory.entries.forEach { cat ->
            val amount = totalDue * cat.recommendedSharePct
            current[cat.id] = AsnafAllocation(category = cat, allocatedAmount = amount)
        }
        _asnafAllocations.value = current
        showToast("Distributed across 8 Asnaf according to prophetic community ratios.")
    }

    fun resetAsnafDistribution() {
        _asnafAllocations.value = AsnafCategory.entries.associate {
            it.id to AsnafAllocation(category = it, allocatedAmount = 0.0)
        }
    }

    fun getAsnafDistributionPlan(): AsnafDistributionPlan {
        val due = getZakatDue()
        val curr = _selectedCurrency.value
        return AsnafDistributionPlan(
            totalZakatDue = due,
            currencySymbol = curr.symbol,
            currencyCode = curr.code,
            allocations = _asnafAllocations.value
        )
    }

    fun generateAsnafDistributionShareText(): String {
        val plan = getAsnafDistributionPlan()
        return plan.formattedSummary()
    }

    // 7. Karat Rates Matrix & 30-Day Market Trend
    fun getKaratMatrix(): List<KaratRateItem> {
        val gold24k = getGoldRate()
        return KaratRateCalculator.calculateMatrix(gold24k, _selectedCurrency.value.symbol)
    }

    fun getCalculatedRateForKarat(karat: Karat): Double {
        return karat.getRatePerGram(getGoldRate())
    }

    fun get30DayNisabTrend(): List<NisabTrendPoint> {
        return NisabTrendGenerator.generate30DayTrend(getGoldRate(), getSilverRate())
    }

    // Dialogs & Toasts
    private val _simulatedToast = MutableStateFlow<String?>(null)
    val simulatedToast: StateFlow<String?> = _simulatedToast.asStateFlow()

    fun showToast(msg: String) {
        _simulatedToast.value = msg
    }

    var showAsnafDialog = MutableStateFlow(false)
    var showPrivacyDialog = MutableStateFlow(false)
    var showCurrencyDialog = MutableStateFlow(false)
    var showProfileDialog = MutableStateFlow(false)
    var showNisabVerificationDialog = MutableStateFlow(false)
    var nisabDialogInitialTab = MutableStateFlow(0)
    var showHawlTrackerDialog = MutableStateFlow(false)
    var showSettingsDialog = MutableStateFlow(false)

    fun openNisabVerificationDialog(initialTab: Int = 0) {
        nisabDialogInitialTab.value = initialTab
        showNisabVerificationDialog.value = true
    }

    // Active record for statement view
    private val _activeStatementRecord = MutableStateFlow<ZakatRecord?>(null)
    val activeStatementRecord: StateFlow<ZakatRecord?> = _activeStatementRecord.asStateFlow()

    // Asset Groups (Hotlinked Images & Sovereign Asset Vault)
    val assetGroups: StateFlow<List<AssetGroup>> = (assetGroupRepository?.allAssetGroups ?: emptyFlow())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val assetGroupSearchQuery = MutableStateFlow("")
    val selectedAssetCategory = MutableStateFlow(AssetGroupCategories.ALL)

    val filteredAssetGroups: StateFlow<List<AssetGroup>> = combine(
        assetGroups,
        assetGroupSearchQuery,
        selectedAssetCategory
    ) { groups, query, category ->
        groups.filter { group ->
            val matchesCategory = (category == AssetGroupCategories.ALL || group.category.equals(category, ignoreCase = true))
            val matchesQuery = query.isBlank() ||
                group.name.contains(query, ignoreCase = true) ||
                group.description.contains(query, ignoreCase = true) ||
                group.tags.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeAssetGroup = MutableStateFlow<AssetGroup?>(null)
    val showCreateAssetGroupDialog = MutableStateFlow(false)
    val showAddImageToGroupId = MutableStateFlow<Long?>(null)
    val previewHotlinkUrl = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            repository.seedSampleIfEmpty()
            assetGroupRepository?.seedDefaultGroupsIfEmpty()
        }
        startPeriodicRateSync()
    }

    // Asset Group Actions
    fun createAssetGroup(
        name: String,
        description: String,
        category: String,
        imageUrls: List<String>,
        tags: String = ""
    ) {
        val trimmedName = name.trim()
        if (trimmedName.isBlank()) return
        val validUrls = imageUrls.map { it.trim() }.filter { it.isNotBlank() }
        val newGroup = AssetGroup(
            name = trimmedName,
            description = description.trim(),
            category = category,
            imageUrls = validUrls,
            coverImageUrl = validUrls.firstOrNull() ?: "",
            tags = tags.trim()
        )
        viewModelScope.launch {
            assetGroupRepository?.insert(newGroup)
            showCreateAssetGroupDialog.value = false
        }
    }

    fun updateAssetGroup(group: AssetGroup) {
        viewModelScope.launch {
            assetGroupRepository?.update(group)
            if (activeAssetGroup.value?.id == group.id) {
                activeAssetGroup.value = group
            }
        }
    }

    fun deleteAssetGroup(group: AssetGroup) {
        viewModelScope.launch {
            assetGroupRepository?.delete(group)
            if (activeAssetGroup.value?.id == group.id) {
                activeAssetGroup.value = null
            }
        }
    }

    fun deleteAssetGroupById(id: Long) {
        viewModelScope.launch {
            assetGroupRepository?.deleteById(id)
            if (activeAssetGroup.value?.id == id) {
                activeAssetGroup.value = null
            }
        }
    }

    fun addHotlinkedImageToGroup(groupId: Long, url: String) {
        val trimmed = url.trim()
        if (trimmed.isBlank()) return
        viewModelScope.launch {
            assetGroupRepository?.addImageUrl(groupId, trimmed)
            val updated = assetGroupRepository?.getById(groupId)
            if (activeAssetGroup.value?.id == groupId) {
                activeAssetGroup.value = updated
            }
            showAddImageToGroupId.value = null
        }
    }

    fun removeHotlinkedImageFromGroup(groupId: Long, url: String) {
        viewModelScope.launch {
            assetGroupRepository?.removeImageUrl(groupId, url)
            val updated = assetGroupRepository?.getById(groupId)
            if (activeAssetGroup.value?.id == groupId) {
                activeAssetGroup.value = updated
            }
        }
    }

    fun setGroupCoverImage(groupId: Long, url: String) {
        viewModelScope.launch {
            assetGroupRepository?.setCoverImageUrl(groupId, url)
            val updated = assetGroupRepository?.getById(groupId)
            if (activeAssetGroup.value?.id == groupId) {
                activeAssetGroup.value = updated
            }
        }
    }

    // Pure Grams Calculations
    fun getPureGoldGrams(): Double {
        val input = goldWeightInput.value.trim().toDoubleOrNull()?.takeIf { it >= 0.0 } ?: 0.0
        val gramsTotal = input * selectedWeightUnit.value.toGramsMultiplier
        return gramsTotal * selectedGoldKarat.value.purityRatio
    }

    fun getSilverGramsInStandard(): Double {
        val input = silverWeightInput.value.trim().toDoubleOrNull()?.takeIf { it >= 0.0 } ?: 0.0
        return input * selectedWeightUnit.value.toGramsMultiplier
    }

    // Valuations
    fun getLiquidTotal(): Double {
        val c = cashOnHand.value.trim().toDoubleOrNull()?.takeIf { it >= 0.0 } ?: 0.0
        val curr = bankCurrent.value.trim().toDoubleOrNull()?.takeIf { it >= 0.0 } ?: 0.0
        val sav = bankSavings.value.trim().toDoubleOrNull()?.takeIf { it >= 0.0 } ?: 0.0
        return c + curr + sav
    }

    fun getGoldValue(): Double {
        if (exemptPersonalJewelry.value) {
            return 0.0
        }
        return getPureGoldGrams() * getGoldRate()
    }

    fun getSilverValue(): Double {
        return getSilverGramsInStandard() * getSilverRate()
    }

    fun getMetalsTotal(): Double {
        return getGoldValue() + getSilverValue()
    }

    fun getInvestmentsTotal(): Double {
        val s = stocksEquities.value.trim().toDoubleOrNull()?.takeIf { it >= 0.0 } ?: 0.0
        val m = mutualFunds.value.trim().toDoubleOrNull()?.takeIf { it >= 0.0 } ?: 0.0
        return s + m
    }

    fun getBusinessTotal(): Double {
        val c = businessCash.value.trim().toDoubleOrNull()?.takeIf { it >= 0.0 } ?: 0.0
        val i = businessInventory.value.trim().toDoubleOrNull()?.takeIf { it >= 0.0 } ?: 0.0
        return c + i
    }

    fun getCustomAssetsTotal(): Double {
        return _customAssets.value.sumOf { it.zakatableValue }
    }

    fun getQualifyingReceivables(): Double {
        return if (receivablesRecoverability.value == "confirmed") {
            moneyOwed.value.trim().toDoubleOrNull()?.takeIf { it >= 0.0 } ?: 0.0
        } else {
            0.0
        }
    }

    fun getStandardDeductibleDebts(): Double {
        return deductibleDebts.value.trim().toDoubleOrNull()?.takeIf { it >= 0.0 } ?: 0.0
    }

    fun getCustomDebtsTotal(): Double {
        return _customDebts.value.sumOf { it.amount }
    }

    fun getTotalDeductibleDebts(): Double {
        return getStandardDeductibleDebts() + getCustomDebtsTotal()
    }

    fun getDeductibleDebtsValue(): Double = getTotalDeductibleDebts()

    fun getGrossAssets(): Double {
        return getLiquidTotal() + getMetalsTotal() + getInvestmentsTotal() + getBusinessTotal() + getCustomAssetsTotal() + getQualifyingReceivables()
    }

    fun getNetWealth(): Double {
        val gross = getGrossAssets()
        val debts = getTotalDeductibleDebts()
        return (gross - debts).coerceAtLeast(0.0)
    }

    fun calculateTotalNetWealth(): Double = getNetWealth()

    fun getNisabFloor(): Double {
        val curr = _selectedCurrency.value
        return if (nisabStandard.value == "Silver") {
            curr.getSilverNisabFloor(getSilverRate())
        } else {
            curr.getGoldNisabFloor(getGoldRate())
        }
    }

    fun getSilverNisabFloor(): Double = _selectedCurrency.value.getSilverNisabFloor(getSilverRate())
    fun getGoldNisabFloor(): Double = _selectedCurrency.value.getGoldNisabFloor(getGoldRate())

    fun setNisabStandard(standard: String) {
        nisabStandard.value = standard
    }

    fun resetCalculation() {
        cashOnHand.value = ""
        bankCurrent.value = ""
        bankSavings.value = ""
        goldWeightInput.value = ""
        silverWeightInput.value = ""
        stocksEquities.value = ""
        mutualFunds.value = ""
        businessCash.value = ""
        businessInventory.value = ""
        moneyOwed.value = ""
        debtorName.value = ""
        deductibleDebts.value = ""
        debtType.value = ""
        _customAssets.value = emptyList()
        _customDebts.value = emptyList()
        _isSavedToVault.value = false
        vaultLabel.value = ""
        setCalcStep(1)
    }

    fun isNisabMet(): Boolean {
        return getNetWealth() >= getNisabFloor()
    }

    fun getNisabMultiplier(): Double {
        val floor = getNisabFloor()
        if (floor <= 0) return 1.0
        return (getNetWealth() / floor)
    }

    fun getZakatDue(): Double {
        return if (isNisabMet()) {
            getNetWealth() * 0.025
        } else {
            0.0
        }
    }

    fun getNisabEvaluation(): NisabEvaluation {
        val rates = _groundedRates.value
        val netWealth = getNetWealth()
        val standard = nisabStandard.value
        val curr = _selectedCurrency.value

        if (rates != null) {
            return rates.evaluateNisab(netWealth, standard, curr.symbol)
        }

        val goldRate = getGoldRate()
        val silverRate = getSilverRate()
        val goldThreshold = curr.getGoldNisabFloor(goldRate)
        val silverThreshold = curr.getSilverNisabFloor(silverRate)
        val activeThreshold = if (standard.equals("Gold", ignoreCase = true)) goldThreshold else silverThreshold
        val isMet = activeThreshold > 0.0 && netWealth >= activeThreshold
        val pct = if (activeThreshold > 0.0) netWealth / activeThreshold else 0.0
        val due = if (isMet) netWealth * 0.025 else 0.0

        return NisabEvaluation(
            goldPerGram = goldRate,
            silverPerGram = silverRate,
            goldNisabThreshold = goldThreshold,
            silverNisabThreshold = silverThreshold,
            selectedStandard = standard,
            activeThreshold = activeThreshold,
            userNetWealth = netWealth,
            isNisabMet = isMet,
            nisabPercentage = pct,
            surplusOrShortfall = netWealth - activeThreshold,
            zakatDue = due,
            currencyCode = curr.code,
            currencySymbol = curr.symbol,
            asOfText = "Live Market Benchmark",
            isGrounded = false
        )
    }

    // Vault Save
    fun saveCurrentCalculation() {
        viewModelScope.launch {
            val net = getNetWealth()
            val due = getZakatDue()
            val ref = "ZC-${SimpleDateFormat("yyyy", Locale.US).format(Date())}-${(100000..999999).random()}"
            val curr = _selectedCurrency.value
            val record = ZakatRecord(
                refNumber = ref,
                timestamp = System.currentTimeMillis(),
                hijriYear = "1448 AH Hawl",
                label = vaultLabel.value.ifBlank { "${_activeProfile.value.name} Zakat" },
                cashOnHand = cashOnHand.value.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0,
                bankCurrent = bankCurrent.value.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0,
                bankSavings = bankSavings.value.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0,
                goldGrams = getPureGoldGrams(),
                goldRate = getGoldRate(),
                silverGrams = getSilverGramsInStandard(),
                silverRate = getSilverRate(),
                stocksEquities = stocksEquities.value.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0,
                mutualFunds = mutualFunds.value.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0,
                businessCash = businessCash.value.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0,
                businessInventory = businessInventory.value.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0,
                moneyOwed = moneyOwed.value.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0,
                receivablesRecoverable = receivablesRecoverability.value == "confirmed",
                debtorName = debtorName.value,
                deductibleDebts = getTotalDeductibleDebts().coerceAtLeast(0.0),
                debtDescription = debtType.value,
                nisabStandard = nisabStandard.value,
                grossAssets = getGrossAssets(),
                deductions = getTotalDeductibleDebts(),
                netWealth = net,
                zakatDue = due,
                hashDigest = ZakatRepository.generateHash(ref, net, due),
                currencyCode = curr.code,
                currencySymbol = curr.symbol,
                profileName = _activeProfile.value.name,
                madhab = _selectedMadhab.value.title,
                goldKarat = selectedGoldKarat.value.label,
                weightUnit = selectedWeightUnit.value.label,
                jewelryExemptApplied = exemptPersonalJewelry.value,
                customAssetsTotal = getCustomAssetsTotal(),
                customDebtsTotal = getCustomDebtsTotal(),
                hawlDateMillis = hawlStartDateMillis.value
            )
            repository.insertRecord(record)
            _isSavedToVault.value = true
            _activeStatementRecord.value = record
        }
    }

    fun prepareCurrentStatement() {
        val net = getNetWealth()
        val due = getZakatDue()
        val curr = _selectedCurrency.value
        val ref = "ZC-${SimpleDateFormat("yyyy", Locale.US).format(Date())}-STATEMENT"
        val record = ZakatRecord(
            refNumber = ref,
            timestamp = System.currentTimeMillis(),
            hijriYear = "1448 AH Hawl",
            label = vaultLabel.value,
            cashOnHand = cashOnHand.value.toDoubleOrNull() ?: 0.0,
            bankCurrent = bankCurrent.value.toDoubleOrNull() ?: 0.0,
            bankSavings = bankSavings.value.toDoubleOrNull() ?: 0.0,
            goldGrams = getPureGoldGrams(),
            goldRate = getGoldRate(),
            silverGrams = getSilverGramsInStandard(),
            silverRate = getSilverRate(),
            stocksEquities = stocksEquities.value.toDoubleOrNull() ?: 0.0,
            mutualFunds = mutualFunds.value.toDoubleOrNull() ?: 0.0,
            businessCash = businessCash.value.toDoubleOrNull() ?: 0.0,
            businessInventory = businessInventory.value.toDoubleOrNull() ?: 0.0,
            moneyOwed = moneyOwed.value.toDoubleOrNull() ?: 0.0,
            receivablesRecoverable = receivablesRecoverability.value == "confirmed",
            debtorName = debtorName.value,
            deductibleDebts = getTotalDeductibleDebts(),
            debtDescription = debtType.value,
            nisabStandard = nisabStandard.value,
            grossAssets = getGrossAssets(),
            deductions = getTotalDeductibleDebts(),
            netWealth = net,
            zakatDue = due,
            hashDigest = ZakatRepository.generateHash(ref, net, due),
            currencyCode = curr.code,
            currencySymbol = curr.symbol,
            profileName = _activeProfile.value.name,
            madhab = _selectedMadhab.value.title,
            goldKarat = selectedGoldKarat.value.label,
            weightUnit = selectedWeightUnit.value.label,
            jewelryExemptApplied = exemptPersonalJewelry.value,
            customAssetsTotal = getCustomAssetsTotal(),
            customDebtsTotal = getCustomDebtsTotal(),
            hawlDateMillis = hawlStartDateMillis.value
        )
        _activeStatementRecord.value = record
        _calcStep.value = 6
    }

    fun viewStatementForRecord(record: ZakatRecord) {
        _activeStatementRecord.value = record
        _calcStep.value = 6
        _currentTab.value = AppTab.CALCULATION
    }

    fun deleteRecord(record: ZakatRecord) {
        viewModelScope.launch {
            repository.deleteRecord(record)
        }
    }

    fun importRecords(records: List<ZakatRecord>) {
        viewModelScope.launch {
            repository.insertRecords(records)
        }
    }

    // Portability & Backup
    fun exportJsonBackup(): String {
        return PortabilityUtils.exportRecordsToJson(savedRecords.value)
    }

    fun exportCsvBackup(): String {
        return PortabilityUtils.exportRecordsToCsv(savedRecords.value)
    }

    fun importJsonBackup(jsonString: String, onComplete: (Int) -> Unit) {
        viewModelScope.launch {
            val records = PortabilityUtils.importRecordsFromJson(jsonString)
            if (records.isNotEmpty()) {
                repository.insertRecords(records)
            }
            onComplete(records.size)
        }
    }

    fun generateStatementShareText(record: ZakatRecord?): String {
        val rec = record ?: _activeStatementRecord.value ?: return "No record"
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.US)
        val dateStr = sdf.format(Date(rec.timestamp))
        return """
            ==============================================
            ☪ ZAKAT COMPANION • OFFICIAL STATEMENT
            ==============================================
            Reference ID : ${rec.refNumber}
            Audit Date   : $dateStr
            Hijri Hawl   : ${rec.hijriYear}
            Portfolio    : ${rec.profileName}
            Jurisprudence: Classical Fiqh Consensus
            ----------------------------------------------
            Gross Zakatable Assets : ${rec.currencySymbol} ${formatNumber(rec.grossAssets)}
            Immediate Liabilities  : ${rec.currencySymbol} ${formatNumber(rec.deductions)}
            Net Zakatable Wealth   : ${rec.currencySymbol} ${formatNumber(rec.netWealth)}
            Nisab Standard Applied : ${rec.nisabStandard}
            
            TOTAL ZAKAT DUE (2.5%) : ${rec.currencySymbol} ${formatNumber(rec.zakatDue)}
            ----------------------------------------------
            SHA-256 Audit Seal:
            ${rec.hashDigest}
            
            Calculated offline with sovereign client-side privacy.
            ==============================================
        """.trimIndent()
    }

    // Reminders
    fun triggerSimulateReminder() {
        _simulatedToast.value = "Hawl Milestone Complete: Peace be upon you. Your Zakat balance for ${_activeProfile.value.name} is ready for review."
    }

    fun clearSimulateToast() {
        _simulatedToast.value = null
    }

    // Currency Formatting
    fun formatCurrency(amount: Double): String {
        val formatter = NumberFormat.getNumberInstance(Locale.US)
        formatter.maximumFractionDigits = 0
        return "${_selectedCurrency.value.symbol} " + formatter.format(amount)
    }

    fun formatCustomCurrency(amount: Double, symbol: String): String {
        val formatter = NumberFormat.getNumberInstance(Locale.US)
        formatter.maximumFractionDigits = 0
        return "$symbol " + formatter.format(amount)
    }

    fun formatNumber(amount: Double): String {
        val formatter = NumberFormat.getNumberInstance(Locale.US)
        formatter.maximumFractionDigits = 0
        return formatter.format(amount)
    }
}

class ZakatViewModelFactory(
    private val repository: ZakatRepository,
    private val assetGroupRepository: AssetGroupRepository? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ZakatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ZakatViewModel(repository, assetGroupRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
