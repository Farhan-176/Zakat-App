package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.data.model.Karat
import com.example.data.model.KaratRateItem
import com.example.data.model.KaratRateCalculator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GroundedNisabRates
import com.example.data.model.RateSyncStatus
import com.example.data.model.SyncInterval
import com.example.ui.ZakatViewModel
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldPrimaryContainer
import com.example.ui.theme.EmeraldPrimaryFixed
import com.example.ui.theme.EmeraldPrimaryFixedDim
import com.example.ui.theme.GoldOnSecondaryContainer
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.GoldSecondaryContainer
import com.example.ui.theme.OnSurfaceDark
import com.example.ui.theme.OnSurfaceVariantMuted
import com.example.ui.theme.OutlineBorder
import com.example.ui.theme.SurfaceContainerDefault
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.SurfaceMint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NisabGroundingDialog(
    viewModel: ZakatViewModel,
    onDismiss: () -> Unit,
    initialTab: Int = 0
) {
    val rateSyncStatus by viewModel.rateSyncStatus.collectAsState()
    val groundedRates by viewModel.groundedRates.collectAsState()
    val syncInterval by viewModel.syncInterval.collectAsState()
    val autoApply by viewModel.autoApplyRates.collectAsState()
    val selectedCurrency by viewModel.selectedCurrency.collectAsState()
    val nisabStandard by viewModel.nisabStandard.collectAsState()

    val netWealth = viewModel.getNetWealth()
    val currentRates: GroundedNisabRates = groundedRates ?: viewModel.rateService.createCalibratedFallbackRates(selectedCurrency)

    val silverNisab = currentRates.silverNisabThreshold
    val goldNisab = currentRates.goldNisabThreshold

    val activeNisabThreshold = if (nisabStandard == "Gold") goldNisab else silverNisab
    val isZakatDue = netWealth >= activeNisabThreshold && activeNisabThreshold > 0.0
    val wealthDiff = netWealth - activeNisabThreshold

    var showCitations by remember { mutableStateOf(false) }
    var selectedTab by remember(initialTab) { mutableStateOf(initialTab) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("dialog_nisab_grounding"),
        title = {
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
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(EmeraldPrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Nisab Live Verification",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceDark
                            )
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "Grounded with Google Search",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = EmeraldPrimary
                            )
                        }
                    }
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("btn_close_grounding_dialog")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = OnSurfaceVariantMuted
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 520.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Sync status banner / progress
                when (val status = rateSyncStatus) {
                    is RateSyncStatus.Fetching -> {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = SurfaceMint,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp,
                                        color = EmeraldPrimary
                                    )
                                    Text(
                                        text = status.message,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = EmeraldPrimary
                                    )
                                }
                                LinearProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(3.dp)
                                        .clip(RoundedCornerShape(100.dp)),
                                    color = EmeraldPrimary,
                                    trackColor = SurfaceContainerHigh
                                )
                            }
                        }
                    }
                    is RateSyncStatus.Error -> {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = SurfaceContainerLow,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = GoldSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Live Search Status",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OnSurfaceDark
                                    )
                                    Text(
                                        text = status.message,
                                        fontSize = 10.5.sp,
                                        color = OnSurfaceVariantMuted,
                                        lineHeight = 14.sp
                                    )
                                }
                            }
                        }
                    }
                    is RateSyncStatus.Success -> {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = SurfaceMint,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "Live spot rates verified via Google Search (${selectedCurrency.code})",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = EmeraldPrimary
                                )
                            }
                        }
                    }
                    RateSyncStatus.Idle -> {
                        // Idle state
                    }
                }

                // Segmented Tabs: 0: Spot Rates & Verdict, 1: Karat Matrix, 2: 30-Day Trend, 3: Sources
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(SurfaceContainerLow)
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    val tabs = listOf("Spot Rates", "Karat Matrix", "30d Trend", "Sources")
                    tabs.forEachIndexed { index, title ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (selectedTab == index) EmeraldPrimaryContainer else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedTab = index }
                                .testTag("tab_grounding_$index")
                        ) {
                            Text(
                                text = title,
                                fontSize = 10.5.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTab == index) Color.White else OnSurfaceDark,
                                modifier = Modifier.padding(vertical = 6.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                maxLines = 1
                            )
                        }
                    }
                }

                when (selectedTab) {
                    0 -> {
                        // Rates Cards (Gold & Silver)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Gold Card
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceContainerLowest,
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (nisabStandard == "Gold") 1.5.dp else 1.dp,
                            color = if (nisabStandard == "Gold") EmeraldPrimary else OutlineBorder.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.nisabStandard.value = "Gold" }
                            .padding(2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "24K Gold",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary
                                )
                                if (nisabStandard == "Gold") {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = null,
                                        tint = EmeraldPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                            Text(
                                text = "${selectedCurrency.symbol}${String.format(Locale.US, "%.2f", currentRates.goldPerGram)} /g",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceDark
                            )
                            HorizontalDivider(
                                color = OutlineBorder.copy(alpha = 0.2f),
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                            Text(
                                text = "Nisab (87.48g):",
                                fontSize = 9.5.sp,
                                color = OnSurfaceVariantMuted
                            )
                            Text(
                                text = viewModel.formatCurrency(goldNisab),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnSurfaceDark
                            )
                        }
                    }

                    // Silver Card
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceContainerLowest,
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (nisabStandard == "Silver") 1.5.dp else 1.dp,
                            color = if (nisabStandard == "Silver") EmeraldPrimary else OutlineBorder.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.nisabStandard.value = "Silver" }
                            .padding(2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Silver",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary
                                )
                                if (nisabStandard == "Silver") {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = null,
                                        tint = EmeraldPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                            Text(
                                text = "${selectedCurrency.symbol}${String.format(Locale.US, "%.2f", currentRates.silverPerGram)} /g",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceDark
                            )
                            HorizontalDivider(
                                color = OutlineBorder.copy(alpha = 0.2f),
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                            Text(
                                text = "Nisab (612.36g):",
                                fontSize = 9.5.sp,
                                color = OnSurfaceVariantMuted
                            )
                            Text(
                                text = viewModel.formatCurrency(silverNisab),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnSurfaceDark
                            )
                        }
                    }
                }

                // Verification Verdict Card
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isZakatDue) EmeraldPrimaryContainer else SurfaceContainerLow,
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
                            Text(
                                text = "THRESHOLD VERIFICATION VERDICT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isZakatDue) EmeraldPrimaryFixed else OnSurfaceVariantMuted,
                                letterSpacing = 0.6.sp
                            )
                            Text(
                                text = "$nisabStandard Standard",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isZakatDue) GoldSecondary else EmeraldPrimary
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (isZakatDue) "Nisab Reached (Zakat Due)" else "Below Nisab Threshold",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isZakatDue) Color.White else OnSurfaceDark
                                )
                                Text(
                                    text = if (isZakatDue) {
                                        "Net Wealth exceeds $nisabStandard Nisab by +${viewModel.formatCurrency(wealthDiff)}"
                                    } else {
                                        "Shortfall of -${viewModel.formatCurrency(-wealthDiff)} to reach $nisabStandard Nisab"
                                    },
                                    fontSize = 11.sp,
                                    color = if (isZakatDue) EmeraldPrimaryFixedDim else OnSurfaceVariantMuted
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Your Hawl Net Wealth: ${viewModel.formatCurrency(netWealth)}",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isZakatDue) Color.White.copy(alpha = 0.9f) else OnSurfaceDark
                            )
                            Text(
                                text = "Benchmark: ${viewModel.formatCurrency(activeNisabThreshold)}",
                                fontSize = 10.5.sp,
                                color = if (isZakatDue) EmeraldPrimaryFixedDim else OnSurfaceVariantMuted
                            )
                        }
                    }
                }

                // Periodic Refresh Settings
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceContainerLowest,
                    border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Periodic Search Synchronization",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceDark
                        )

                        // Interval selector
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            SyncInterval.entries.forEach { interval ->
                                val isSelected = interval == syncInterval
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) EmeraldPrimaryContainer else SurfaceContainerLow,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { viewModel.setSyncInterval(interval) }
                                        .testTag("interval_${interval.name}")
                                ) {
                                    Text(
                                        text = interval.label,
                                        fontSize = 10.5.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) Color.White else OnSurfaceDark,
                                        modifier = Modifier.padding(vertical = 6.dp),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        }

                        // Auto Apply Switch
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Auto-apply to calculation flow",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OnSurfaceDark
                                )
                                Text(
                                    text = "Automatically synchronizes active inputs with grounded market spot rates",
                                    fontSize = 9.5.sp,
                                    color = OnSurfaceVariantMuted
                                )
                            }
                            Switch(
                                checked = autoApply,
                                onCheckedChange = { viewModel.setAutoApplyRates(it) },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = EmeraldPrimary),
                                modifier = Modifier.testTag("toggle_auto_apply_rates")
                            )
                        }
                    }
                }
                    }

                    1 -> {
                        // TAB 1: Karat Matrix
                        KaratMatrixDialogContent(
                            viewModel = viewModel,
                            goldRate24k = currentRates.goldPerGram,
                            silverRate = currentRates.silverPerGram,
                            currencySymbol = selectedCurrency.symbol
                        )
                    }

                    2 -> {
                        // TAB 2: 30-Day Trend Trajectory
                        NisabTrendChartContent(
                            viewModel = viewModel,
                            goldRate = currentRates.goldPerGram,
                            silverRate = currentRates.silverPerGram,
                            currencySymbol = selectedCurrency.symbol
                        )
                    }

                    3 -> {
                        // TAB 3: Google Search Grounding Sources & Query Citations
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SurfaceContainerLowest,
                            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Google Search Verification Details",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnSurfaceDark
                                )
                                Text(
                                    text = "As of: ${currentRates.asOfText}",
                                    fontSize = 10.5.sp,
                                    color = OnSurfaceVariantMuted
                                )
                                Text(
                                    text = "Primary Benchmark: ${currentRates.sourceName}",
                                    fontSize = 10.5.sp,
                                    color = OnSurfaceDark,
                                    fontWeight = FontWeight.SemiBold
                                )

                                if (currentRates.searchQueries.isNotEmpty()) {
                                    Text(
                                        text = "Search Queries Executed:",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OnSurfaceDark
                                    )
                                    currentRates.searchQueries.forEach { query ->
                                        Text(
                                            text = "• \"$query\"",
                                            fontSize = 9.5.sp,
                                            color = OnSurfaceVariantMuted,
                                            modifier = Modifier.padding(start = 4.dp)
                                        )
                                    }
                                }

                                if (currentRates.sources.isNotEmpty()) {
                                    Text(
                                        text = "Verified Sources (${currentRates.sources.size}):",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OnSurfaceDark
                                    )
                                    currentRates.sources.forEach { src ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                                            modifier = Modifier.padding(vertical = 2.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Link,
                                                contentDescription = null,
                                                tint = EmeraldPrimary,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Text(
                                                text = src.title,
                                                fontSize = 10.sp,
                                                color = EmeraldPrimary,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                } else {
                                    Text(
                                        text = "Calibrated using multi-source bullion exchange indices.",
                                        fontSize = 10.sp,
                                        color = OnSurfaceVariantMuted
                                    )
                                }
                            }
                        }
                    }
                }

                // Action Buttons inside body
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.fetchLiveGroundedRates(force = true) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_refresh_grounded_rates"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = EmeraldPrimary
                            )
                            Text(
                                text = "Sync Now",
                                fontSize = 11.5.sp,
                                color = EmeraldPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Button(
                        onClick = {
                            viewModel.applyGroundedRatesToCalculator(notify = true)
                            onDismiss()
                        },
                        modifier = Modifier
                            .weight(1.3f)
                            .testTag("btn_apply_grounded_rates"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                    ) {
                        Text(
                            text = "Apply to Calculator",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("btn_dialog_close")
            ) {
                Text(
                    text = "Close",
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary
                )
            }
        },
        containerColor = SurfaceContainerLowest,
        shape = RoundedCornerShape(18.dp)
    )
}

@Composable
fun KaratMatrixDialogContent(
    viewModel: ZakatViewModel,
    goldRate24k: Double,
    silverRate: Double,
    currencySymbol: String
) {
    val selectedKarat by viewModel.selectedGoldKarat.collectAsState()
    val matrix = remember(goldRate24k, currencySymbol) {
        KaratRateCalculator.calculateMatrix(goldRate24k, currencySymbol)
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = SurfaceMint,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Derived from 24K spot price of $currencySymbol${String.format(Locale.US, "%,.2f", goldRate24k)}/g. Tap any karat to select it for your calculation flow.",
                    fontSize = 10.5.sp,
                    color = EmeraldPrimary,
                    lineHeight = 14.sp
                )
            }
        }

        matrix.forEach { item ->
            val isSelected = item.karat == selectedKarat
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (isSelected) EmeraldPrimaryContainer else SurfaceContainerLow,
                border = androidx.compose.foundation.BorderStroke(
                    width = if (isSelected) 1.5.dp else 1.dp,
                    color = if (isSelected) EmeraldPrimary else OutlineBorder.copy(alpha = 0.25f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.selectedGoldKarat.value = item.karat }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = item.karat.label,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (isSelected) Color.White else OnSurfaceDark
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (isSelected) Color.White.copy(alpha = 0.2f) else SurfaceContainerHigh)
                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "${item.purityPercent}% Pure",
                                    fontSize = 9.sp,
                                    color = if (isSelected) Color.White else OnSurfaceVariantMuted
                                )
                            }
                        }
                        Text(
                            text = "Tola (~11.66g): ${item.formattedPerTola}",
                            fontSize = 10.sp,
                            color = if (isSelected) EmeraldPrimaryFixedDim else OnSurfaceVariantMuted
                        )
                        Text(
                            text = "Troy Oz (~31.10g): ${item.formattedPerOunce}",
                            fontSize = 9.5.sp,
                            color = if (isSelected) EmeraldPrimaryFixedDim else OnSurfaceVariantMuted
                        )
                    }

                    Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "${item.formattedPerGram}/g",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else EmeraldPrimary
                        )
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(100.dp))
                                    .background(Color.White.copy(alpha = 0.25f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Active in Calc ✓",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        } else {
                            Text(
                                text = "Tap to use",
                                fontSize = 9.5.sp,
                                color = OnSurfaceVariantMuted
                            )
                        }
                    }
                }
            }
        }

        // Fine Silver Reference
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = SurfaceContainerLow,
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder.copy(alpha = 0.25f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Pure Fine Silver (.999)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = OnSurfaceDark
                    )
                    Text(
                        text = "Tola (~11.66g): $currencySymbol${String.format(Locale.US, "%,.2f", silverRate * 11.664)}",
                        fontSize = 10.sp,
                        color = OnSurfaceVariantMuted
                    )
                }
                Text(
                    text = "$currencySymbol${String.format(Locale.US, "%,.2f", silverRate)}/g",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary
                )
            }
        }
    }
}

@Composable
fun NisabTrendChartContent(
    viewModel: ZakatViewModel,
    goldRate: Double,
    silverRate: Double,
    currencySymbol: String
) {
    val trendPoints = remember(goldRate, silverRate) {
        viewModel.get30DayNisabTrend()
    }
    val minGold = trendPoints.minOfOrNull { it.goldRate } ?: goldRate
    val maxGold = trendPoints.maxOfOrNull { it.goldRate } ?: goldRate
    val minSilver = trendPoints.minOfOrNull { it.silverRate } ?: silverRate
    val maxSilver = trendPoints.maxOfOrNull { it.silverRate } ?: silverRate

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "30-Day Market Trajectory",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = OnSurfaceDark
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(GoldSecondary))
                    Text("Gold (24K)", fontSize = 10.sp, color = OnSurfaceVariantMuted)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(EmeraldPrimary))
                    Text("Silver", fontSize = 10.sp, color = OnSurfaceVariantMuted)
                }
            }
        }

        // Canvas Trajectory Chart
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = SurfaceContainerLowest,
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder.copy(alpha = 0.25f)),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp)
            ) {
                if (trendPoints.isEmpty()) return@Canvas

                val width = size.width
                val height = size.height

                // Draw Gold Line
                val goldPath = Path()
                val goldRange = (maxGold - minGold).coerceAtLeast(0.01)
                trendPoints.forEachIndexed { index, pt ->
                    val x = (index.toFloat() / (trendPoints.size - 1).toFloat()) * width
                    val normalizedY = ((pt.goldRate - minGold) / goldRange).toFloat()
                    val y = height - (normalizedY * (height - 16.dp.toPx())) - 8.dp.toPx()
                    if (index == 0) goldPath.moveTo(x, y) else goldPath.lineTo(x, y)
                }
                drawPath(
                    path = goldPath,
                    color = GoldSecondary,
                    style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                )

                // Draw Silver Line
                val silverPath = Path()
                val silverRange = (maxSilver - minSilver).coerceAtLeast(0.01)
                trendPoints.forEachIndexed { index, pt ->
                    val x = (index.toFloat() / (trendPoints.size - 1).toFloat()) * width
                    val normalizedY = ((pt.silverRate - minSilver) / silverRange).toFloat()
                    val y = height - (normalizedY * (height - 16.dp.toPx())) - 8.dp.toPx()
                    if (index == 0) silverPath.moveTo(x, y) else silverPath.lineTo(x, y)
                }
                drawPath(
                    path = silverPath,
                    color = EmeraldPrimary,
                    style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                )
            }
        }

        // Range Stat Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = SurfaceContainerLow,
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("30d Gold 24K Range", fontSize = 9.5.sp, color = OnSurfaceVariantMuted)
                    Text(
                        text = "$currencySymbol${String.format(Locale.US, "%,.1f", minGold)} - $currencySymbol${String.format(Locale.US, "%,.1f", maxGold)}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldSecondary
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = SurfaceContainerLow,
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("30d Silver Range", fontSize = 9.5.sp, color = OnSurfaceVariantMuted)
                    Text(
                        text = "$currencySymbol${String.format(Locale.US, "%,.2f", minSilver)} - $currencySymbol${String.format(Locale.US, "%,.2f", maxSilver)}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary
                    )
                }
            }
        }

        Text(
            text = "Historic 30-day moving trajectories are calibrated using historical volatility indexes to assist Hawl benchmark decisions.",
            fontSize = 9.5.sp,
            color = OnSurfaceVariantMuted,
            lineHeight = 13.sp
        )
    }
}
