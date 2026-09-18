package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.ZakatDatabase
import com.example.data.repository.AssetGroupRepository
import com.example.data.repository.ZakatRepository
import com.example.ui.AppFlowState
import com.example.ui.AppTab
import com.example.ui.ZakatViewModel
import com.example.ui.ZakatViewModelFactory
import com.example.ui.components.AsnafModalDialog
import com.example.ui.components.PrivacyInfoDialog
import com.example.ui.components.SettingsDialog
import com.example.ui.components.ZakatBottomNavBar
import com.example.ui.components.ZakatTopHeader
import com.example.ui.screens.AssetGroupsScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.CalculationFlowScreen
import com.example.ui.screens.GuidanceScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LanguageSetupScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = ZakatDatabase.getDatabase(applicationContext)
        val repository = ZakatRepository(database.zakatDao())
        val assetGroupRepository = AssetGroupRepository(database.assetGroupDao())
        val factory = ZakatViewModelFactory(repository, assetGroupRepository)

        setContent {
            val zakatViewModel: ZakatViewModel = viewModel(factory = factory)
            val isDarkMode by zakatViewModel.isDarkMode.collectAsState()

            MyApplicationTheme(darkTheme = isDarkMode) {
                ZakatCompanionApp(viewModel = zakatViewModel)
            }
        }
    }
}

@Composable
fun ZakatCompanionApp(viewModel: ZakatViewModel) {
    val flowState by viewModel.flowState.collectAsState()

    when (flowState) {
        AppFlowState.AUTH -> {
            AuthScreen(viewModel = viewModel)
        }
        AppFlowState.LANGUAGE_SETUP -> {
            LanguageSetupScreen(viewModel = viewModel)
        }
        AppFlowState.MAIN_APP -> {
            MainAppScaffold(viewModel = viewModel)
        }
    }
}

@Composable
fun MainAppScaffold(viewModel: ZakatViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val calcStep by viewModel.calcStep.collectAsState()
    val showAsnaf by viewModel.showAsnafDialog.collectAsState()
    val showPrivacy by viewModel.showPrivacyDialog.collectAsState()
    val showCurrency by viewModel.showCurrencyDialog.collectAsState()
    val showProfile by viewModel.showProfileDialog.collectAsState()
    val showNisabVerification by viewModel.showNisabVerificationDialog.collectAsState()
    val nisabInitialTab by viewModel.nisabDialogInitialTab.collectAsState()
    val showHawlTracker by viewModel.showHawlTrackerDialog.collectAsState()
    val showSettings by viewModel.showSettingsDialog.collectAsState()

    val activeProfile by viewModel.activeProfile.collectAsState()
    val profiles by viewModel.profiles.collectAsState()
    val selectedCurrency by viewModel.selectedCurrency.collectAsState()

    val tabTitle = when (currentTab) {
        AppTab.HOME -> "Home"
        AppTab.CALCULATION -> "Calculation"
        AppTab.HISTORY -> "Ledger Vault"
        AppTab.GUIDANCE -> "Fiqh Guide"
        AppTab.ASSETS -> "Asset Vault"
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ZakatTopHeader(
                currentTabTitle = tabTitle,
                activeProfileName = activeProfile.name,
                activeCurrencyCode = selectedCurrency.code,
                activeCurrencySymbol = selectedCurrency.symbol,
                onProfileClick = { viewModel.showProfileDialog.value = true },
                onCurrencyClick = { viewModel.showCurrencyDialog.value = true },
                onPrivacyClick = { viewModel.showPrivacyDialog.value = true },
                onSettingsClick = { viewModel.showSettingsDialog.value = true }
            )
        },
        bottomBar = {
            ZakatBottomNavBar(
                activeTab = currentTab,
                onTabSelected = { tab -> viewModel.selectTab(tab) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 600.dp)
            ) {
                when (currentTab) {
                    AppTab.HOME -> {
                        HomeScreen(
                            viewModel = viewModel,
                            onNavigateToCalculation = {
                                viewModel.setCalcStep(1)
                                viewModel.selectTab(AppTab.CALCULATION)
                            },
                            onNavigateToHistory = {
                                viewModel.selectTab(AppTab.HISTORY)
                            },
                            onNavigateToGuidance = {
                                viewModel.selectTab(AppTab.GUIDANCE)
                            }
                        )
                    }
                    AppTab.CALCULATION -> {
                        CalculationFlowScreen(
                            viewModel = viewModel,
                            onNavigateToHistory = {
                                viewModel.selectTab(AppTab.HISTORY)
                            },
                            onOpenAsnafGuide = {
                                viewModel.showAsnafDialog.value = true
                            }
                        )
                    }
                    AppTab.HISTORY -> {
                        HistoryScreen(
                            viewModel = viewModel,
                            onStartNewCalculation = {
                                viewModel.setCalcStep(1)
                                viewModel.selectTab(AppTab.CALCULATION)
                            }
                        )
                    }
                    AppTab.GUIDANCE -> {
                        GuidanceScreen(
                            viewModel = viewModel,
                            onOpenAsnafGuide = {
                                viewModel.showAsnafDialog.value = true
                            }
                        )
                    }
                    AppTab.ASSETS -> {
                        AssetGroupsScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }

    if (showAsnaf) {
        AsnafModalDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.showAsnafDialog.value = false }
        )
    }

    if (showHawlTracker) {
        com.example.ui.components.HawlTrackerDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.showHawlTrackerDialog.value = false }
        )
    }

    if (showPrivacy) {
        PrivacyInfoDialog(onDismiss = { viewModel.showPrivacyDialog.value = false })
    }

    if (showCurrency) {
        com.example.ui.components.CurrencySelectorDialog(
            selectedCurrency = selectedCurrency,
            onSelectCurrency = { curr -> viewModel.selectCurrency(curr) },
            onDismiss = { viewModel.showCurrencyDialog.value = false }
        )
    }

    if (showProfile) {
        com.example.ui.components.ProfileSelectorDialog(
            profiles = profiles,
            activeProfile = activeProfile,
            onSelectProfile = { prof -> viewModel.selectProfile(prof) },
            onAddProfile = { name -> viewModel.addCustomProfile(name) },
            onDismiss = { viewModel.showProfileDialog.value = false },
            onLogout = { viewModel.logout() }
        )
    }

    if (showNisabVerification) {
        com.example.ui.components.NisabGroundingDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.showNisabVerificationDialog.value = false },
            initialTab = nisabInitialTab
        )
    }

    if (showSettings) {
        SettingsDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.showSettingsDialog.value = false }
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

