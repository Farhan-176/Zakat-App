package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCardOff
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import java.util.Locale
import androidx.compose.material3.HorizontalDivider
import com.example.data.model.Karat
import com.example.data.model.WeightUnit
import com.example.ui.AppTab
import com.example.ui.ZakatViewModel
import com.example.ui.components.ZakatBrandLogo
import com.example.ui.components.PdfExportDialog
import com.example.util.PortabilityUtils
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import com.example.ui.theme.EmeraldOnPrimary
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldPrimaryContainer
import com.example.ui.theme.EmeraldPrimaryFixed
import com.example.ui.theme.EmeraldPrimaryFixedDim
import com.example.ui.theme.ErrorContainerRed
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GoldOnSecondaryContainer
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.GoldSecondaryContainer
import com.example.ui.theme.GoldSecondaryFixed
import com.example.ui.theme.GoldSecondaryFixedDim
import com.example.ui.theme.OnSurfaceDark
import com.example.ui.theme.OnSurfaceVariantMuted
import com.example.ui.theme.OutlineBorder
import com.example.ui.theme.SurfaceContainerDefault
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerHighest
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.SurfaceMint

@Composable
fun CalculationFlowScreen(
    viewModel: ZakatViewModel,
    onNavigateToHistory: () -> Unit,
    onOpenAsnafGuide: () -> Unit
) {
    val step by viewModel.calcStep.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceMint)
    ) {
        CalculationFlowTopStepper(
            currentStep = step,
            totalSteps = 6,
            onBack = {
                if (step > 1) {
                    viewModel.setCalcStep(step - 1)
                } else {
                    viewModel.selectTab(AppTab.HOME)
                }
            },
            onStepClick = { targetStep ->
                if (targetStep < step) {
                    viewModel.setCalcStep(targetStep)
                }
            }
        )

        Box(modifier = Modifier.weight(1f)) {
            when (step) {
                1 -> Step1CashAndSavings(viewModel)
                2 -> Step2MetalsAndBusiness(viewModel)
                3 -> Step3ReceivablesAndDebts(viewModel)
                4 -> Step4ReviewAndNisab(viewModel)
                5 -> Step5Summary(viewModel, onOpenAsnafGuide)
                6 -> Step6OfficialStatement(viewModel, onNavigateToHistory)
            }
        }
    }
}

@Composable
private fun CalculationFlowTopStepper(
    currentStep: Int,
    totalSteps: Int = 6,
    onBack: () -> Unit,
    onStepClick: (Int) -> Unit
) {
    val stepTitles = listOf(
        "Cash & Liquid Savings",
        "Precious Metals & Assets",
        "Debts & Deductions",
        "Nisab Verification",
        "Calculation Summary",
        "Official Statement"
    )
    val currentTitle = stepTitles.getOrElse(currentStep - 1) { "Calculation" }

    Surface(
        color = SurfaceContainerLowest,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = SurfaceContainerDefault,
                    modifier = Modifier
                        .size(34.dp)
                        .clickable(onClick = onBack)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "STEP $currentStep OF $totalSteps",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldSecondary,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = currentTitle,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        ),
                        maxLines = 1
                    )
                }
            }

            // 6-segment smooth stepper bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                for (i in 1..totalSteps) {
                    val isPast = i < currentStep
                    val isCurrent = i == currentStep
                    val color = when {
                        isCurrent -> EmeraldPrimary
                        isPast -> EmeraldPrimary.copy(alpha = 0.5f)
                        else -> SurfaceContainerHigh
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(100.dp))
                            .background(color)
                            .clickable(enabled = isPast) { onStepClick(i) }
                    )
                }
            }
        }
    }
}

// ==========================================
// STEP 1: CASH & SAVINGS
// ==========================================
@Composable
private fun Step1CashAndSavings(viewModel: ZakatViewModel) {
    val cashOnHand by viewModel.cashOnHand.collectAsState()
    val bankCurrent by viewModel.bankCurrent.collectAsState()
    val bankSavings by viewModel.bankSavings.collectAsState()
    val selectedCurrency by viewModel.selectedCurrency.collectAsState()

    val scrollState = rememberScrollState()
    val liquidTotal = viewModel.getLiquidTotal()
    val step1Errors = viewModel.getStep1Errors()
    val isStep1Valid = step1Errors.isEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceMint)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Real-time Error Summary Banner
        if (!isStep1Valid) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = ErrorContainerRed,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("step1_validation_warning")
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = ErrorRed,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Invalid Numerical Input Detected",
                            color = ErrorRed,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "All asset fields must contain valid, non-negative numerical amounts (≥ 0). Please correct the following highlighted fields:",
                        color = OnSurfaceDark,
                        fontSize = 11.5.sp,
                        lineHeight = 16.sp
                    )
                    step1Errors.forEach { (field, error) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(start = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(ErrorRed)
                            )
                            Text(
                                text = "$field: $error",
                                color = ErrorRed,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Live Step Aggregate Banner
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = EmeraldPrimaryContainer,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "LIQUID ASSETS SUB-TOTAL",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimaryFixed,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = viewModel.formatCurrency(liquidTotal),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Fiqh Guidance Callout
        GuidanceCalloutCard(
            title = "Fiqh Guidance · Liquid Cash",
            text = "Include cash in hand, and balances in current and savings bank accounts as of today. Zakat applies if held for one lunar year (Hawl)."
        )

        // Inputs
        FinancialInputField(
            label = "Cash on Hand",
            value = cashOnHand,
            onValueChange = { viewModel.cashOnHand.value = it },
            icon = Icons.Default.Wallet,
            helperText = "Physical cash currently held",
            placeholder = "e.g. 15,000",
            testTag = "input_cash_on_hand",
            currencyCode = selectedCurrency.code,
            currencySymbol = selectedCurrency.symbol
        )

        FinancialInputField(
            label = "Bank Savings — Current Account",
            value = bankCurrent,
            onValueChange = { viewModel.bankCurrent.value = it },
            icon = Icons.Default.AccountBalance,
            helperText = "Checking and salary account active balances",
            placeholder = "e.g. 80,000",
            testTag = "input_bank_current",
            currencyCode = selectedCurrency.code,
            currencySymbol = selectedCurrency.symbol
        )

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            FinancialInputField(
                label = "Bank Savings — Savings Account",
                value = bankSavings,
                onValueChange = { viewModel.bankSavings.value = it },
                icon = Icons.Default.AccountBalanceWallet,
                helperText = "Savings bank deposit principal",
                placeholder = "e.g. 120,000",
                testTag = "input_bank_savings",
                currencyCode = selectedCurrency.code,
                currencySymbol = selectedCurrency.symbol
            )

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = SurfaceContainerHigh,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = GoldSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Exclude any earned interest (Riba); only principal balance is subject to Zakat.",
                        fontSize = 11.sp,
                        color = GoldOnSecondaryContainer,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        // Navigation CTA
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { viewModel.resetCalculation() },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SurfaceContainerDefault,
                    contentColor = OnSurfaceVariantMuted
                )
            ) {
                Text("Clear", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }

            Button(
                onClick = {
                    if (isStep1Valid) {
                        viewModel.setCalcStep(2)
                    }
                },
                enabled = isStep1Valid,
                modifier = Modifier
                    .weight(2.5f)
                    .height(48.dp)
                    .testTag("continue_to_step2_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isStep1Valid) EmeraldPrimary else OutlineBorder.copy(alpha = 0.5f),
                    contentColor = Color.White,
                    disabledContainerColor = SurfaceContainerHigh,
                    disabledContentColor = OnSurfaceVariantMuted
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isStep1Valid) "Next: Precious Metals" else "Resolve Errors to Proceed",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isStep1Valid) Color.White else OnSurfaceVariantMuted,
                            fontSize = 13.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (isStep1Valid) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

// ==========================================
// STEP 2: PRECIOUS METALS & BUSINESS
// ==========================================
@Composable
private fun Step2MetalsAndBusiness(viewModel: ZakatViewModel) {
    val goldGrams by viewModel.goldGrams.collectAsState()
    val silverGrams by viewModel.silverGrams.collectAsState()
    val businessCash by viewModel.businessCash.collectAsState()
    val businessInventory by viewModel.businessInventory.collectAsState()
    val stocksEquities by viewModel.stocksEquities.collectAsState()
    val mutualFunds by viewModel.mutualFunds.collectAsState()

    val goldKarat by viewModel.selectedGoldKarat.collectAsState()
    val weightUnit by viewModel.selectedWeightUnit.collectAsState()
    val exemptPersonalJewelry by viewModel.exemptPersonalJewelry.collectAsState()
    val selectedCurrency by viewModel.selectedCurrency.collectAsState()
    val customAssets by viewModel.customAssets.collectAsState()

    var showAddAssetDialog by remember { mutableStateOf(false) }
    var newAssetName by remember { mutableStateOf("") }
    var newAssetGrossValue by remember { mutableStateOf("") }
    var newAssetRatio by remember { mutableStateOf(1.0) }

    val scrollState = rememberScrollState()

    val goldVal = viewModel.getGoldValue()
    val silverVal = viewModel.getSilverValue()
    val metalsTotal = viewModel.getMetalsTotal()
    val bizTotal = viewModel.getBusinessTotal()
    val investmentsTotal = viewModel.getInvestmentsTotal()
    val customAssetsTotal = viewModel.getCustomAssetsTotal()
    val step2Total = metalsTotal + bizTotal + investmentsTotal + customAssetsTotal
    val estimatedZakat = step2Total * 0.025
    val step2Errors = viewModel.getStep2Errors()
    val isStep2Valid = step2Errors.isEmpty()

    val silverGramsNum = viewModel.getSilverGramsInStandard()
    val silverNisabProgress = (silverGramsNum / 612.36).coerceIn(0.0, 1.0).toFloat()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceMint)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Real-time Error Summary Banner
        if (!isStep2Valid) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = ErrorContainerRed,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("step2_top_validation_warning")
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = ErrorRed,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Invalid Numerical Input Detected",
                            color = ErrorRed,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "All asset and rate fields must be non-negative numerical values (≥ 0). Please correct the following highlighted inputs:",
                        color = OnSurfaceDark,
                        fontSize = 11.5.sp,
                        lineHeight = 16.sp
                    )
                    step2Errors.forEach { (field, error) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(start = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(ErrorRed)
                            )
                            Text(
                                text = "$field: $error",
                                color = ErrorRed,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Live Step Aggregate Banner
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = EmeraldPrimaryContainer,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "METALS & BUSINESS SUB-TOTAL",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimaryFixed,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = viewModel.formatCurrency(step2Total),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = null,
                        tint = GoldSecondaryFixed,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Google Search Grounded Market Rates Banner
        val groundedRates by viewModel.groundedRates.collectAsState()
        val rateSyncStatus by viewModel.rateSyncStatus.collectAsState()

        Surface(
            shape = RoundedCornerShape(14.dp),
            color = SurfaceContainerLowest,
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder.copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Google Search Grounded Spot Rates",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceDark
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(100.dp),
                        color = SurfaceMint,
                        modifier = Modifier
                            .clickable { viewModel.showNisabVerificationDialog.value = true }
                            .testTag("btn_step2_verify_nisab")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "Verify Nisab",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Gold 24K: ${selectedCurrency.symbol}${viewModel.getGoldRate().toInt()}/g • Silver: ${selectedCurrency.symbol}${viewModel.getSilverRate().toInt()}/g",
                        fontSize = 11.5.sp,
                        color = OnSurfaceVariantMuted
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Refresh",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary,
                            modifier = Modifier
                                .clickable { viewModel.fetchLiveGroundedRates(force = true) }
                                .testTag("btn_step2_refresh_rates")
                        )
                        Text(
                            text = "•",
                            fontSize = 11.sp,
                            color = OnSurfaceVariantMuted
                        )
                        Text(
                            text = "Apply Live Rates",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary,
                            modifier = Modifier
                                .clickable { viewModel.applyGroundedRatesToCalculator(notify = true) }
                                .testTag("btn_step2_apply_live_rates")
                        )
                    }
                }
            }
        }

        // Guidance Callout
        GuidanceCalloutCard(
            title = "Nisab Relevant Holdings",
            text = "Enter the weight of gold and silver you own in grams. Only gold above the Nisab-relevant threshold and silver held for savings purposes count toward your aggregate liability."
        )

        // 1. Gold Holdings Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceContainerLowest,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stars,
                            contentDescription = null,
                            tint = GoldSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "1. Gold Holdings",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceDark
                            )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(SurfaceContainerDefault)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(goldKarat.label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                    }
                }

                Text(
                    text = "Select purity (Karat) and weight unit.",
                    style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariantMuted)
                )

                // Karat Selector Chips
                Text("Gold Purity / Karat:", fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = OnSurfaceDark)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Karat.entries.forEach { karat ->
                        val isSel = karat == goldKarat
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSel) EmeraldPrimaryContainer else SurfaceContainerLow,
                            border = androidx.compose.foundation.BorderStroke(
                                width = if (isSel) 1.5.dp else 1.dp,
                                color = if (isSel) EmeraldPrimary else OutlineBorder.copy(alpha = 0.25f)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.selectedGoldKarat.value = karat }
                                .testTag("karat_${karat.name}")
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 2.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = karat.shortName,
                                    fontSize = 12.5.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.SemiBold,
                                    color = if (isSel) Color.White else OnSurfaceDark
                                )
                                Text(
                                    text = karat.purityPercent,
                                    fontSize = 9.5.sp,
                                    color = if (isSel) Color.White.copy(alpha = 0.85f) else OnSurfaceVariantMuted
                                )
                            }
                        }
                    }
                }

                // Live Karat Rate & Conversion Matrix Callout
                val currentMatrix = remember(viewModel.getGoldRate(), selectedCurrency.symbol) {
                    viewModel.getKaratMatrix()
                }
                val selectedKaratRate = currentMatrix.find { it.karat == goldKarat }
                if (selectedKaratRate != null) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = SurfaceMint,
                        border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.2f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.openNisabVerificationDialog(1) }
                            .testTag("btn_view_karat_matrix")
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Default.TrendingUp, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(15.dp))
                                    Text(
                                        text = "${goldKarat.shortName} Gold Valuation Rate",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldPrimary
                                    )
                                }
                                Text(
                                    text = "All Rates →",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary
                                )
                            }
                            Text(
                                text = "${selectedKaratRate.formattedPerGram}/gram • ${selectedKaratRate.formattedPerTola}/tola",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnSurfaceDark
                            )
                        }
                    }
                }

                // Weight Unit Chips
                Text("Weight Unit:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = OnSurfaceDark)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    WeightUnit.entries.forEach { unit ->
                        val isSel = unit == weightUnit
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSel) GoldSecondaryContainer else SurfaceContainerLow,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.selectedWeightUnit.value = unit }
                                .testTag("unit_${unit.name}")
                        ) {
                            Text(
                                text = unit.label,
                                fontSize = 11.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSel) GoldOnSecondaryContainer else OnSurfaceDark,
                                modifier = Modifier.padding(vertical = 6.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                val goldError = viewModel.validateAmount(goldGrams)
                OutlinedTextField(
                    value = goldGrams,
                    onValueChange = { viewModel.goldGrams.value = it },
                    isError = goldError != null,
                    label = { Text("Weight Owned in ${weightUnit.label}") },
                    supportingText = {
                        if (goldError != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_gold_grams_error")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = ErrorRed,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = goldError,
                                        color = ErrorRed,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    if (goldGrams.contains("-")) {
                                        Text(
                                            text = "Make Positive",
                                            color = EmeraldPrimary,
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .clickable { viewModel.goldGrams.value = goldGrams.replace("-", "").trim() }
                                                .padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                    Text(
                                        text = "Set to 0",
                                        color = ErrorRed,
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .clickable { viewModel.goldGrams.value = "0" }
                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    },
                    trailingIcon = {
                        if (goldError != null) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Validation error",
                                tint = ErrorRed,
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            Text(weightUnit.label, fontWeight = FontWeight.Bold, color = OnSurfaceVariantMuted, modifier = Modifier.padding(end = 12.dp))
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_gold_grams"),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (goldError != null) ErrorRed else EmeraldPrimary,
                        unfocusedBorderColor = if (goldError != null) ErrorRed else OutlineBorder.copy(alpha = 0.4f),
                        errorBorderColor = ErrorRed,
                        errorContainerColor = ErrorContainerRed.copy(alpha = 0.15f)
                    )
                )

                // Pure 24K Equivalence Banner
                val pureGrams = viewModel.getPureGoldGrams()
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SurfaceContainerDefault,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Pure 24K equivalent:", fontSize = 11.sp, color = OnSurfaceVariantMuted)
                        Text(
                            text = "${String.format(Locale.US, "%.2f", pureGrams)} g",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        )
                    }
                }

                // Jewelry Exemption Switch
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceContainerLow)
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Personal Customary Jewelry", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OnSurfaceDark)
                        Text(
                            text = if (exemptPersonalJewelry) "Exempt from Zakat calculation" else "Subject to Zakat calculation",
                            fontSize = 10.sp,
                            color = if (exemptPersonalJewelry) EmeraldPrimary else OnSurfaceVariantMuted
                        )
                    }
                    Switch(
                        checked = exemptPersonalJewelry,
                        onCheckedChange = { viewModel.exemptPersonalJewelry.value = it },
                        modifier = Modifier.testTag("toggle_jewelry_exemption")
                    )
                }

                // Market Value Pill
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SurfaceContainerLow,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Est. Value (@ ${selectedCurrency.symbol}${viewModel.getGoldRate().toInt()}/g):", fontSize = 11.sp, color = OnSurfaceVariantMuted)
                        Text(viewModel.formatCurrency(goldVal), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Gold Nisab: 87.48g pure gold", fontSize = 11.sp, color = OnSurfaceVariantMuted)
                    Text("${selectedCurrency.code} Standard", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldSecondary)
                }
            }
        }

        // 2. Silver Holdings Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceContainerLowest,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Diamond,
                            contentDescription = null,
                            tint = OutlineBorder,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "2. Silver Holdings",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceDark
                            )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(SurfaceContainerDefault)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("Fine Silver (99.9%)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OnSurfaceVariantMuted)
                    }
                }

                Text(
                    text = "Silver bars, investment rounds, and non-exempt utensils or savings.",
                    style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariantMuted)
                )

                val silverError = viewModel.validateAmount(silverGrams)
                OutlinedTextField(
                    value = silverGrams,
                    onValueChange = { viewModel.silverGrams.value = it },
                    isError = silverError != null,
                    label = { Text("Weight Owned in ${weightUnit.label}") },
                    supportingText = {
                        if (silverError != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_silver_grams_error")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = ErrorRed,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = silverError,
                                        color = ErrorRed,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    if (silverGrams.contains("-")) {
                                        Text(
                                            text = "Make Positive",
                                            color = EmeraldPrimary,
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .clickable { viewModel.silverGrams.value = silverGrams.replace("-", "").trim() }
                                                .padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                    Text(
                                        text = "Set to 0",
                                        color = ErrorRed,
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .clickable { viewModel.silverGrams.value = "0" }
                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    },
                    trailingIcon = {
                        if (silverError != null) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Validation error",
                                tint = ErrorRed,
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            Text(weightUnit.label, fontWeight = FontWeight.Bold, color = OnSurfaceVariantMuted, modifier = Modifier.padding(end = 12.dp))
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_silver_grams"),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (silverError != null) ErrorRed else EmeraldPrimary,
                        unfocusedBorderColor = if (silverError != null) ErrorRed else OutlineBorder.copy(alpha = 0.4f),
                        errorBorderColor = ErrorRed,
                        errorContainerColor = ErrorContainerRed.copy(alpha = 0.15f)
                    )
                )

                // Market Value Pill
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SurfaceContainerLow,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Est. Market Value (@ ${selectedCurrency.symbol}${viewModel.getSilverRate().toInt()}/g):", fontSize = 11.sp, color = OnSurfaceVariantMuted)
                        Text(viewModel.formatCurrency(silverVal), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Silver Nisab Anchor (612.36g)", fontSize = 11.sp, color = OnSurfaceVariantMuted)
                        Text("${(silverNisabProgress * 100).toInt()}% of benchmark", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                    }
                    LinearProgressIndicator(
                        progress = { silverNisabProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(100.dp)),
                        color = EmeraldPrimaryFixedDim,
                        trackColor = SurfaceContainerHigh
                    )
                }
            }
        }

        // 3. Business & Trade Goods
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceContainerLowest,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Storefront,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "3. Business & Trade Goods",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceDark
                            )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(SurfaceContainerDefault)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("🔒 Private", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OnSurfaceVariantMuted)
                    }
                }

                FinancialInputField(
                    label = "Business Cash & Bank Balances",
                    value = businessCash,
                    onValueChange = { viewModel.businessCash.value = it },
                    icon = Icons.Default.AccountBalance,
                    helperText = "Operating liquidity dedicated to commercial venture",
                    placeholder = "0",
                    testTag = "input_business_cash",
                    currencyCode = selectedCurrency.code,
                    currencySymbol = selectedCurrency.symbol
                )

                FinancialInputField(
                    label = "Inventory / Stock Resale Value",
                    value = businessInventory,
                    onValueChange = { viewModel.businessInventory.value = it },
                    icon = Icons.Default.Store,
                    helperText = "Wholesale market resale value of current tradable stock",
                    placeholder = "0",
                    testTag = "input_business_inventory",
                    currencyCode = selectedCurrency.code,
                    currencySymbol = selectedCurrency.symbol
                )
            }
        }

        // 4. Stocks, Shares & Investments
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceContainerLowest,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "4. Stocks, Equities & Sukuk",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceDark
                            )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(SurfaceContainerDefault)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("Tradable", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OnSurfaceVariantMuted)
                    }
                }

                FinancialInputField(
                    label = "Public Equities & Shares",
                    value = stocksEquities,
                    onValueChange = { viewModel.stocksEquities.value = it },
                    icon = Icons.Default.TrendingUp,
                    helperText = "Tradable equities or company net zakatable assets value",
                    placeholder = "0",
                    testTag = "input_stocks_equities",
                    currencyCode = selectedCurrency.code,
                    currencySymbol = selectedCurrency.symbol
                )

                FinancialInputField(
                    label = "Mutual Funds & Sukuk",
                    value = mutualFunds,
                    onValueChange = { viewModel.mutualFunds.value = it },
                    icon = Icons.Default.AccountBalance,
                    helperText = "Liquid unit trusts and Islamic bond investments",
                    placeholder = "0",
                    testTag = "input_mutual_funds",
                    currencyCode = selectedCurrency.code,
                    currencySymbol = selectedCurrency.symbol
                )
            }
        }

        // 5. Custom Assets & Portfolios
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceContainerLowest,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Payments,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "5. Custom Assets & Portfolios",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceDark
                            )
                        )
                    }

                    Button(
                        onClick = { showAddAssetDialog = true },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceContainerHigh, contentColor = EmeraldPrimary),
                        modifier = Modifier.testTag("add_custom_asset_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                            Text("Add Asset", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Text(
                    text = "Add crypto, rental income, private equity, or custom line items with configurable Zakatable ratios.",
                    style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariantMuted)
                )

                if (customAssets.isEmpty()) {
                    Text(
                        text = "No custom assets added. Tap 'Add Asset' to include non-traditional holdings.",
                        fontSize = 11.sp,
                        color = OnSurfaceVariantMuted
                    )
                } else {
                    customAssets.forEach { item ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = SurfaceContainerLow,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(item.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = OnSurfaceDark)
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(100.dp))
                                                .background(EmeraldPrimaryContainer)
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text("${(item.zakatablePercentage * 100).toInt()}% Zakatable", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        }
                                    }
                                    Text(
                                        text = "Gross: ${viewModel.formatCurrency(item.grossValue)} • Zakatable: ${viewModel.formatCurrency(item.zakatableValue)}",
                                        fontSize = 11.sp,
                                        color = OnSurfaceVariantMuted
                                    )
                                }

                                IconButton(
                                    onClick = { viewModel.removeCustomAsset(item.id) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = ErrorRed, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        // Subtotal Hero Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = EmeraldPrimaryContainer,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "STEP 2 SUBTOTAL",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimaryFixed,
                        letterSpacing = 1.sp
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(EmeraldPrimary)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("Subject to Hawl", fontSize = 10.sp, color = EmeraldPrimaryFixed, fontWeight = FontWeight.Bold)
                    }
                }

                Text(
                    text = viewModel.formatCurrency(step2Total),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )

                Text(
                    text = "Estimated 2.5% Zakat Due: ${viewModel.formatCurrency(estimatedZakat)}",
                    fontSize = 12.sp,
                    color = GoldSecondaryFixed,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Metals (Gold + Silver):", fontSize = 11.sp, color = EmeraldPrimaryFixedDim)
                    Text(viewModel.formatCurrency(metalsTotal), fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Business Trade Goods:", fontSize = 11.sp, color = EmeraldPrimaryFixedDim)
                    Text(viewModel.formatCurrency(bizTotal), fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Stocks, Shares & Sukuk:", fontSize = 11.sp, color = EmeraldPrimaryFixedDim)
                    Text(viewModel.formatCurrency(investmentsTotal), fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Custom Assets:", fontSize = 11.sp, color = EmeraldPrimaryFixedDim)
                    Text(viewModel.formatCurrency(customAssetsTotal), fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (showAddAssetDialog) {
            val assetGrossError = viewModel.validateAmount(newAssetGrossValue)
            AlertDialog(
                onDismissRequest = { showAddAssetDialog = false },
                title = { Text("Add Custom Zakatable Asset", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = newAssetName,
                            onValueChange = { newAssetName = it },
                            label = { Text("Asset Name (e.g. BTC, Rental)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        )
                        OutlinedTextField(
                            value = newAssetGrossValue,
                            onValueChange = { newAssetGrossValue = it },
                            isError = assetGrossError != null && newAssetGrossValue.isNotBlank(),
                            label = { Text("Gross Valuation (${selectedCurrency.code})") },
                            supportingText = {
                                if (assetGrossError != null && newAssetGrossValue.isNotBlank()) {
                                    Text(assetGrossError, color = ErrorRed, fontSize = 11.sp, modifier = Modifier.testTag("input_custom_asset_gross_error"))
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_custom_asset_gross"),
                            shape = RoundedCornerShape(8.dp)
                        )
                        Text("Zakatable Ratio Preset:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = OnSurfaceDark)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            val ratios = listOf(
                                "100%" to 1.0,
                                "30% Stock" to 0.30,
                                "25% Div" to 0.25,
                                "Exempt" to 0.0
                            )
                            ratios.forEach { (lbl, ratio) ->
                                val isSel = newAssetRatio == ratio
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (isSel) EmeraldPrimaryContainer else SurfaceContainerHigh,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { newAssetRatio = ratio }
                                ) {
                                    Text(
                                        text = lbl,
                                        fontSize = 10.sp,
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSel) Color.White else OnSurfaceDark,
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    val canAdd = newAssetName.isNotBlank() &&
                        newAssetGrossValue.isNotBlank() &&
                        assetGrossError == null &&
                        (newAssetGrossValue.toDoubleOrNull() ?: 0.0) >= 0.0
                    Button(
                        onClick = {
                            val gVal = newAssetGrossValue.toDoubleOrNull() ?: 0.0
                            if (canAdd) {
                                viewModel.addCustomAsset(newAssetName.trim(), gVal, newAssetRatio)
                                newAssetName = ""
                                newAssetGrossValue = ""
                                showAddAssetDialog = false
                            }
                        },
                        enabled = canAdd,
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                    ) {
                        Text("Add Asset")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddAssetDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Validation Warning Banner
        if (!isStep2Valid) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = ErrorContainerRed.copy(alpha = 0.5f),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("step2_validation_warning")
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = ErrorRed,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Please enter valid, non-negative numerical amounts before continuing.",
                        color = ErrorRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { viewModel.setCalcStep(1) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceContainerDefault, contentColor = EmeraldPrimary)
            ) {
                Text("Back", fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = {
                    if (isStep2Valid) {
                        viewModel.setCalcStep(3)
                    }
                },
                enabled = isStep2Valid,
                modifier = Modifier
                    .weight(2f)
                    .height(48.dp)
                    .testTag("continue_to_step3_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isStep2Valid) EmeraldPrimary else OutlineBorder.copy(alpha = 0.5f),
                    contentColor = Color.White,
                    disabledContainerColor = SurfaceContainerHigh,
                    disabledContentColor = OnSurfaceVariantMuted
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isStep2Valid) "Next: Debts" else "Resolve Errors to Proceed",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isStep2Valid) Color.White else OnSurfaceVariantMuted,
                            fontSize = 13.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (isStep2Valid) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

// ==========================================
// STEP 3: ASSET & LIABILITY CLASSIFICATION (RECEIVABLES & DEBTS)
// ==========================================
@Composable
private fun Step3ReceivablesAndDebts(viewModel: ZakatViewModel) {
    val debtorName by viewModel.debtorName.collectAsState()
    val moneyOwed by viewModel.moneyOwed.collectAsState()
    val recoverability by viewModel.receivablesRecoverability.collectAsState()

    val debtType by viewModel.debtType.collectAsState()
    val deductibleDebts by viewModel.deductibleDebts.collectAsState()
    val customDebts by viewModel.customDebts.collectAsState()
    val selectedCurrency by viewModel.selectedCurrency.collectAsState()

    var showAddDebtDialog by remember { mutableStateOf(false) }
    var newDebtDesc by remember { mutableStateOf("") }
    var newDebtAmount by remember { mutableStateOf("") }
    var newDebtCategory by remember { mutableStateOf("Immediate Bill") }

    val scrollState = rememberScrollState()

    val grossAssets = viewModel.getGrossAssets()
    val netWealth = viewModel.getNetWealth()
    val step3Errors = viewModel.getStep3Errors()
    val isStep3Valid = step3Errors.isEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceMint)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (!isStep3Valid) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = ErrorContainerRed,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("step3_validation_warning")
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = ErrorRed,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Invalid Numerical Input Detected",
                            color = ErrorRed,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "All receivables and liability fields must be non-negative numerical amounts (≥ 0). Please resolve the following inputs:",
                        color = OnSurfaceDark,
                        fontSize = 11.5.sp,
                        lineHeight = 16.sp
                    )
                    step3Errors.forEach { (field, error) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(start = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(ErrorRed)
                            )
                            Text(
                                text = "$field: $error",
                                color = ErrorRed,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Real-time Net Wealth Summary Strip
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = SurfaceContainerLow,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceContainerHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
                    }
                    Column {
                        Text("Gross Assets Declared", fontSize = 11.sp, color = OnSurfaceVariantMuted)
                        Text(viewModel.formatCurrency(grossAssets), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("Net Zakatable (est.)", fontSize = 11.sp, color = OnSurfaceVariantMuted)
                    Text(viewModel.formatCurrency(netWealth), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                }
            }
        }

        // SECTION 1: Money Owed to Me (Receivables)
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceContainerLowest,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(SurfaceContainerHigh),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Handshake, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                        }
                        Column {
                            Text("Money Owed to Me", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = EmeraldPrimary))
                            Text("Personal loans, trade credit, or refunds", fontSize = 11.sp, color = OnSurfaceVariantMuted)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(SurfaceContainerHigh)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("RECEIVABLES", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                    }
                }

                // Guidance Banner
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SurfaceContainerLow,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Help, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
                        Text(
                            text = "Why do we ask this? Money owed to you is considered Zakatable if it is expected to be repaid. If doubtful or uncollectible, it is only assessed upon actual receipt.",
                            fontSize = 11.sp,
                            color = OnSurfaceVariantMuted,
                            lineHeight = 15.sp
                        )
                    }
                }

                OutlinedTextField(
                    value = debtorName,
                    onValueChange = { viewModel.debtorName.value = it },
                    label = { Text("Who owes you money? (Name / Description)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldPrimary,
                        unfocusedBorderColor = OutlineBorder.copy(alpha = 0.4f)
                    )
                )

                FinancialInputField(
                    label = "Amount Owed",
                    value = moneyOwed,
                    onValueChange = { viewModel.moneyOwed.value = it },
                    icon = Icons.Default.MonetizationOn,
                    helperText = "Total pending sum",
                    placeholder = "0",
                    testTag = "input_money_owed",
                    currencyCode = selectedCurrency.code,
                    currencySymbol = selectedCurrency.symbol
                )

                // Fiqh Classification Radio Matrix
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Likelihood of Repayment (Fiqh Classification)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = OnSurfaceDark)

                    FiqhRadioOption(
                        title = "Yes — Confirmed / Readily recoverable",
                        badge = "Included",
                        description = "Borrower acknowledges and can pay. Counted directly into current Hawl wealth.",
                        isSelected = recoverability == "confirmed",
                        onClick = { viewModel.receivablesRecoverability.value = "confirmed" }
                    )

                    FiqhRadioOption(
                        title = "No — Bad debt / Uncollectible",
                        badge = "Exempt",
                        description = "Borrower unable or unwilling. Exempt from Zakat until collected in hand.",
                        isSelected = recoverability == "bad",
                        onClick = { viewModel.receivablesRecoverability.value = "bad" }
                    )

                    FiqhRadioOption(
                        title = "Not Sure / Disputed",
                        badge = null,
                        description = "Consult conservative ruling or defer assessment until settlement is received.",
                        isSelected = recoverability == "doubtful",
                        onClick = { viewModel.receivablesRecoverability.value = "doubtful" }
                    )
                }
            }
        }

        // SECTION 2: Deductible Debts & Liabilities
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceContainerLowest,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(SurfaceContainerHigh),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                        }
                        Column {
                            Text("Deductible Debts & Liabilities", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = EmeraldPrimary))
                            Text("Immediate obligations due within lunar cycle", fontSize = 11.sp, color = OnSurfaceVariantMuted)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(SurfaceContainerHigh)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("DEDUCTIONS", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SurfaceContainerLow,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
                        Text(
                            text = "Debts due immediately or within the current lunar cycle reduce your gross zakatable assets before final calculation. Future non-due installments (e.g. long-term mortgages) are not fully deductible.",
                            fontSize = 11.sp,
                            color = OnSurfaceVariantMuted,
                            lineHeight = 15.sp
                        )
                    }
                }

                OutlinedTextField(
                    value = debtType,
                    onValueChange = { viewModel.debtType.value = it },
                    label = { Text("Type of Debt / Liability") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldPrimary,
                        unfocusedBorderColor = OutlineBorder.copy(alpha = 0.4f)
                    )
                )

                FinancialInputField(
                    label = "Primary Immediate Due Amount",
                    value = deductibleDebts,
                    onValueChange = { viewModel.deductibleDebts.value = it },
                    icon = Icons.Default.CreditCardOff,
                    helperText = "Debts reducing current Hawl net pool",
                    placeholder = "0",
                    testTag = "input_deductible_debts",
                    currencyCode = selectedCurrency.code,
                    currencySymbol = selectedCurrency.symbol
                )

                // Quick Add chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    QuickChip("+ Supplier Bill") {
                        viewModel.debtType.value = "Supplier Pending Bill"
                        viewModel.deductibleDebts.value = "25000"
                    }
                    QuickChip("+ Credit Card") {
                        viewModel.debtType.value = "Credit Card Balance (Current Cycle)"
                        viewModel.deductibleDebts.value = "18000"
                    }
                    QuickChip("+ Rent Due") {
                        viewModel.debtType.value = "House Rent Due"
                        viewModel.deductibleDebts.value = "50000"
                    }
                }

                // Itemized Liabilities List
                HorizontalDivider(color = OutlineBorder.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Additional Itemized Liabilities", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = OnSurfaceDark)
                    Button(
                        onClick = { showAddDebtDialog = true },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceContainerHigh, contentColor = EmeraldPrimary),
                        modifier = Modifier.testTag("add_custom_debt_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                            Text("Add Debt", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                if (customDebts.isNotEmpty()) {
                    customDebts.forEach { debt ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SurfaceContainerLow,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(debt.description, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OnSurfaceDark)
                                    Text("${debt.categoryTag} • ${viewModel.formatCurrency(debt.amount)}", fontSize = 10.sp, color = OnSurfaceVariantMuted)
                                }
                                IconButton(
                                    onClick = { viewModel.removeCustomDebt(debt.id) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = ErrorRed, modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                    }
                }

                // Total Deductible Callout
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SurfaceContainerHigh,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total Deductible Liabilities:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = OnSurfaceDark)
                        Text("-${viewModel.formatCurrency(viewModel.getTotalDeductibleDebts())}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ErrorRed)
                    }
                }
            }
        }

        if (showAddDebtDialog) {
            val newDebtAmtError = viewModel.validateAmount(newDebtAmount)
            val isNewDebtValid = newDebtDesc.isNotBlank() && newDebtAmtError == null && (newDebtAmount.toDoubleOrNull() ?: 0.0) > 0.0

            AlertDialog(
                onDismissRequest = { showAddDebtDialog = false },
                title = { Text("Add Itemized Liability", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = newDebtDesc,
                            onValueChange = { newDebtDesc = it },
                            label = { Text("Description (e.g. Employee Wages, Urgent Tax)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        )
                        OutlinedTextField(
                            value = newDebtAmount,
                            onValueChange = { newDebtAmount = it },
                            label = { Text("Amount (${selectedCurrency.code})") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_custom_debt_amount"),
                            shape = RoundedCornerShape(8.dp),
                            isError = newDebtAmtError != null,
                            supportingText = if (newDebtAmtError != null) {
                                { Text(newDebtAmtError, color = ErrorRed, modifier = Modifier.testTag("input_custom_debt_amount_error")) }
                            } else null
                        )
                        OutlinedTextField(
                            value = newDebtCategory,
                            onValueChange = { newDebtCategory = it },
                            label = { Text("Category Tag") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val amt = newDebtAmount.toDoubleOrNull() ?: 0.0
                            if (isNewDebtValid) {
                                viewModel.addCustomDebt(newDebtDesc.trim(), amt, newDebtCategory.trim())
                                newDebtDesc = ""
                                newDebtAmount = ""
                                showAddDebtDialog = false
                            }
                        },
                        enabled = isNewDebtValid,
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                    ) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDebtDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { viewModel.setCalcStep(2) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceContainerDefault, contentColor = EmeraldPrimary)
            ) {
                Text("Back", fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = {
                    if (isStep3Valid) {
                        viewModel.setCalcStep(4)
                    }
                },
                enabled = isStep3Valid,
                modifier = Modifier
                    .weight(2f)
                    .height(48.dp)
                    .testTag("continue_to_step4_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isStep3Valid) EmeraldPrimary else OutlineBorder.copy(alpha = 0.5f),
                    contentColor = Color.White,
                    disabledContainerColor = SurfaceContainerHigh,
                    disabledContentColor = OnSurfaceVariantMuted
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isStep3Valid) "Next: Nisab Check" else "Resolve Errors to Proceed",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isStep3Valid) Color.White else OnSurfaceVariantMuted,
                            fontSize = 13.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (isStep3Valid) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

// ==========================================
// STEP 4: REVIEW & NISAB CHECK
// ==========================================
@Composable
private fun Step4ReviewAndNisab(viewModel: ZakatViewModel) {
    val nisabChoice by viewModel.nisabStandard.collectAsState()
    val scrollState = rememberScrollState()

    val grossAssets = viewModel.getGrossAssets()
    val deductions = viewModel.getDeductibleDebtsValue()
    val netWealth = viewModel.getNetWealth()
    val nisabFloor = viewModel.getNisabFloor()
    val isEligible = viewModel.isNisabMet()
    val multiplier = viewModel.getNisabMultiplier()
    val zakatDue = viewModel.getZakatDue()

    val allErrors = viewModel.getStep1Errors() + viewModel.getStep2Errors() + viewModel.getStep3Errors()
    val isAllValid = allErrors.isEmpty()

    // Accordion expansion states
    var expCash by remember { mutableStateOf(false) }
    var expMetals by remember { mutableStateOf(false) }
    var expInvest by remember { mutableStateOf(false) }
    var expDebts by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceMint)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (!isAllValid) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = ErrorContainerRed,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("step4_validation_warning")
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = ErrorRed,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Calculation Blocked: Invalid Numerical Values",
                            color = ErrorRed,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "To guarantee an accurate and valid Zakat calculation according to Islamic guidelines, all assets and liabilities must be non-negative numerical amounts. Please correct these fields before finalizing:",
                        color = OnSurfaceDark,
                        fontSize = 11.5.sp,
                        lineHeight = 16.sp
                    )
                    allErrors.forEach { (field, error) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(start = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(ErrorRed)
                            )
                            Text(
                                text = "$field: $error",
                                color = ErrorRed,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Section 2: Nisab Eligibility Comparison Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceContainerLowest,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.Balance, contentDescription = null, tint = GoldSecondary, modifier = Modifier.size(20.dp))
                        Text("Nisab Benchmark", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = EmeraldPrimary))
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(SurfaceContainerDefault)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("Updated Today", fontSize = 10.sp, color = OnSurfaceVariantMuted)
                    }
                }

                // Segmented Toggle: Silver vs Gold
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(SurfaceContainerLow)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    NisabSegmentButton(
                        title = "Silver Nisab",
                        subtitle = "52.5 tolas (612.36g)",
                        isSelected = nisabChoice == "Silver",
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.nisabStandard.value = "Silver" }
                    )
                    NisabSegmentButton(
                        title = "Gold Nisab",
                        subtitle = "7.5 tolas (87.48g)",
                        isSelected = nisabChoice == "Gold",
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.nisabStandard.value = "Gold" }
                    )
                }

                // Two-tier gauge
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("COMPARATIVE THRESHOLD", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OnSurfaceVariantMuted, letterSpacing = 1.sp)
                        Text("Exceeds by ${"%.1f".format(multiplier)}x", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldSecondary)
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Required Nisab Floor", fontSize = 11.sp, color = OnSurfaceVariantMuted)
                            Text(viewModel.formatCurrency(nisabFloor), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OnSurfaceDark)
                        }
                        LinearProgressIndicator(
                            progress = { (nisabFloor / (netWealth.coerceAtLeast(1.0))).coerceIn(0.0, 1.0).toFloat() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(100.dp)),
                            color = GoldSecondaryFixedDim,
                            trackColor = SurfaceContainerDefault
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Your Zakatable Net Wealth", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                            Text(viewModel.formatCurrency(netWealth), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                        }
                        LinearProgressIndicator(
                            progress = { 1f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(100.dp)),
                            color = EmeraldPrimaryContainer,
                            trackColor = SurfaceContainerDefault
                        )
                    }
                }

                // Status Banner: Zakat is Obligatory
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceContainerHigh,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(EmeraldPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = if (isEligible) "Zakat is Obligatory" else "Below Nisab Threshold",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary
                                )
                                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(EmeraldPrimary))
                            }
                            Text(
                                text = if (isEligible)
                                    "Your net wealth comfortably surpasses the standard $nisabChoice Nisab of ${viewModel.formatCurrency(nisabFloor)}. The required 1-lunar-year Hawl condition has been fulfilled."
                                else
                                    "Your net wealth is below the $nisabChoice standard threshold. No obligatory Zakat due.",
                                fontSize = 11.sp,
                                color = OnSurfaceVariantMuted,
                                lineHeight = 15.sp,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }

                // Verify with Google Search Grounding Button
                Button(
                    onClick = { viewModel.showNisabVerificationDialog.value = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .testTag("btn_step4_verify_nisab"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SurfaceContainerLow,
                        contentColor = EmeraldPrimary
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "Verify Nisab Floor with Google Search",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Section 3: Asset Accordions / Declared Holdings
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Declared Holdings", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = EmeraldPrimary))
                Text("4 categories", fontSize = 11.sp, color = OnSurfaceVariantMuted)
            }

            // Category 1: Cash & Savings
            HoldingAccordionCard(
                title = "Cash & Savings",
                subtitle = "Bank, cash, liquid funds",
                amountText = viewModel.formatCurrency(viewModel.getLiquidTotal()),
                icon = Icons.Default.AccountBalanceWallet,
                isExpanded = expCash,
                onToggle = { expCash = !expCash },
                onEdit = { viewModel.setCalcStep(1) },
                expandedContent = {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        HoldingsItemRow("Physical Cash", viewModel.cashOnHand.value)
                        HoldingsItemRow("Checking Accounts", viewModel.bankCurrent.value)
                        HoldingsItemRow("Savings Balances", viewModel.bankSavings.value)
                    }
                }
            )

            // Category 2: Gold & Silver Holdings
            HoldingAccordionCard(
                title = "Gold & Silver",
                subtitle = "${viewModel.goldGrams.value}g Gold + ${viewModel.silverGrams.value}g Silver",
                amountText = viewModel.formatCurrency(viewModel.getMetalsTotal()),
                icon = Icons.Default.MonetizationOn,
                isExpanded = expMetals,
                onToggle = { expMetals = !expMetals },
                onEdit = { viewModel.setCalcStep(2) },
                expandedContent = {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        HoldingsItemRow("24K Bullion / Jewelry", viewModel.formatCurrency(viewModel.getGoldValue()))
                        HoldingsItemRow("Sterling / Fine Silver", viewModel.formatCurrency(viewModel.getSilverValue()))
                    }
                }
            )

            // Category 3: Investments & Equities
            HoldingAccordionCard(
                title = "Investments & Equities",
                subtitle = "Stocks & Mutual funds",
                amountText = viewModel.formatCurrency(viewModel.getInvestmentsTotal()),
                icon = Icons.Default.TrendingUp,
                isExpanded = expInvest,
                onToggle = { expInvest = !expInvest },
                onEdit = { viewModel.setCalcStep(2) },
                expandedContent = {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        HoldingsItemRow("Public Stocks & Equities", viewModel.stocksEquities.value)
                        HoldingsItemRow("Mutual Funds & Sukuk", viewModel.mutualFunds.value)
                    }
                }
            )

            // Category 4: Deductible Liabilities
            HoldingAccordionCard(
                title = "Deductible Liabilities",
                subtitle = "Immediate debts & bills due",
                amountText = "-${viewModel.formatCurrency(deductions)}",
                icon = Icons.Default.CreditCardOff,
                isExpanded = expDebts,
                onToggle = { expDebts = !expDebts },
                onEdit = { viewModel.setCalcStep(3) },
                isDeduction = true,
                expandedContent = {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        HoldingsItemRow(viewModel.debtType.value, "-${viewModel.formatCurrency(deductions)}")
                    }
                }
            )
        }

        // Section 4: Calculation Breakdown Emerald Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = EmeraldPrimary,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("CALCULATION BREAKDOWN", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimaryFixed, letterSpacing = 1.sp)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(EmeraldPrimaryContainer)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("🔒 Offline Computed", fontSize = 10.sp, color = GoldSecondaryFixed, fontWeight = FontWeight.Bold)
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Gross Zakatable Assets", fontSize = 12.sp, color = EmeraldPrimaryFixedDim)
                    Text(viewModel.formatCurrency(grossAssets), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Less Deductions", fontSize = 12.sp, color = EmeraldPrimaryFixedDim)
                    Text("-${viewModel.formatCurrency(deductions)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GoldSecondaryFixedDim)
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = EmeraldPrimaryContainer.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Net Zakatable Amount", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(viewModel.formatCurrency(netWealth), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                // Total Zakat Due (2.5%) Highlight Box
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceContainerLowest,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("TOTAL ZAKAT DUE (2.5%)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldSecondary, letterSpacing = 1.sp)
                            Text(viewModel.formatCurrency(zakatDue), style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = EmeraldPrimary))
                        }

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(GoldSecondaryFixed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Payments, contentDescription = null, tint = GoldOnSecondaryContainer, modifier = Modifier.size(24.dp))
                        }
                    }
                }

                Text(
                    text = "Computed according to orthodox Fiqh consensus ($nisabChoice standard applied). 100% private and stored only in your device's memory.",
                    fontSize = 11.sp,
                    color = EmeraldPrimaryFixedDim,
                    lineHeight = 15.sp
                )
            }
        }

        // Action CTA
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { viewModel.setCalcStep(3) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceContainerDefault, contentColor = EmeraldPrimary)
            ) {
                Text("Back", fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = {
                    if (isAllValid) {
                        viewModel.setCalcStep(5)
                    }
                },
                enabled = isAllValid,
                modifier = Modifier
                    .weight(2f)
                    .height(48.dp)
                    .testTag("confirm_and_proceed_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isAllValid) EmeraldPrimary else OutlineBorder.copy(alpha = 0.5f),
                    contentColor = Color.White,
                    disabledContainerColor = SurfaceContainerHigh,
                    disabledContentColor = OnSurfaceVariantMuted
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isAllValid) "Next: Summary" else "Resolve Errors to Proceed",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isAllValid) Color.White else OnSurfaceVariantMuted,
                            fontSize = 13.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (isAllValid) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

// ==========================================
// STEP 5: CALCULATION SUMMARY & VAULT SAVE
// ==========================================
@Composable
private fun Step5Summary(
    viewModel: ZakatViewModel,
    onOpenAsnafGuide: () -> Unit
) {
    val scrollState = rememberScrollState()
    val vaultLabel by viewModel.vaultLabel.collectAsState()
    val isSaved by viewModel.isSavedToVault.collectAsState()
    val reminderActive by viewModel.annualRamadanReminder.collectAsState()
    val activeRecord by viewModel.activeStatementRecord.collectAsState()
    val context = LocalContext.current

    var isSavingInProgress by remember { mutableStateOf(false) }
    var showPdfExportDialog by remember { mutableStateOf(false) }

    val grossAssets = viewModel.getGrossAssets()
    val deductions = viewModel.getDeductibleDebtsValue()
    val netWealth = viewModel.getNetWealth()
    val zakatDue = viewModel.getZakatDue()
    val multiplier = viewModel.getNisabMultiplier()
    val allErrors = viewModel.getStep1Errors() + viewModel.getStep2Errors() + viewModel.getStep3Errors()
    val isAllValid = allErrors.isEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceMint)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Highlight Card (Final Obligation)
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceContainerHighest,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text("FINAL ZAKAT AMOUNT (2.5%)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary, letterSpacing = 1.sp)
                        Text(
                            text = viewModel.formatCurrency(zakatDue),
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(EmeraldPrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Eco, contentDescription = null, tint = EmeraldOnPrimary, modifier = Modifier.size(22.dp))
                    }
                }

                // Nisab Status Banner
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SurfaceContainerLowest,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(EmeraldPrimary))
                            Column {
                                Text("Eligible for Zakat", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                                Text("Silver Nisab Met: ${"%.2f".format(multiplier)}x threshold", fontSize = 11.sp, color = OnSurfaceVariantMuted)
                            }
                        }
                        Icon(Icons.Default.Verified, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = GoldSecondary, modifier = Modifier.size(14.dp))
                        Text("Ready to discharge", fontSize = 11.sp, color = OnSurfaceVariantMuted)
                    }
                    Text("Hawl cycle satisfied", fontSize = 11.sp, color = OnSurfaceVariantMuted)
                }
            }
        }

        // Financial Ledger Breakdown Cards
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("LEDGER BREAKDOWN", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OnSurfaceVariantMuted, letterSpacing = 1.sp)
                Text("Audited 100% locally", fontSize = 11.sp, color = EmeraldPrimary)
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SurfaceContainerLowest,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    LedgerBreakdownRow(
                        title = "Zakatable Assets",
                        subtitle = "Cash, Gold, Stocks",
                        amountText = viewModel.formatCurrency(grossAssets),
                        icon = Icons.Default.AccountBalanceWallet,
                        tint = EmeraldPrimary
                    )
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(SurfaceContainerHighest))
                    LedgerBreakdownRow(
                        title = "Liabilities / Deductions",
                        subtitle = "Immediate debts & expenses",
                        amountText = "-${viewModel.formatCurrency(deductions)}",
                        icon = Icons.Default.RemoveCircleOutline,
                        tint = ErrorRed,
                        amountColor = ErrorRed
                    )
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(SurfaceContainerHighest))
                    LedgerBreakdownRow(
                        title = "Net Zakatable Wealth",
                        subtitle = "Subject to 2.5% rate",
                        amountText = viewModel.formatCurrency(netWealth),
                        icon = Icons.Default.MonetizationOn,
                        tint = EmeraldPrimary,
                        isBold = true
                    )
                }
            }
        }

        // Save to Local Vault Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceContainerLowest,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceContainerDefault),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
                    }
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Save to Local Vault", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = EmeraldPrimary))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(100.dp))
                                    .background(EmeraldPrimaryFixed)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("OFFLINE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                            }
                        }
                        Text(
                            text = "This calculation will be saved locally on your device. No account or internet connection is required.",
                            fontSize = 11.sp,
                            color = OnSurfaceVariantMuted,
                            lineHeight = 15.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }

                OutlinedTextField(
                    value = vaultLabel,
                    onValueChange = { viewModel.vaultLabel.value = it },
                    label = { Text("Label (optional)") },
                    placeholder = { Text("e.g. Ramadan 2026 / Business Vault") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldPrimary,
                        unfocusedBorderColor = OutlineBorder.copy(alpha = 0.4f)
                    )
                )

                Button(
                    onClick = {
                        if (isAllValid) {
                            isSavingInProgress = true
                            viewModel.saveCurrentCalculation()
                            Toast.makeText(context, "✨ Encrypted record safely committed to device storage!", Toast.LENGTH_SHORT).show()
                            isSavingInProgress = false
                        }
                    },
                    enabled = isAllValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("save_vault_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSaved) SurfaceContainerHigh else EmeraldPrimaryContainer,
                        contentColor = if (isSaved) EmeraldPrimary else Color.White,
                        disabledContainerColor = SurfaceContainerHigh,
                        disabledContentColor = OnSurfaceVariantMuted
                    )
                ) {
                    if (isSavingInProgress) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = if (isSaved) Icons.Default.CheckCircle else if (!isAllValid) Icons.Default.Info else Icons.Default.Save,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (!isAllValid) "Resolve Errors to Save" else if (isSaved) "Stored in Device Vault" else "Save to Device Vault",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                if (isSaved) {
                    Text(
                        text = "✨ Encrypted record safely committed to device storage!",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Export & Safeguard Section
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceContainerLowest,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                        Text("Export & Safeguard", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = EmeraldPrimary))
                    }
                    Text("PDF Document", fontSize = 11.sp, color = OnSurfaceVariantMuted)
                }

                // Download PDF Button
                Button(
                    onClick = {
                        viewModel.prepareCurrentStatement()
                        showPdfExportDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("download_pdf_statement_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SurfaceContainerDefault,
                        contentColor = EmeraldPrimary
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                            Text("Download Detailed PDF Statement", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Icon(Icons.Default.Download, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
                    }
                }

                // Annual Ramadan Reminder Toggle
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SurfaceContainerLow,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(GoldSecondaryFixed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = GoldOnSecondaryContainer, modifier = Modifier.size(16.dp))
                            }
                            Column {
                                Text("Annual Ramadan Reminder", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = OnSurfaceDark)
                                Text("Hawl calendar alert (1448 AH)", fontSize = 10.sp, color = OnSurfaceVariantMuted)
                            }
                        }

                        Switch(
                            checked = reminderActive,
                            onCheckedChange = { viewModel.annualRamadanReminder.value = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = EmeraldPrimary
                            )
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
                    Text(
                        text = "Full step-by-step breakdown is included in this encrypted local record. You can export or clear this data at any time from Settings.",
                        fontSize = 10.sp,
                        color = OnSurfaceVariantMuted,
                        lineHeight = 14.sp
                    )
                }
            }
        }

        // Ready to Distribute Asnaf Banner
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = SurfaceContainerDefault,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(EmeraldPrimaryFixedDim),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.VolunteerActivism, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
                    }
                    Column {
                        Text("Ready to Distribute?", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                        Text("View 8 Quranic Asnaf categories", fontSize = 10.sp, color = OnSurfaceVariantMuted)
                    }
                }

                Button(
                    onClick = onOpenAsnafGuide,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SurfaceContainerLowest,
                        contentColor = EmeraldPrimary
                    )
                ) {
                    Text("Guidance", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Action Buttons: Back + View Statement
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { viewModel.setCalcStep(4) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceContainerDefault, contentColor = EmeraldPrimary)
            ) {
                Text("Back", fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = {
                    viewModel.prepareCurrentStatement()
                    viewModel.setCalcStep(6)
                },
                modifier = Modifier
                    .weight(2f)
                    .height(48.dp)
                    .testTag("view_official_statement_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary, contentColor = Color.White)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "View Statement",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 13.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    if (showPdfExportDialog && activeRecord != null) {
        PdfExportDialog(
            record = activeRecord!!,
            onDismiss = { showPdfExportDialog = false }
        )
    }
}

// ==========================================
// STEP 6: OFFICIAL ZAKAT STATEMENT / CERTIFICATE
// ==========================================
@Composable
private fun Step6OfficialStatement(
    viewModel: ZakatViewModel,
    onNavigateToHistory: () -> Unit
) {
    val record by viewModel.activeStatementRecord.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var showPdfExportDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (record == null) {
            viewModel.prepareCurrentStatement()
        }
    }

    val activeRecord = record ?: run {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = EmeraldPrimary)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceMint)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Physical PDF Sheet Aesthetic Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceContainerLowest,
            shadowElevation = 3.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Top Guilloché Gradient Accent Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(100.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(EmeraldPrimary, GoldSecondaryContainer, EmeraldPrimary)
                            )
                        )
                )

                // Header & Logo Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ZakatBrandLogo(size = 40.dp)
                        Column {
                            Text("Zakat Companion", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = EmeraldPrimary))
                            Text("Shariah Obligation Certificate", fontSize = 10.sp, color = OnSurfaceVariantMuted)
                            Text("AUTONOMOUS LEDGER", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = GoldSecondary, letterSpacing = 0.5.sp)
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("REF: ${activeRecord.refNumber}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OnSurfaceDark)
                        Text("12 Aug 2026", fontSize = 10.sp, color = OnSurfaceVariantMuted)
                        Text(activeRecord.hijriYear, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = GoldSecondary)
                    }
                }

                // Shariah Compliance Stamp
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SurfaceContainerLow,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(GoldSecondaryFixed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Stars, contentDescription = null, tint = GoldOnSecondaryContainer, modifier = Modifier.size(14.dp))
                            }
                            Column {
                                Text("100% AUDITED COMPLIANCE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                                Text("${activeRecord.nisabStandard} Nisab Threshold Applied", fontSize = 10.sp, color = OnSurfaceVariantMuted)
                            }
                        }
                        Icon(Icons.Default.Verified, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
                    }
                }

                // Key Aggregate Callout (Emerald Certificate Tile)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = EmeraldPrimaryContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("NET ZAKAT OBLIGATION (2.5%)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimaryFixedDim, letterSpacing = 1.sp)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(100.dp))
                                    .background(GoldSecondary)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("DUE NOW", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        Text(
                            text = viewModel.formatCurrency(activeRecord.zakatDue),
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Hawl Cleared Period", fontSize = 11.sp, color = SurfaceContainerHigh)
                            Text("1 Full Lunar Year", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }

                // Financial Ledger Overview
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("BALANCE SHEET ASSESSMENT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OnSurfaceVariantMuted, letterSpacing = 1.sp)
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SurfaceContainerLow,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Total Zakatable Assets", fontSize = 11.sp, color = OnSurfaceDark)
                                Text(viewModel.formatCurrency(activeRecord.grossAssets), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = OnSurfaceDark)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Deductible Liabilities", fontSize = 11.sp, color = OnSurfaceVariantMuted)
                                Text("-${viewModel.formatCurrency(activeRecord.deductions)}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ErrorRed)
                            }
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(SurfaceContainerHighest))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Net Zakatable Wealth", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                                Text(viewModel.formatCurrency(activeRecord.netWealth), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                            }
                        }
                    }
                }

                // Itemized Ledger Breakdown Table
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("ASSET DISSECTION", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OnSurfaceVariantMuted, letterSpacing = 1.sp)

                    DissectionRow("Cash & Liquid Deposits", "Base wealth: ${viewModel.formatCurrency(activeRecord.cashOnHand + activeRecord.bankCurrent + activeRecord.bankSavings)}", viewModel.formatCurrency((activeRecord.cashOnHand + activeRecord.bankCurrent + activeRecord.bankSavings) * 0.025))
                    DissectionRow("Gold & Silver (${activeRecord.goldGrams}g + ${activeRecord.silverGrams}g)", "Base wealth: ${viewModel.formatCurrency(activeRecord.goldGrams * activeRecord.goldRate + activeRecord.silverGrams * activeRecord.silverRate)}", viewModel.formatCurrency((activeRecord.goldGrams * activeRecord.goldRate + activeRecord.silverGrams * activeRecord.silverRate) * 0.025))
                    DissectionRow("Investments & Sukuk", "Base wealth: ${viewModel.formatCurrency(activeRecord.stocksEquities + activeRecord.mutualFunds)}", viewModel.formatCurrency((activeRecord.stocksEquities + activeRecord.mutualFunds) * 0.025))
                    DissectionRow("Immediate Liabilities", "Due debts & living expenses", "-${viewModel.formatCurrency(activeRecord.deductions)}", isDeduction = true)
                }

                // Cryptographic Proof Signature
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SurfaceContainerDefault,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.QrCode2, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
                            Column {
                                Text("SHA-256 HASH DIGEST", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = OnSurfaceDark, letterSpacing = 0.5.sp)
                                Text(
                                    text = activeRecord.hashDigest.take(24) + "...",
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = OnSurfaceVariantMuted
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(SurfaceContainerLowest)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("VALID", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(Icons.Default.Shield, contentDescription = null, tint = OnSurfaceVariantMuted, modifier = Modifier.size(14.dp))
                    Text(
                        text = "Zero telemetry verification. Generated entirely in-memory on this device. No financial data leaves your local offline vault.",
                        fontSize = 10.sp,
                        color = OnSurfaceVariantMuted,
                        lineHeight = 14.sp
                    )
                }
            }
        }

        // Action Buttons
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    showPdfExportDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("export_pdf_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary, contentColor = Color.White)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Download / Export PDF Statement", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        val cert = """
                            OFFICIAL ZAKAT CERTIFICATE
                            Ref: ${activeRecord.refNumber}
                            Profile: ${activeRecord.profileName}
                            Hijri: ${activeRecord.hijriYear}
                            Nisab Standard: ${activeRecord.nisabStandard}
                            Gold Karat: ${activeRecord.goldKarat}
                            Currency: ${activeRecord.currencyCode} (${activeRecord.currencySymbol})
                            Gross Assets: ${activeRecord.currencySymbol}${activeRecord.grossAssets}
                            Deductions: ${activeRecord.currencySymbol}${activeRecord.deductions}
                            Net Zakatable: ${activeRecord.currencySymbol}${activeRecord.netWealth}
                            Zakat Due (2.5%): ${activeRecord.currencySymbol}${activeRecord.zakatDue}
                            SHA-256 Digest: ${activeRecord.hashDigest}
                            Generated Offline on Device Local Ledger Vault
                        """.trimIndent()
                        PortabilityUtils.shareText(context, cert, "Zakat Certificate ${activeRecord.refNumber}")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("share_cert_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceContainerHigh, contentColor = EmeraldPrimary)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Text("Share Statement", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                Button(
                    onClick = {
                        val targetDate = System.currentTimeMillis() + (86400000L * 354)
                        PortabilityUtils.addHawlMilestoneToCalendar(
                            context = context,
                            hawlDateMillis = targetDate,
                            zakatDueFormatted = "${activeRecord.currencySymbol}${activeRecord.zakatDue}"
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("add_calendar_milestone_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceContainerHigh, contentColor = GoldSecondary)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Event, contentDescription = null, modifier = Modifier.size(16.dp))
                        Text("Hawl to Calendar", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.setCalcStep(5) },
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceContainerDefault, contentColor = EmeraldPrimary)
                ) {
                    Text("Back to Summary", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onNavigateToHistory,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("back_to_history_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceContainerDefault, contentColor = OnSurfaceDark)
                ) {
                    Text("View Ledger Vault", fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    if (showPdfExportDialog) {
        PdfExportDialog(
            record = activeRecord,
            onDismiss = { showPdfExportDialog = false }
        )
    }
}

// Helper Subcomponents
@Composable
private fun FinancialInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    helperText: String,
    placeholder: String,
    testTag: String,
    currencyCode: String = "PKR",
    currencySymbol: String = "₨",
    validator: (String) -> String? = { input ->
        val trimmed = input.trim()
        when {
            trimmed.isEmpty() -> null
            trimmed.contains("-") -> "Negative values are not permitted. Amount must be 0 or greater."
            trimmed.count { it == '.' } > 1 -> "Invalid decimal format. Only one decimal point allowed."
            trimmed.any { !it.isDigit() && it != '.' } -> "Only non-negative numbers are permitted (digits and decimal point)."
            trimmed.toDoubleOrNull() == null -> "Please enter a valid numerical amount."
            (trimmed.toDoubleOrNull() ?: 0.0) < 0.0 -> "Negative values are not permitted. Amount must be 0 or greater."
            (trimmed.toDoubleOrNull() ?: 0.0) > 1e14 -> "Amount exceeds maximum supported numerical limit."
            else -> null
        }
    }
) {
    val errorMessage = validator(value)
    val isError = errorMessage != null

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = SurfaceContainerLowest,
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = if (isError) ErrorRed else EmeraldPrimary)
                Text(text = currencyCode, fontSize = 11.sp, color = if (isError) ErrorRed else OnSurfaceVariantMuted)
            }

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                isError = isError,
                leadingIcon = {
                    Text(currencySymbol, fontWeight = FontWeight.Bold, color = if (isError) ErrorRed else OnSurfaceVariantMuted, modifier = Modifier.padding(start = 12.dp))
                },
                trailingIcon = {
                    if (isError) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Validation error",
                            tint = ErrorRed,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                },
                placeholder = { Text(placeholder) },
                supportingText = {
                    if (isError) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("${testTag}_error")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(12.dp))
                                Text(
                                    text = errorMessage ?: "",
                                    color = ErrorRed,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                if (value.contains("-")) {
                                    Text(
                                        text = "Make Positive",
                                        color = EmeraldPrimary,
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .clickable { onValueChange(value.replace("-", "").trim()) }
                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                                Text(
                                    text = "Set to 0",
                                    color = ErrorRed,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .clickable { onValueChange("0") }
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(imageVector = icon, contentDescription = null, tint = OnSurfaceVariantMuted, modifier = Modifier.size(13.dp))
                            Text(text = helperText, fontSize = 10.sp, color = OnSurfaceVariantMuted)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(testTag),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (isError) ErrorRed else EmeraldPrimary,
                    unfocusedBorderColor = if (isError) ErrorRed else OutlineBorder.copy(alpha = 0.4f),
                    errorBorderColor = ErrorRed,
                    focusedContainerColor = SurfaceContainerLowest,
                    unfocusedContainerColor = if (isError) ErrorContainerRed.copy(alpha = 0.15f) else SurfaceContainerLow,
                    errorContainerColor = ErrorContainerRed.copy(alpha = 0.15f)
                )
            )
        }
    }
}

@Composable
private fun StepCarouselPill(
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(100.dp),
        color = if (isActive) EmeraldPrimary else SurfaceContainerDefault,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
            color = if (isActive) Color.White else OnSurfaceVariantMuted,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun StepBreadcrumb(
    label: String,
    isDone: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(if (isDone) EmeraldPrimary else SurfaceContainerHigh)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Icon(
                imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.Info,
                contentDescription = null,
                tint = if (isDone) EmeraldPrimary else OnSurfaceVariantMuted,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDone) EmeraldPrimary else OnSurfaceVariantMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun GuidanceCalloutCard(title: String, text: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = SurfaceContainerLow,
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(SurfaceContainerDefault),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.AutoStories, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
            }
            Column {
                Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                Text(text, fontSize = 11.sp, color = OnSurfaceVariantMuted, lineHeight = 15.sp, modifier = Modifier.padding(top = 2.dp))
            }
        }
    }
}

@Composable
private fun FiqhRadioOption(
    title: String,
    badge: String?,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) SurfaceContainerLow else SurfaceContainerLowest,
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary) else null,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) EmeraldPrimary else SurfaceContainerHigh),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color.White))
                }
            }
            Column {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (isSelected) EmeraldPrimary else OnSurfaceDark)
                    if (badge != null) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (badge == "Included") EmeraldPrimary else SurfaceContainerHighest)
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = badge,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (badge == "Included") Color.White else OnSurfaceVariantMuted
                            )
                        }
                    }
                }
                Text(description, fontSize = 10.sp, color = OnSurfaceVariantMuted, lineHeight = 14.sp, modifier = Modifier.padding(top = 2.dp))
            }
        }
    }
}

@Composable
private fun QuickChip(text: String, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(100.dp),
        color = SurfaceContainerDefault,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = EmeraldPrimary,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun NisabSegmentButton(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) EmeraldPrimaryContainer else Color.Transparent,
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else OnSurfaceVariantMuted
            )
            Text(
                text = subtitle,
                fontSize = 9.sp,
                color = if (isSelected) EmeraldPrimaryFixedDim else OnSurfaceVariantMuted
            )
        }
    }
}

@Composable
private fun HoldingAccordionCard(
    title: String,
    subtitle: String,
    amountText: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
    isDeduction: Boolean = false,
    expandedContent: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = SurfaceContainerLowest,
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggle)
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isDeduction) ErrorContainerRed else SurfaceContainerDefault),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (isDeduction) ErrorRed else EmeraldPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (isDeduction) ErrorRed else OnSurfaceDark)
                        Text(subtitle, fontSize = 10.sp, color = OnSurfaceVariantMuted)
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(amountText, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (isDeduction) ErrorRed else EmeraldPrimary)
                    IconButton(onClick = onEdit, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = OnSurfaceVariantMuted, modifier = Modifier.size(16.dp))
                    }
                    Icon(
                        imageVector = Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = OnSurfaceVariantMuted,
                        modifier = Modifier.size(18.dp).rotate(if (isExpanded) 180f else 0f)
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceContainerLow)
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    expandedContent()
                }
            }
        }
    }
}

@Composable
private fun HoldingsItemRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 11.sp, color = OnSurfaceVariantMuted)
        Text(value, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = OnSurfaceDark)
    }
}

@Composable
private fun LedgerBreakdownRow(
    title: String,
    subtitle: String,
    amountText: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    amountColor: Color = EmeraldPrimary,
    isBold: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceContainerLow),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
            }
            Column {
                Text(title, fontSize = 12.sp, fontWeight = if (isBold) FontWeight.Bold else FontWeight.SemiBold, color = OnSurfaceDark)
                Text(subtitle, fontSize = 10.sp, color = OnSurfaceVariantMuted)
            }
        }

        Text(amountText, fontSize = if (isBold) 14.sp else 12.sp, fontWeight = FontWeight.Bold, color = amountColor)
    }
}

@Composable
private fun DissectionRow(
    title: String,
    subtitle: String,
    dueText: String,
    isDeduction: Boolean = false
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = SurfaceContainerLow,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(title, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = if (isDeduction) ErrorRed else OnSurfaceDark)
                Text(subtitle, fontSize = 9.sp, color = OnSurfaceVariantMuted)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(dueText, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isDeduction) ErrorRed else EmeraldPrimary)
                Text(if (isDeduction) "Deduction" else "2.5% rate", fontSize = 9.sp, color = OnSurfaceVariantMuted)
            }
        }
    }
}

@Composable
private fun PrivateNoticeFooter() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("Calculations are fully private & processed on your device only", fontSize = 10.sp, color = OnSurfaceVariantMuted)
    }
}
