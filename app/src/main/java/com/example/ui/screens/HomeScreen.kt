package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.AppTab
import com.example.ui.ZakatViewModel
import com.example.ui.components.ZakatBrandLogo
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldPrimaryContainer
import com.example.ui.theme.EmeraldPrimaryFixedDim
import com.example.ui.theme.GoldOnSecondaryContainer
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.GoldSecondaryContainer
import com.example.ui.theme.OnSurfaceDark
import com.example.ui.theme.OnSurfaceVariantMuted
import com.example.ui.theme.OutlineBorder
import com.example.ui.theme.SurfaceContainerDefault
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerHighest
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.SurfaceMint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: ZakatViewModel,
    onNavigateToCalculation: () -> Unit,
    onNavigateToHistory: () -> Unit = { viewModel.selectTab(AppTab.HISTORY) },
    onNavigateToGuidance: () -> Unit = { viewModel.selectTab(AppTab.GUIDANCE) },
    onNavigateToAssets: () -> Unit = { viewModel.selectTab(AppTab.ASSETS) }
) {
    val language by viewModel.language.collectAsState()
    val isUrdu = language == "ur"

    val userName by viewModel.currentUserName.collectAsState()
    val isGuest by viewModel.isGuestUser.collectAsState()
    val activeProfile by viewModel.activeProfile.collectAsState()
    val selectedCurrency by viewModel.selectedCurrency.collectAsState()
    val savedRecords by viewModel.savedRecords.collectAsState()
    val groundedRates by viewModel.groundedRates.collectAsState()
    val rateSyncStatus by viewModel.rateSyncStatus.collectAsState()

    val goldRate = viewModel.getGoldRate()
    val silverRate = viewModel.getSilverRate()
    val goldNisab = selectedCurrency.getGoldNisabFloor(goldRate)
    val silverNisab = selectedCurrency.getSilverNisabFloor(silverRate)
    val nisabStandard by viewModel.nisabStandard.collectAsState()

    val daysElapsed = viewModel.getHawlDaysElapsed()
    val daysRemaining = viewModel.getHawlDaysRemaining()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceMint)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. User Welcome & Profile Header Banner
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceContainerLowest,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ZakatBrandLogo(size = 46.dp)

                    Column {
                        Text(
                            text = if (isUrdu) {
                                if (isGuest) "السلام علیکم، مہمان صارف" else "السلام علیکم، $userName"
                            } else {
                                if (isGuest) "Assalamu Alaikum, Guest" else "Assalamu Alaikum, $userName"
                            },
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary,
                                fontSize = 16.sp
                            )
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SurfaceContainerHigh)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = activeProfile.name,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary
                                )
                            }
                            Text(
                                text = "• ${selectedCurrency.code} (${selectedCurrency.symbol})",
                                fontSize = 11.sp,
                                color = OnSurfaceVariantMuted
                            )
                        }
                    }
                }

                // Privacy Indicator Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(SurfaceContainerLow)
                        .clickable { viewModel.showPrivacyDialog.value = true }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(12.dp))
                        Text(
                            text = if (isUrdu) "آف لائن" else "Offline",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        )
                    }
                }
            }
        }

        // 2. Primary Hero Card: "Calculate Zakat"
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = EmeraldPrimaryContainer,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isUrdu) "سالانہ زکوٰۃ کا حساب کریں" else "Annual Zakat Assessment",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 20.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isUrdu)
                                "نقد رقم، سونا، چاندی، حصص اور تجارتی مال پر 2.5% واجب الادا زکوٰۃ کا شفاف حساب۔"
                            else
                                "Accurately compute 2.5% Zakat across Cash, Gold, Silver, Stocks, and Business goods.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = EmeraldPrimaryFixedDim,
                                fontSize = 12.5.sp,
                                lineHeight = 17.sp
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Calculate,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                // Quick Badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White.copy(alpha = 0.12f),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = if (isUrdu) "نقد و بینک" else "Cash & Bank",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White.copy(alpha = 0.12f),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = if (isUrdu) "سونا و چاندی" else "Gold & Silver",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White.copy(alpha = 0.12f),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = if (isUrdu) "قرض کٹوتی" else "Deduct Debts",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }
                }

                // Action Button
                Button(
                    onClick = onNavigateToCalculation,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("home_start_calculation_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldSecondary,
                        contentColor = OnSurfaceDark
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (isUrdu) "حساب کا آغاز کریں" else "Start Hawl Calculation",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.5.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // 3. Live Nisab Benchmarks Card
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
                            Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = if (isUrdu) "موجودہ نصاب کی حدیں" else "Current Nisab Thresholds",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary,
                                fontSize = 14.sp
                            )
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Quick Sync Button
                        IconButton(
                            onClick = { viewModel.fetchLiveGroundedRates(force = true) },
                            modifier = Modifier
                                .size(28.dp)
                                .testTag("btn_home_sync_rates")
                        ) {
                            if (rateSyncStatus is com.example.data.model.RateSyncStatus.Fetching) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(14.dp),
                                    strokeWidth = 2.dp,
                                    color = EmeraldPrimary
                                )
                            } else {
                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = "Sync spot rates via Google Search",
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(SurfaceContainerLow)
                                .clickable { viewModel.showCurrencyDialog.value = true }
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${selectedCurrency.code} • Rates",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary
                            )
                        }
                    }
                }

                // Google Search Grounding Badge & Spot Rate Indicator
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SurfaceMint,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.showNisabVerificationDialog.value = true }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = if (groundedRates?.isGrounded == true) "Grounded via Google Search" else "Google Search Grounded",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary
                            )
                        }
                        Text(
                            text = "Gold: ${selectedCurrency.symbol}${goldRate.toInt()}/g • Silver: ${selectedCurrency.symbol}${silverRate.toInt()}/g",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = OnSurfaceDark
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Silver Nisab
                    val isSilverActive = nisabStandard == "Silver"
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSilverActive) SurfaceContainerLow else SurfaceContainerDefault,
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = if (isSilverActive) 1.5.dp else 0.dp,
                                color = if (isSilverActive) EmeraldPrimary else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { viewModel.nisabStandard.value = "Silver" }
                            .padding(10.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isUrdu) "چاندی کا نصاب" else "Silver Nisab",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary
                                )
                                if (isSilverActive) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(14.dp))
                                }
                            }
                            Text(
                                text = "612.36g (~52.5 Tola)",
                                fontSize = 10.sp,
                                color = OnSurfaceVariantMuted
                            )
                            Text(
                                text = viewModel.formatCurrency(silverNisab),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceDark
                            )
                            Text(
                                text = if (isUrdu) "فقہ حنفی کے مطابق مستحب" else "Consensus for poor benefit",
                                fontSize = 9.sp,
                                color = OnSurfaceVariantMuted
                            )
                        }
                    }

                    // Gold Nisab
                    val isGoldActive = nisabStandard == "Gold"
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isGoldActive) SurfaceContainerLow else SurfaceContainerDefault,
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = if (isGoldActive) 1.5.dp else 0.dp,
                                color = if (isGoldActive) EmeraldPrimary else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { viewModel.nisabStandard.value = "Gold" }
                            .padding(10.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isUrdu) "سونے کا نصاب" else "Gold Nisab",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary
                                )
                                if (isGoldActive) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(14.dp))
                                }
                            }
                            Text(
                                text = "87.48g (~7.5 Tola)",
                                fontSize = 10.sp,
                                color = OnSurfaceVariantMuted
                            )
                            Text(
                                text = viewModel.formatCurrency(goldNisab),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceDark
                            )
                            Text(
                                text = if (isUrdu) "شوافع و حنابلہ ترجیح" else "Pure metals baseline",
                                fontSize = 9.sp,
                                color = OnSurfaceVariantMuted
                            )
                        }
                    }
                }

                // Dynamic Personal Nisab Status for the User
                val nisabEval = viewModel.getNisabEvaluation()
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (nisabEval.isNisabMet) SurfaceMint else SurfaceContainerLow,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (nisabEval.isNisabMet) "✅ Nisab Threshold Met" else if (nisabEval.userNetWealth > 0.0) "ℹ️ Below Nisab Threshold" else "📊 Personal Nisab Status",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (nisabEval.isNisabMet) EmeraldPrimary else OnSurfaceDark
                            )
                            Text(
                                text = "${nisabEval.selectedStandard} Standard",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnSurfaceVariantMuted
                            )
                        }

                        if (nisabEval.userNetWealth > 0.0) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Your Net Zakatable Wealth:",
                                    fontSize = 11.sp,
                                    color = OnSurfaceVariantMuted
                                )
                                Text(
                                    text = viewModel.formatCurrency(nisabEval.userNetWealth),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnSurfaceDark
                                )
                            }
                            val progress = (nisabEval.nisabPercentage.toFloat()).coerceIn(0f, 1f)
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(5.dp)
                                    .clip(RoundedCornerShape(100.dp)),
                                color = if (nisabEval.isNisabMet) EmeraldPrimary else GoldSecondary,
                                trackColor = SurfaceContainerHigh
                            )
                            Text(
                                text = if (nisabEval.isNisabMet)
                                    "Zakat is obligatory: ${viewModel.formatCurrency(nisabEval.zakatDue)} due (2.5% of net wealth)."
                                else
                                    "No Zakat due currently. You are ${viewModel.formatCurrency(-nisabEval.surplusOrShortfall)} below the threshold.",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (nisabEval.isNisabMet) EmeraldPrimary else OnSurfaceVariantMuted
                            )
                        } else {
                            Text(
                                text = "Start your Hawl calculation to compare your personal assets against this live ${nisabEval.selectedStandard.lowercase()} Nisab benchmark.",
                                fontSize = 10.5.sp,
                                color = OnSurfaceVariantMuted
                            )
                        }
                    }
                }

                // Action Buttons: Fetch Live Spot Prices & Verify Nisab
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.fetchLiveGroundedRates(force = true) },
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                            .testTag("btn_home_fetch_rates"),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            if (rateSyncStatus is com.example.data.model.RateSyncStatus.Fetching) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(14.dp),
                                    strokeWidth = 2.dp,
                                    color = EmeraldPrimary
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(
                                text = if (isUrdu) "تازہ ترین قیمتیں" else "Fetch Current Rates",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = EmeraldPrimary
                            )
                        }
                    }

                    Button(
                        onClick = { viewModel.showNisabVerificationDialog.value = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                            .testTag("btn_home_verify_nisab"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SurfaceMint,
                            contentColor = EmeraldPrimary
                        ),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = if (isUrdu) "نصاب کی تصدیق" else "Verify Nisab",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary
                            )
                        }
                    }
                }
            }
        }

        // 4. Quick Action Tiles (2x2 Grid)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Ledger Vault Tile
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = SurfaceContainerLowest,
                shadowElevation = 1.dp,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigateToHistory() }
                    .testTag("home_tile_vault")
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceContainerLow),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.History, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
                    }
                    Text(
                        text = if (isUrdu) "لیجر والٹ" else "Ledger Vault",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = OnSurfaceDark
                    )
                    Text(
                        text = if (isUrdu) "${savedRecords.size} سابقہ ریکارڈز" else "${savedRecords.size} Saved Audits",
                        fontSize = 11.sp,
                        color = OnSurfaceVariantMuted
                    )
                }
            }

            // Fiqh Guide Tile
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = SurfaceContainerLowest,
                shadowElevation = 1.dp,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigateToGuidance() }
                    .testTag("home_tile_guidance")
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceContainerLow),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.MenuBook, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
                    }
                    Text(
                        text = if (isUrdu) "فقہی رہنمائی" else "Fiqh Guidance",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = OnSurfaceDark
                    )
                    Text(
                        text = if (isUrdu) "8 قرآنی اصناف و احکام" else "8 Asnaf & Rulings",
                        fontSize = 11.sp,
                        color = OnSurfaceVariantMuted
                    )
                }
            }
        }

        // 5. Hawl Progress & Lunar Calendar Indicator
        val hawlState by viewModel.hawlCycleState.collectAsState()
        val selectedMilestone by viewModel.selectedHijriMilestone.collectAsState()

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceContainerLowest,
            shadowElevation = 1.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder.copy(alpha = 0.2f)),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.showHawlTrackerDialog.value = true }
                .testTag("home_hawl_status_card")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(EmeraldPrimaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
                        }
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = if (isUrdu) "حول (قمری سال) کی تکمیل" else "Hawl (Lunar Cycle) Status",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = OnSurfaceDark
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(SurfaceMint)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = selectedMilestone.hijriDateText,
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldPrimary
                                    )
                                }
                            }
                            Text(
                                text = if (isUrdu) "$daysRemaining دن باقی ہیں • 354 دن کل مدت" else "${hawlState.daysRemainingInCycle} days until completion • Day ${hawlState.currentCycleDay}/354",
                                fontSize = 11.sp,
                                color = OnSurfaceVariantMuted
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(if (hawlState.isCycleDueNow) GoldSecondaryContainer else SurfaceContainerLow)
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = if (hawlState.isCycleDueNow) "DUE" else "${(hawlState.progressFraction * 100).toInt()}%",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (hawlState.isCycleDueNow) GoldSecondary else EmeraldPrimary
                        )
                    }
                }

                LinearProgressIndicator(
                    progress = { hawlState.progressFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = EmeraldPrimary,
                    trackColor = SurfaceContainerHigh
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Due: ${hawlState.formattedDueDate}",
                        fontSize = 10.5.sp,
                        color = OnSurfaceVariantMuted
                    )
                    Text(
                        text = "Tap to manage & sync calendar →",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = EmeraldPrimary
                    )
                }
            }
        }

        // 5b. Asset Groups & Hotlinked Images Vault
        val assetGroups by viewModel.assetGroups.collectAsState()
        val totalAssetImages = assetGroups.sumOf { it.imageUrls.size }

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceContainerLowest,
            shadowElevation = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("home_asset_vault_card")
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
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(EmeraldPrimaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Collections,
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column {
                            Text(
                                text = if (isUrdu) "اثاثہ جات اور تصدیقی تصاویر" else "Asset Groups & Hotlinks",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp,
                                color = OnSurfaceDark
                            )
                            Text(
                                text = "Room SQLite • ${assetGroups.size} groups • $totalAssetImages hotlinked images",
                                fontSize = 11.sp,
                                color = EmeraldPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    TextButton(
                        onClick = onNavigateToAssets,
                        modifier = Modifier.testTag("home_view_all_assets_button")
                    ) {
                        Text("Open Vault", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(14.dp))
                    }
                }

                // Horizontal preview of recent group cover images
                if (assetGroups.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val previewGroups = assetGroups.take(3)
                        previewGroups.forEach { group ->
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable(onClick = onNavigateToAssets),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLow),
                                border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
                            ) {
                                Column {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(55.dp)
                                    ) {
                                        if (group.effectiveCoverUrl.isNotBlank()) {
                                            AsyncImage(
                                                model = group.effectiveCoverUrl,
                                                contentDescription = group.name,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        } else {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .background(SurfaceContainerHigh),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(Icons.Default.Collections, contentDescription = null, tint = OnSurfaceVariantMuted)
                                            }
                                        }
                                    }
                                    Text(
                                        text = group.name,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                                        color = OnSurfaceDark
                                    )
                                }
                            }
                        }
                    }
                } else {
                    OutlinedButton(
                        onClick = { viewModel.showCreateAssetGroupDialog.value = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Create your first asset group")
                    }
                }
            }
        }

        // 6. Recent Saved Calculation Summary
        if (savedRecords.isNotEmpty()) {
            val latest = savedRecords.first()
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.US)
            val dateStr = sdf.format(Date(latest.timestamp))

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
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isUrdu) "حالیہ تصدیق شدہ زکوٰۃ" else "Latest Recorded Calculation",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary
                            )
                        )
                        Text(
                            text = dateStr,
                            fontSize = 11.sp,
                            color = OnSurfaceVariantMuted
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Net Wealth: ${latest.currencySymbol} ${viewModel.formatNumber(latest.netWealth)}",
                                fontSize = 12.sp,
                                color = OnSurfaceVariantMuted
                            )
                            Text(
                                text = "Zakat Due: ${latest.currencySymbol} ${viewModel.formatNumber(latest.zakatDue)}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary
                            )
                        }

                        Button(
                            onClick = { viewModel.viewStatementForRecord(latest) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SurfaceContainerHigh,
                                contentColor = EmeraldPrimary
                            )
                        ) {
                            Text(if (isUrdu) "بیان دیکھیں" else "Statement", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 7. Quick Language Bar & Settings Shortcut
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = SurfaceContainerLow,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.Translate, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
                    Text(
                        text = if (isUrdu) "زبان:" else "Language:",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    TextButton(
                        onClick = { viewModel.setLanguage("en") },
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(
                            text = "English",
                            fontWeight = if (!isUrdu) FontWeight.Bold else FontWeight.Normal,
                            color = if (!isUrdu) EmeraldPrimary else OnSurfaceVariantMuted,
                            fontSize = 11.5.sp
                        )
                    }
                    TextButton(
                        onClick = { viewModel.setLanguage("ur") },
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(
                            text = "اردو",
                            fontWeight = if (isUrdu) FontWeight.Bold else FontWeight.Normal,
                            color = if (isUrdu) EmeraldPrimary else OnSurfaceVariantMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}
