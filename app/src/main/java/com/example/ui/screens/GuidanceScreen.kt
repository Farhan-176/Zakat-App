package com.example.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ZakatViewModel
import com.example.ui.theme.EmeraldOnPrimary
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldPrimaryContainer
import com.example.ui.theme.EmeraldPrimaryFixed
import com.example.ui.theme.EmeraldPrimaryFixedDim
import com.example.ui.theme.GoldOnSecondaryContainer
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.GoldSecondaryContainer
import com.example.ui.theme.GoldSecondaryFixed
import com.example.ui.theme.OnSurfaceDark
import com.example.ui.theme.OnSurfaceVariantMuted
import com.example.ui.theme.SurfaceContainerDefault
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerHighest
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.SurfaceMint

data class FiqhQuestion(
    val id: String,
    val category: String,
    val question: String,
    val answer: String,
    val reference: String
)

@Composable
fun GuidanceScreen(
    viewModel: ZakatViewModel,
    onOpenAsnafGuide: () -> Unit
) {
    val scrollState = rememberScrollState()
    val annualReminder by viewModel.annualRamadanReminder.collectAsState()
    val cadence by viewModel.reminderCadence.collectAsState()
    val simulatedToast by viewModel.simulatedToast.collectAsState()

    var selectedCategory by remember { mutableStateOf("All Questions") }
    var expandedQuestionId by remember { mutableStateOf<String?>("q1") }

    val categories = listOf("All Questions", "Nisab Rules", "Gold & Silver", "Investments & 401k", "Debts & Loans", "8 Asnaf Categories")

    val allQuestions = listOf(
        FiqhQuestion(
            id = "q1",
            category = "Nisab Rules",
            question = "What is the Nisab threshold and which standard should I use?",
            answer = "Nisab is the minimum threshold of wealth an individual must possess before Zakat becomes obligatory. Classical scholars established two benchmarks: Gold (87.48g / 7.5 tolas) and Silver (612.36g / 52.5 tolas). Many classical jurists (including the Hanafi school) recommend using the Silver standard for liquid cash and mixed assets because its lower financial threshold benefits the poor (Anfa' lil-Fuqara).",
            reference = "Al-Hidaya (Vol. 1), Fatawa Hindiyya"
        ),
        FiqhQuestion(
            id = "q2",
            category = "Gold & Silver",
            question = "Is personal gold jewelry subject to Zakat?",
            answer = "Scholars have two recognized views: The Hanafi school rules that all gold and silver is Zakatable regardless of whether it is worn as ornamental jewelry or stored as bullion. The Shafi'i, Maliki, and Hanbali schools hold that permissible customary jewelry worn by women is exempt from Zakat unless held as an investment or exceeding customary amounts.",
            reference = "Sahih Muslim 987, Bidayat al-Mujtahid"
        ),
        FiqhQuestion(
            id = "q3",
            category = "Investments & 401k",
            question = "How is Zakat assessed on Stocks, Equities, and Retirement (401k/IRA)?",
            answer = "For active trading (day trading / swing trading), Zakat is paid on the full market value at 2.5%. For long-term investment, Zakat is only due on the company's underlying Zakatable assets (cash, receivables, and trade inventory), typically estimated between 25% and 30% of the share price if detailed balance sheets are unavailable. For retirement funds, Zakat is calculated on the net accessible amount after early-withdrawal tax and penalty.",
            reference = "OIC Islamic Fiqh Academy Resolution No. 120"
        ),
        FiqhQuestion(
            id = "q4",
            category = "Debts & Loans",
            question = "Can I deduct long-term mortgages or car financing from my Zakat pool?",
            answer = "No. Long-term debts that span decades cannot be deducted in their entirety to offset current liquid wealth. Only the upcoming monthly payment or current lunar year's due installment is deductible as an immediate liability.",
            reference = "AAOIFI Shariah Standard No. 35 (Zakah)"
        ),
        FiqhQuestion(
            id = "q5",
            category = "8 Asnaf Categories",
            question = "Who are the legitimate Quranic recipients of Zakat?",
            answer = "The Holy Quran specifies eight distinct categories in Surah At-Tawbah (9:60): 1. Al-Fuqara (the desperately poor), 2. Al-Masakin (the destitute), 3. Appointed administrators, 4. Reconciling hearts, 5. Freeing captives, 6. Burdened debtors, 7. In the cause of Allah, and 8. The stranded traveler. Zakat cannot be given to direct ascendants (parents/grandparents), direct descendants (children), or one's spouse.",
            reference = "Surah At-Tawbah (9:60), Tafsir Ibn Kathir"
        ),
        FiqhQuestion(
            id = "q6",
            category = "All Questions",
            question = "Is any of my financial information transmitted online?",
            answer = "Never. Zakat Companion is built on sovereign, zero-telemetry architecture. All calculations, database tables, and reminders run exclusively inside your phone's memory and local SQLite sandbox. No cloud servers, analytics trackers, or external API endpoints are ever accessed.",
            reference = "Local Sovereign Cryptographic Guarantee"
        )
    )

    val filteredQuestions = if (selectedCategory == "All Questions") {
        allQuestions
    } else {
        allQuestions.filter { it.category == selectedCategory }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceMint)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Simulated Toast Banner
        simulatedToast?.let { toastMsg ->
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = EmeraldPrimary,
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(GoldSecondaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = GoldSecondary, modifier = Modifier.size(20.dp))
                        }
                        Column {
                            Text("LOCAL HAWL ALERT (SIMULATED)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimaryFixed, letterSpacing = 0.5.sp)
                            Text(toastMsg, fontSize = 11.sp, color = Color.White, lineHeight = 15.sp)
                        }
                    }

                    TextButton(onClick = { viewModel.clearSimulateToast() }) {
                        Text("Dismiss", color = GoldSecondaryFixed, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // Header Title
        Column {
            Text(
                text = "Zakat Guidance & Classical Rulings",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary
                )
            )
            Text(
                text = "Authentic classical consensus rulings & offline Hawl scheduler",
                fontSize = 11.sp,
                color = OnSurfaceVariantMuted
            )
        }

        // Category Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { cat ->
                val isSel = cat == selectedCategory
                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = if (isSel) EmeraldPrimary else SurfaceContainerLowest,
                    border = if (isSel) null else androidx.compose.foundation.BorderStroke(1.dp, SurfaceContainerHigh),
                    modifier = Modifier.clickable {
                        if (cat == "8 Asnaf Categories") {
                            onOpenAsnafGuide()
                        } else {
                            selectedCategory = cat
                        }
                    }
                ) {
                    Text(
                        text = cat,
                        fontSize = 11.sp,
                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSel) Color.White else OnSurfaceDark,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Questions Accordion List
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            filteredQuestions.forEach { item ->
                val isExp = expandedQuestionId == item.id
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
                                .clickable {
                                    expandedQuestionId = if (isExp) null else item.id
                                }
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(SurfaceContainerLow),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.AutoStories, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
                                }
                                Text(
                                    text = item.question,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary,
                                    lineHeight = 16.sp
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = OnSurfaceVariantMuted,
                                modifier = Modifier
                                    .size(20.dp)
                                    .rotate(if (isExp) 180f else 0f)
                            )
                        }

                        AnimatedVisibility(visible = isExp) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(SurfaceContainerLow)
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = item.answer,
                                    fontSize = 12.sp,
                                    color = OnSurfaceDark,
                                    lineHeight = 17.sp
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = GoldSecondary, modifier = Modifier.size(14.dp))
                                    Text(
                                        text = "Classical Reference: ${item.reference}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = GoldOnSecondaryContainer
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section: Device-Level Hawl Tracking & Local Zakat Reminders
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceContainerLowest,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
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
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(GoldSecondaryFixed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = GoldOnSecondaryContainer, modifier = Modifier.size(20.dp))
                        }
                        Column {
                            Text("Device-Level Hawl Tracking", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = EmeraldPrimary))
                            Text("Local Zakat Reminders", fontSize = 11.sp, color = OnSurfaceVariantMuted)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(SurfaceContainerHigh)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("OFFLINE ALARM", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                    }
                }

                // Master Toggle Switch
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceContainerLow,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Annual Zakat Reminder", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = OnSurfaceDark)
                            Text(
                                text = "Alerts you automatically when 354 lunar days have completed.",
                                fontSize = 11.sp,
                                color = OnSurfaceVariantMuted,
                                lineHeight = 15.sp
                            )
                        }

                        Switch(
                            checked = annualReminder,
                            onCheckedChange = { viewModel.annualRamadanReminder.value = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = EmeraldPrimary
                            )
                        )
                    }
                }

                // Cadence Selector
                if (annualReminder) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Reminder Cadence", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = OnSurfaceDark)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (cadence == "ramadan") SurfaceContainerLow else SurfaceContainerLowest)
                                .border(1.dp, if (cadence == "ramadan") EmeraldPrimary else SurfaceContainerHigh, RoundedCornerShape(10.dp))
                                .clickable { viewModel.reminderCadence.value = "ramadan" }
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RadioButton(
                                selected = cadence == "ramadan",
                                onClick = { viewModel.reminderCadence.value = "ramadan" },
                                colors = RadioButtonDefaults.colors(selectedColor = EmeraldPrimary)
                            )
                            Column {
                                Text("Yearly (Ramadan)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                                Text("Triggers automatically on 1st Ramadan annually", fontSize = 10.sp, color = OnSurfaceVariantMuted)
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (cadence == "hawl") SurfaceContainerLow else SurfaceContainerLowest)
                                .border(1.dp, if (cadence == "hawl") EmeraldPrimary else SurfaceContainerHigh, RoundedCornerShape(10.dp))
                                .clickable { viewModel.reminderCadence.value = "hawl" }
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RadioButton(
                                selected = cadence == "hawl",
                                onClick = { viewModel.reminderCadence.value = "hawl" },
                                colors = RadioButtonDefaults.colors(selectedColor = EmeraldPrimary)
                            )
                            Column {
                                Text("Hawl Anniversary (Lunar 354-Day)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                                Text("Calculates exactly 1 lunar year from your last ledger date", fontSize = 10.sp, color = OnSurfaceVariantMuted)
                            }
                        }

                        // Target Date Preview Card
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = SurfaceContainerLow,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Alarm, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                                Column {
                                    Text("Estimated Milestone Date", fontSize = 10.sp, color = OnSurfaceVariantMuted)
                                    Text(
                                        text = if (cadence == "ramadan") "1 Ramadan 1448 AH (approx. 18 Feb 2027)" else "12 Safar 1448 AH (approx. 27 Jul 2027)",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldPrimary
                                    )
                                }
                            }
                        }

                        // Test Notification Trigger
                        Button(
                            onClick = { viewModel.triggerSimulateReminder() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("simulate_notification_button"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SurfaceContainerDefault,
                                contentColor = EmeraldPrimary
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Simulate Milestone Notification", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
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
                        text = "All timers and notifications are registered directly in the Android AlarmManager and do not ping any external server.",
                        fontSize = 10.sp,
                        color = OnSurfaceVariantMuted,
                        lineHeight = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
