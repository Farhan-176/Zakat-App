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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.HawlContinuityRuling
import com.example.data.model.HijriMilestone
import com.example.ui.ZakatViewModel
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldPrimaryContainer
import com.example.ui.theme.EmeraldPrimaryFixed
import com.example.ui.theme.EmeraldPrimaryFixedDim
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.GoldSecondaryContainer
import com.example.ui.theme.OnSurfaceDark
import com.example.ui.theme.OnSurfaceVariantMuted
import com.example.ui.theme.OutlineBorder
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.SurfaceMint
import com.example.util.PortabilityUtils

@Composable
fun HawlTrackerDialog(
    viewModel: ZakatViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val hawlState by viewModel.hawlCycleState.collectAsState()
    val selectedMilestone by viewModel.selectedHijriMilestone.collectAsState()
    val selectedRuling by viewModel.selectedHawlRuling.collectAsState()
    val wealthDipped by viewModel.wealthDippedBelowNisab.collectAsState()
    val selectedCurrency by viewModel.selectedCurrency.collectAsState()

    var showRulingExplanation by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("dialog_hawl_tracker"),
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
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Hawl (Lunar Year) Cycle",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceDark
                            )
                        )
                        Text(
                            text = "354-Day Islamic Hawl Anniversary Tracker",
                            fontSize = 10.5.sp,
                            color = EmeraldPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
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
                    .heightIn(max = 540.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 1. Lunar Cycle Progress Card
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = EmeraldPrimaryContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "LUNAR CYCLE PROGRESS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimaryFixed,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "${(hawlState.progressFraction * 100).toInt()}% Elapsed",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldSecondary
                            )
                        }

                        LinearProgressIndicator(
                            progress = { hawlState.progressFraction },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = GoldSecondary,
                            trackColor = Color.White.copy(alpha = 0.2f)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Current Day",
                                    fontSize = 10.sp,
                                    color = EmeraldPrimaryFixedDim
                                )
                                Text(
                                    text = "Day ${hawlState.currentCycleDay} of 354",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Hawl Completion Due",
                                    fontSize = 10.sp,
                                    color = EmeraldPrimaryFixedDim
                                )
                                Text(
                                    text = if (hawlState.isCycleDueNow) "DUE NOW!" else "${hawlState.daysRemainingInCycle} days remaining",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (hawlState.isCycleDueNow) GoldSecondary else Color.White
                                )
                            }
                        }

                        Text(
                            text = "Projected Due Date: ${hawlState.formattedDueDate}",
                            fontSize = 10.5.sp,
                            color = EmeraldPrimaryFixed
                        )
                    }
                }

                // 2. Select Hijri Milestone Pre-set
                Text(
                    text = "Islamic Milestone Anniversary:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurfaceDark
                )

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    HijriMilestone.entries.forEach { milestone ->
                        val isSelected = milestone == selectedMilestone
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) SurfaceMint else SurfaceContainerLow,
                            border = androidx.compose.foundation.BorderStroke(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) EmeraldPrimary else OutlineBorder.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setHijriMilestone(milestone)
                                    if (milestone != HijriMilestone.CUSTOM) {
                                        // Set approximate start date relative to lunar offset
                                        val dayOffset = (354 - milestone.approximateDayOfYear) % 354
                                        val estimatedStart = System.currentTimeMillis() - (dayOffset * 86_400_000L)
                                        viewModel.setHawlStartDate(estimatedStart)
                                    }
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = milestone.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = if (isSelected) EmeraldPrimary else OnSurfaceDark
                                        )
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(SurfaceContainerHigh)
                                                .padding(horizontal = 5.dp, vertical = 1.dp)
                                        ) {
                                            Text(
                                                text = milestone.hijriDateText,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = OnSurfaceVariantMuted
                                            )
                                        }
                                    }
                                    Text(
                                        text = milestone.significance,
                                        fontSize = 10.sp,
                                        color = OnSurfaceVariantMuted,
                                        lineHeight = 13.sp
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = EmeraldPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // 3. Fiqh Hawl Continuity Standard
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Fiqh Hawl Continuity Ruling:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceDark
                    )
                    IconButton(
                        onClick = { showRulingExplanation = !showRulingExplanation },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = "Ruling Info",
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HawlContinuityRuling.entries.forEach { ruling ->
                        val isSelected = ruling == selectedRuling
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) EmeraldPrimaryContainer else SurfaceContainerLow,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.setHawlRuling(ruling) }
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = ruling.madhabName,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else OnSurfaceDark
                                )
                                Text(
                                    text = ruling.summary,
                                    fontSize = 9.5.sp,
                                    color = if (isSelected) EmeraldPrimaryFixedDim else OnSurfaceVariantMuted,
                                    lineHeight = 12.sp
                                )
                            }
                        }
                    }
                }

                AnimatedVisibility(visible = showRulingExplanation) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = SurfaceMint,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = selectedRuling.detailedRuling,
                                fontSize = 10.5.sp,
                                color = OnSurfaceDark,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }

                // 4. Intermediate Wealth Drop Below Nisab
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (wealthDipped && selectedRuling == HawlContinuityRuling.SHAFI_CONTINUOUS) {
                        SurfaceContainerLow
                    } else {
                        SurfaceContainerLowest
                    },
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (wealthDipped) ErrorRed.copy(alpha = 0.5f) else OutlineBorder.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Did net wealth drop below Nisab during the year?",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.sp,
                                    color = OnSurfaceDark
                                )
                                Text(
                                    text = "Checks whether mid-year financial dips interrupt your Hawl cycle",
                                    fontSize = 9.5.sp,
                                    color = OnSurfaceVariantMuted
                                )
                            }
                            Switch(
                                checked = wealthDipped,
                                onCheckedChange = { viewModel.setWealthDippedBelowNisab(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = ErrorRed
                                )
                            )
                        }

                        if (wealthDipped) {
                            HorizontalDivider(
                                color = OutlineBorder.copy(alpha = 0.2f),
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                            if (selectedRuling == HawlContinuityRuling.HANAFI_START_END) {
                                Text(
                                    text = "✓ Hanafi Verdict: Since your wealth is currently at/above Nisab, the mid-year dip does NOT invalidate your Hawl. Your cycle remains valid.",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = EmeraldPrimary,
                                    lineHeight = 13.sp
                                )
                            } else {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = "⚠ Shafi'i/Hanbali Verdict: Under the continuous standard, dipping below Nisab interrupts the Hawl. The timer resets from the day wealth re-attained Nisab.",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ErrorRed,
                                        lineHeight = 13.sp
                                    )
                                    OutlinedButton(
                                        onClick = { viewModel.resetHawlToNow() },
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(14.dp), tint = EmeraldPrimary)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Restart Hawl from Today", fontSize = 11.sp, color = EmeraldPrimary)
                                    }
                                }
                            }
                        }
                    }
                }

                // 5. Actions: Add to System Calendar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val dueMillis = hawlState.startDateMillis + ((((hawlState.daysElapsed / hawlState.totalLunarDays) + 1) * hawlState.totalLunarDays) * 86_400_000L)
                            PortabilityUtils.addHawlMilestoneToCalendar(
                                context = context,
                                hawlDateMillis = dueMillis,
                                zakatDueFormatted = "${selectedCurrency.symbol}${String.format(java.util.Locale.US, "%,.2f", viewModel.getZakatDue())}"
                            )
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.Event, contentDescription = null, modifier = Modifier.size(16.dp))
                            Text("Add to Calendar", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    OutlinedButton(
                        onClick = { viewModel.resetHawlToNow() },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(16.dp), tint = EmeraldPrimary)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", fontWeight = FontWeight.Bold, color = EmeraldPrimary)
            }
        },
        containerColor = SurfaceContainerLowest,
        shape = RoundedCornerShape(18.dp)
    )
}
