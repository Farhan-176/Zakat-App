package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PortfolioProfile
import com.example.data.model.ZakatRecord
import com.example.ui.AppTab
import com.example.ui.ZakatViewModel
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldPrimaryContainer
import com.example.ui.theme.EmeraldPrimaryFixed
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GoldOnSecondaryContainer
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.GoldSecondaryFixed
import com.example.ui.theme.OnSurfaceDark
import com.example.ui.theme.OnSurfaceVariantMuted
import com.example.ui.theme.OutlineBorder
import com.example.ui.theme.SurfaceContainerDefault
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.SurfaceMint
import com.example.ui.components.PdfExportDialog
import com.example.util.PortabilityUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    viewModel: ZakatViewModel,
    onStartNewCalculation: () -> Unit
) {
    val records by viewModel.savedRecords.collectAsState()
    val activeProfile by viewModel.activeProfile.collectAsState()
    val profiles by viewModel.profiles.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedProfileFilter by remember { mutableStateOf("All") }
    var recordToDelete by remember { mutableStateOf<ZakatRecord?>(null) }
    var showImportDialog by remember { mutableStateOf(false) }
    var importJsonText by remember { mutableStateOf("") }
    var recordForPdfExport by remember { mutableStateOf<ZakatRecord?>(null) }

    val context = LocalContext.current

    val filteredRecords = records.filter { record ->
        val matchesSearch = record.label.contains(searchQuery, ignoreCase = true) ||
                record.refNumber.contains(searchQuery, ignoreCase = true) ||
                record.hijriYear.contains(searchQuery, ignoreCase = true) ||
                record.profileName.contains(searchQuery, ignoreCase = true)
        val matchesProfile = selectedProfileFilter == "All" || record.profileName.equals(selectedProfileFilter, ignoreCase = true)
        matchesSearch && matchesProfile
    }

    val totalZakatCumulative = filteredRecords.sumOf { it.zakatDue }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceMint)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Record Count & Status Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${filteredRecords.size} Saved Calculations",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = EmeraldPrimary
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(SurfaceContainerHigh)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(12.dp))
                    Text("Offline Vault", fontSize = 11.sp, color = EmeraldPrimary, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Summary Statistics Ribbon
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = EmeraldPrimaryContainer,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (selectedProfileFilter == "All") "CUMULATIVE DISCHARGED" else "$selectedProfileFilter DISCHARGED",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimaryFixed,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = viewModel.formatCurrency(totalZakatCumulative),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }

                Button(
                    onClick = onStartNewCalculation,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldSecondaryFixed,
                        contentColor = GoldOnSecondaryContainer
                    ),
                    modifier = Modifier.testTag("history_new_calc_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Text("New Calc", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Data Sovereignty & Portability Action Bar
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = SurfaceContainerLowest,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Data Sovereignty:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary,
                    modifier = Modifier.padding(start = 4.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                // Export PDF Report
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SurfaceContainerLow,
                    modifier = Modifier
                        .clickable {
                            if (filteredRecords.isNotEmpty()) {
                                recordForPdfExport = filteredRecords.first()
                            } else if (records.isNotEmpty()) {
                                recordForPdfExport = records.first()
                            } else {
                                Toast.makeText(context, "No records available to export as PDF", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .testTag("export_pdf_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(14.dp))
                        Text("PDF", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                    }
                }

                // Export JSON
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SurfaceContainerLow,
                    modifier = Modifier
                        .clickable {
                            val json = PortabilityUtils.exportRecordsToJson(records)
                            PortabilityUtils.shareText(context, json, "Export Zakat Vault (JSON)")
                        }
                        .testTag("export_json_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.FileDownload, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(14.dp))
                        Text("JSON", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                    }
                }

                // Export CSV
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SurfaceContainerLow,
                    modifier = Modifier
                        .clickable {
                            val csv = PortabilityUtils.exportRecordsToCsv(records)
                            PortabilityUtils.shareText(context, csv, "Export Zakat Vault (CSV)")
                        }
                        .testTag("export_csv_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.TableChart, contentDescription = null, tint = GoldSecondary, modifier = Modifier.size(14.dp))
                        Text("CSV", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldSecondary)
                    }
                }

                // Import JSON
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SurfaceContainerLow,
                    modifier = Modifier
                        .clickable { showImportDialog = true }
                        .testTag("import_json_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.FileUpload, contentDescription = null, tint = OnSurfaceDark, modifier = Modifier.size(14.dp))
                        Text("Import", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OnSurfaceDark)
                    }
                }
            }
        }

        // Profile Filter Chips Carousel
        val filterOptions = listOf("All") + profiles.map { it.name }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filterOptions.forEach { opt ->
                val isSelected = opt == selectedProfileFilter
                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = if (isSelected) EmeraldPrimaryContainer else SurfaceContainerLowest,
                    modifier = Modifier
                        .clickable { selectedProfileFilter = opt }
                        .testTag("filter_profile_$opt")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (opt != "All") {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else OnSurfaceVariantMuted,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                        Text(
                            text = opt,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else OnSurfaceDark
                        )
                    }
                }
            }
        }

        // Search Input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search reference, year, or label...", fontSize = 12.sp) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = OnSurfaceVariantMuted, modifier = Modifier.size(18.dp))
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("history_search_input"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmeraldPrimary,
                unfocusedBorderColor = OutlineBorder.copy(alpha = 0.3f),
                focusedContainerColor = SurfaceContainerLowest,
                unfocusedContainerColor = SurfaceContainerLowest
            )
        )

        // Records List
        if (filteredRecords.isEmpty()) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SurfaceContainerLowest,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(SurfaceContainerLow),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.History, contentDescription = null, tint = OnSurfaceVariantMuted, modifier = Modifier.size(28.dp))
                    }
                    Text(
                        text = if (searchQuery.isNotBlank() || selectedProfileFilter != "All") "No matching records" else "No saved records yet",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceDark
                        )
                    )
                    Text(
                        text = "Complete a Zakat calculation to preserve a cryptographically signed statement in your private local vault.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = OnSurfaceVariantMuted,
                            textAlign = TextAlign.Center
                        )
                    )
                    Button(
                        onClick = onStartNewCalculation,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmeraldPrimaryContainer,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.padding(top = 6.dp)
                    ) {
                        Text("Calculate Zakat Now", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredRecords, key = { it.refNumber }) { record ->
                    HistoryRecordCard(
                        record = record,
                        formatCurrency = { amt -> viewModel.formatCurrency(amt) },
                        onViewStatement = {
                            viewModel.viewStatementForRecord(record)
                            viewModel.selectTab(AppTab.CALCULATION)
                        },
                        onShareStatement = {
                            val text = """
                                OFFICIAL ZAKAT CERTIFICATE
                                Ref: ${record.refNumber}
                                Profile: ${record.profileName}
                                Hijri: ${record.hijriYear}
                                Nisab Standard: ${record.nisabStandard}
                                Currency: ${record.currencyCode} (${record.currencySymbol})
                                Net Wealth: ${record.currencySymbol}${record.netWealth}
                                Zakat Due (2.5%): ${record.currencySymbol}${record.zakatDue}
                                Verification Digest: ${record.hashDigest}
                            """.trimIndent()
                            PortabilityUtils.shareText(context, text, "Zakat Certificate ${record.refNumber}")
                        },
                        onExportPdf = { recordForPdfExport = record },
                        onDeleteClick = { recordToDelete = record }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    // Delete Confirmation Dialog
    if (recordToDelete != null) {
        val record = recordToDelete!!
        AlertDialog(
            onDismissRequest = { recordToDelete = null },
            icon = { Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = ErrorRed) },
            title = { Text("Delete Record?", fontWeight = FontWeight.Bold) },
            text = {
                Text("Are you sure you want to delete ${record.refNumber} (${record.label})? This offline record cannot be recovered.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteRecord(record)
                        recordToDelete = null
                        Toast.makeText(context, "Record removed from local vault", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed, contentColor = Color.White)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { recordToDelete = null }) {
                    Text("Cancel", color = OnSurfaceDark)
                }
            },
            containerColor = SurfaceContainerLowest
        )
    }

    // Import JSON Dialog
    if (showImportDialog) {
        AlertDialog(
            onDismissRequest = { showImportDialog = false },
            icon = { Icon(Icons.Default.FileUpload, contentDescription = null, tint = EmeraldPrimary) },
            title = { Text("Restore / Import Ledger", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Paste previously exported JSON records to restore them into your offline database:", fontSize = 12.sp, color = OnSurfaceVariantMuted)
                    OutlinedTextField(
                        value = importJsonText,
                        onValueChange = { importJsonText = it },
                        placeholder = { Text("[{\"refNumber\": \"...\"}]") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val imported = PortabilityUtils.importRecordsFromJson(importJsonText)
                        if (imported.isNotEmpty()) {
                            viewModel.importRecords(imported)
                            Toast.makeText(context, "Successfully restored ${imported.size} records!", Toast.LENGTH_LONG).show()
                            showImportDialog = false
                            importJsonText = ""
                        } else {
                            Toast.makeText(context, "Invalid JSON format or empty records", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimaryContainer, contentColor = Color.White)
                ) {
                    Text("Import Records")
                }
            },
            dismissButton = {
                TextButton(onClick = { showImportDialog = false }) {
                    Text("Cancel", color = OnSurfaceDark)
                }
            },
            containerColor = SurfaceContainerLowest
        )
    }

    if (recordForPdfExport != null) {
        PdfExportDialog(
            record = recordForPdfExport!!,
            onDismiss = { recordForPdfExport = null }
        )
    }
}

@Composable
private fun HistoryRecordCard(
    record: ZakatRecord,
    formatCurrency: (Double) -> String,
    onViewStatement: () -> Unit,
    onShareStatement: () -> Unit,
    onExportPdf: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val dateStr = remember(record.timestamp) {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.US)
        sdf.format(Date(record.timestamp))
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = SurfaceContainerLowest,
        shadowElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onViewStatement)
            .testTag("history_item_${record.refNumber}")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(record.refNumber, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                        Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(OnSurfaceVariantMuted))
                        Text(dateStr, fontSize = 11.sp, color = OnSurfaceVariantMuted)
                    }
                    Text(
                        text = record.label,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceDark
                        ),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(SurfaceContainerDefault)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(record.profileName, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(SurfaceContainerLow)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(record.currencyCode, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldSecondary)
                    }
                }
            }

            // Ledger Summary Numbers
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
                    Column {
                        Text("Net Zakatable Wealth", fontSize = 10.sp, color = OnSurfaceVariantMuted)
                        Text(
                            text = "${record.currencySymbol}${formatNumber(record.netWealth)}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceDark
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("Zakat Discharged (2.5%)", fontSize = 10.sp, color = EmeraldPrimary, fontWeight = FontWeight.Bold)
                        Text(
                            text = "${record.currencySymbol}${formatNumber(record.zakatDue)}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        )
                    }
                }
            }

            // Card Footer & Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Default.Verified, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(14.dp))
                    Text(
                        text = "${record.nisabStandard} Nisab • ${record.goldKarat}",
                        fontSize = 10.sp,
                        color = OnSurfaceVariantMuted
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = onExportPdf,
                        modifier = Modifier.size(32.dp).testTag("export_pdf_record_${record.refNumber}")
                    ) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = "Export PDF Report", tint = EmeraldPrimary, modifier = Modifier.size(17.dp))
                    }

                    IconButton(
                        onClick = onShareStatement,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
                    }

                    IconButton(
                        onClick = onViewStatement,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Visibility, contentDescription = "View Statement", tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                    }

                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = ErrorRed, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

private fun formatNumber(amt: Double): String {
    return String.format(Locale.US, "%,.2f", amt)
}
