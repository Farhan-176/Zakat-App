package com.example.ui.components

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.ZakatRecord
import com.example.ui.theme.EmeraldOnPrimary
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldPrimaryContainer
import com.example.ui.theme.GoldOnSecondaryContainer
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.GoldSecondaryFixed
import com.example.ui.theme.OnSurfaceDark
import com.example.ui.theme.OnSurfaceVariantMuted
import com.example.ui.theme.SurfaceContainerDefault
import com.example.ui.theme.SurfaceContainerHighest
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.util.PdfReportGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun PdfExportDialog(
    record: ZakatRecord,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isGenerating by remember { mutableStateOf(true) }
    var generatedFile by remember { mutableStateOf<File?>(null) }
    var savedToDownloadsUri by remember { mutableStateOf<Uri?>(null) }
    var statusMessage by remember { mutableStateOf<String?>(null) }

    // Launcher for "Save As..." system file picker
    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { destinationUri ->
        if (destinationUri != null && generatedFile != null) {
            scope.launch(Dispatchers.IO) {
                val success = PdfReportGenerator.savePdfToDestinationUri(context, generatedFile!!, destinationUri)
                withContext(Dispatchers.Main) {
                    if (success) {
                        statusMessage = "Saved successfully to custom location!"
                        Toast.makeText(context, "PDF Report saved successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        statusMessage = "Could not save to selected location."
                    }
                }
            }
        }
    }

    // Automatically generate the PDF in background on dialog open
    LaunchedEffect(record) {
        withContext(Dispatchers.IO) {
            try {
                val file = PdfReportGenerator.generateZakatPdf(context, record)
                generatedFile = file
                isGenerating = false
            } catch (e: Exception) {
                e.printStackTrace()
                isGenerating = false
                statusMessage = "Error creating PDF: ${e.localizedMessage}"
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SurfaceContainerLowest,
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(vertical = 24.dp)
                .testTag("pdf_export_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Dialog Top Bar
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
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(EmeraldPrimaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.PictureAsPdf,
                                contentDescription = null,
                                tint = EmeraldOnPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Export Zakat Report",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary
                                )
                            )
                            Text(
                                text = "Ref: ${record.refNumber}",
                                fontSize = 11.sp,
                                color = OnSurfaceVariantMuted
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Close",
                            tint = OnSurfaceVariantMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                if (isGenerating) {
                    // Loading State
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(
                            color = EmeraldPrimary,
                            modifier = Modifier.size(36.dp)
                        )
                        Text(
                            text = "Compiling official A4 Zakat Statement...",
                            fontSize = 12.sp,
                            color = OnSurfaceVariantMuted,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    // Report Information Card
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceContainerLow,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Payer Profile:", fontSize = 11.sp, color = OnSurfaceVariantMuted)
                                Text(record.profileName, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OnSurfaceDark)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Net Zakatable Wealth:", fontSize = 11.sp, color = OnSurfaceVariantMuted)
                                Text("${record.currencySymbol}${formatNumber(record.netWealth)}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OnSurfaceDark)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total Zakat Obligation (2.5%):", fontSize = 11.sp, color = EmeraldPrimary, fontWeight = FontWeight.Bold)
                                Text("${record.currencySymbol}${formatNumber(record.zakatDue)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                            }

                            if (generatedFile != null) {
                                val sizeKb = generatedFile!!.length() / 1024
                                Text(
                                    text = "📄 ${generatedFile!!.name} (${sizeKb} KB • A4 Print Ready)",
                                    fontSize = 10.sp,
                                    color = OnSurfaceVariantMuted,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }

                    // Success or Status Message Banner
                    if (statusMessage != null) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SurfaceContainerDefault,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
                                Text(
                                    text = statusMessage!!,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = EmeraldPrimary
                                )
                            }
                        }
                    }

                    // Action Buttons Column
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 1. Primary Action: Download to Device (Public Downloads)
                        Button(
                            onClick = {
                                if (generatedFile != null) {
                                    scope.launch(Dispatchers.IO) {
                                        val uri = PdfReportGenerator.savePdfToDownloads(
                                            context = context,
                                            pdfFile = generatedFile!!,
                                            displayName = "Zakat_Statement_${record.refNumber}"
                                        )
                                        withContext(Dispatchers.Main) {
                                            if (uri != null) {
                                                savedToDownloadsUri = uri
                                                statusMessage = "Saved to Downloads/ZakatCompanion folder!"
                                                Toast.makeText(context, "Saved to Downloads/ZakatCompanion", Toast.LENGTH_SHORT).show()
                                            } else {
                                                statusMessage = "Could not save directly to Downloads."
                                            }
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("btn_save_pdf_to_downloads"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EmeraldPrimary,
                                contentColor = Color.White
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                Text("Download to Device", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }

                        // 2. Open PDF in Viewer
                        Button(
                            onClick = {
                                if (generatedFile != null) {
                                    PdfReportGenerator.openPdfReport(context, generatedFile!!)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("btn_open_pdf_viewer"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SurfaceContainerHighest,
                                contentColor = EmeraldPrimary
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Visibility, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                                Text("Open in PDF Reader", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                            }
                        }

                        // 3. Row of Secondary Actions: Share PDF & Custom Folder (Save As)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Share PDF Button
                            Button(
                                onClick = {
                                    if (generatedFile != null) {
                                        PdfReportGenerator.sharePdfReport(
                                            context = context,
                                            pdfFile = generatedFile!!,
                                            title = "Zakat Assessment ${record.refNumber}"
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp)
                                    .testTag("btn_share_pdf_file"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SurfaceContainerLow,
                                    contentColor = GoldSecondary
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(Icons.Default.Share, contentDescription = null, tint = GoldSecondary, modifier = Modifier.size(16.dp))
                                    Text("Share PDF", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            // Save As... (Custom Directory) Button
                            Button(
                                onClick = {
                                    createDocumentLauncher.launch("Zakat_Statement_${record.refNumber}.pdf")
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp)
                                    .testTag("btn_save_as_pdf"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SurfaceContainerLow,
                                    contentColor = OnSurfaceDark
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(Icons.Default.Folder, contentDescription = null, tint = OnSurfaceDark, modifier = Modifier.size(16.dp))
                                    Text("Save As...", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    // Sovereign Offline Security Guarantee Footnote
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            Icons.Default.Shield,
                            contentDescription = null,
                            tint = OnSurfaceVariantMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "100% sovereign offline export. No financial data leaves this device.",
                            fontSize = 9.5.sp,
                            color = OnSurfaceVariantMuted
                        )
                    }
                }
            }
        }
    }
}

private fun formatNumber(amt: Double): String {
    return String.format(java.util.Locale.US, "%,.2f", amt)
}
