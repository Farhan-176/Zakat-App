package com.example.ui.screens

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.ZakatViewModel
import com.example.ui.components.ZakatBrandLogo
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldPrimaryContainer
import com.example.ui.theme.GoldOnSecondaryContainer
import com.example.ui.theme.GoldSecondaryContainer
import com.example.ui.theme.OnSurfaceDark
import com.example.ui.theme.OnSurfaceVariantMuted
import com.example.ui.theme.OutlineBorder
import com.example.ui.theme.SurfaceContainerDefault
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.SurfaceMint

@Composable
fun LanguageSetupScreen(viewModel: ZakatViewModel) {
    val language by viewModel.language.collectAsState()
    val scrollState = rememberScrollState()
    val isUrdu = language == "ur"

    val userName by viewModel.currentUserName.collectAsState()
    val isGuest by viewModel.isGuestUser.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceMint)
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 540.dp)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Privacy Pill
            Surface(
                shape = RoundedCornerShape(100.dp),
                color = SurfaceContainerHigh,
                shadowElevation = 0.5.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Icon(
                        Icons.Default.Shield,
                        contentDescription = null,
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = if (isUrdu) "نجی اور محفوظ • 100% آف لائن" else "Private & Secure • 100% Offline",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.6.sp,
                            color = OnSurfaceVariantMuted,
                            fontSize = 10.5.sp
                        )
                    )
                }
            }

            // 2. Emblem & Welcome Identity
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ZakatBrandLogo(size = 84.dp)

                Text(
                    text = "Zakat Companion",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary,
                        fontSize = 25.sp,
                        letterSpacing = (-0.5).sp
                    ),
                    modifier = Modifier.testTag("app_title_language_setup")
                )

                Text(
                    text = if (isUrdu)
                        "آپ کے اپنے آلے پر مکمل رازداری کے ساتھ زکوٰۃ کا شفاف حساب۔"
                    else
                        "Privacy-first Zakat calculation, right on your device.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = OnSurfaceVariantMuted,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                // Active Account Banner
                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = SurfaceContainerDefault,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = if (isGuest) "Continuing as Guest" else "Signed in as $userName",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = EmeraldPrimary
                        )
                    }
                }
            }

            // 3. Choose Language Card
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = SurfaceContainerLowest,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Header row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Shield,
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = if (isUrdu) "زبان منتخب کریں" else "Choose Language",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary,
                                    fontSize = 16.sp
                                )
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(SurfaceContainerDefault)
                                .padding(horizontal = 10.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "زبان منتخب کریں",
                                fontSize = 11.sp,
                                color = OnSurfaceVariantMuted,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Interactive Language Options
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // English Option
                        val isEn = language == "en"
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isEn) SurfaceContainerLow else SurfaceContainerLowest,
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = if (isEn) 1.5.dp else 1.dp,
                                    color = if (isEn) EmeraldPrimary else OutlineBorder.copy(alpha = 0.25f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.setLanguage("en") }
                                .testTag("lang_option_en")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(if (isEn) EmeraldPrimary.copy(alpha = 0.12f) else SurfaceContainerDefault),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "EN",
                                            fontWeight = FontWeight.Bold,
                                            color = EmeraldPrimary,
                                            fontSize = 14.sp
                                        )
                                    }

                                    Column {
                                        Text(
                                            text = "English",
                                            style = MaterialTheme.typography.labelLarge.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = if (isEn) EmeraldPrimary else OnSurfaceDark,
                                                fontSize = 15.sp
                                            )
                                        )
                                        Text(
                                            text = "Default Interface",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = OnSurfaceVariantMuted,
                                                fontSize = 12.sp
                                            )
                                        )
                                    }
                                }

                                // Selection checkmark circle
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(if (isEn) EmeraldPrimary else SurfaceContainerHigh),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isEn) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Urdu Option
                        val isUr = language == "ur"
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isUr) SurfaceContainerLow else SurfaceContainerLowest,
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = if (isUr) 1.5.dp else 1.dp,
                                    color = if (isUr) EmeraldPrimary else OutlineBorder.copy(alpha = 0.25f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.setLanguage("ur") }
                                .testTag("lang_option_ur")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(if (isUr) EmeraldPrimary.copy(alpha = 0.12f) else SurfaceContainerDefault),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "اردو",
                                            fontWeight = FontWeight.Bold,
                                            color = EmeraldPrimary,
                                            fontSize = 13.sp
                                        )
                                    }

                                    Column {
                                        Text(
                                            text = "اردو (Urdu)",
                                            style = MaterialTheme.typography.labelLarge.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = if (isUr) EmeraldPrimary else OnSurfaceDark,
                                                fontSize = 15.sp
                                            )
                                        )
                                        Text(
                                            text = "مقامی وضع اور ترجمہ",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = OnSurfaceVariantMuted,
                                                fontSize = 12.sp
                                            )
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(if (isUr) EmeraldPrimary else SurfaceContainerHigh),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isUr) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Text(
                        text = if (isUrdu) "آپ اسے بعد میں ترتیبات میں تبدیل کر سکتے ہیں۔" else "You can change this later in Settings.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = OnSurfaceVariantMuted,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp)
                    )
                }
            }

            // Primary Action: Proceed to Main App
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.completeLanguageSetup() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("continue_to_dashboard_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldPrimaryContainer,
                        contentColor = Color.White
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (isUrdu) "ڈیش بورڈ پر جائیں" else "Continue to Dashboard",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 15.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                TextButton(
                    onClick = { viewModel.logout() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("switch_account_back_button")
                ) {
                    Text(
                        text = if (isUrdu) "کھاتہ تبدیل کریں / لاگ ان پر واپس جائیں" else "Switch Account / Back to Login",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = EmeraldPrimary,
                            fontSize = 13.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
