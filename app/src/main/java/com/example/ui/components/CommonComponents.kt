package com.example.ui.components

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.example.data.model.AsnafCategory
import com.example.data.model.AsnafAllocation
import com.example.ui.ZakatViewModel
import com.example.ui.theme.EmeraldPrimaryContainer
import com.example.ui.theme.EmeraldPrimaryFixed
import com.example.ui.theme.EmeraldPrimaryFixedDim
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.GoldSecondaryContainer
import com.example.ui.theme.GoldOnSecondaryContainer
import com.example.ui.theme.SurfaceMint
import com.example.util.PortabilityUtils
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.AvailableCurrencies
import com.example.data.model.CurrencyInfo
import com.example.data.model.FiqhMadhab
import com.example.data.model.PortfolioProfile
import com.example.ui.AppTab
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldPrimaryContainer
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.GoldSecondaryContainer
import com.example.ui.theme.OnSurfaceDark
import com.example.ui.theme.OnSurfaceVariantMuted
import com.example.ui.theme.SurfaceContainerDefault
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.SurfaceMint

const val BRAND_LOGO_URL = "https://lh3.googleusercontent.com/aida/AEtjO1XjhmkYIt0DGly0OBicBcNlnIH8oLLmJ74SLp9XDyQ3If1VH9lz_dcVPg3AsyH1S8HYOwEkcnIC4LyM3ScNM2TjyiXSWoa_uH2W67hd2789KI9JDh6EPBc5VLeWARwQpuh3JQzQyPvTVYBxYNWQtXWT3Ptl5aXrSvr0Tj6qcWTKXwRJGsDJIZkflner8Y7yu6-gAmYfyRCSk_TaNzsk-TeVugsGhtgH-pib6E1gke50_KqWIbnP3VCiuivC"

@Composable
fun ZakatBrandLogo(
    modifier: Modifier = Modifier,
    size: Dp = 36.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(SurfaceContainerLow),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.Image(
            painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.ic_zakat_app_logo),
            contentDescription = "Zakat Companion Emblem",
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun ZakatTopHeader(
    currentTabTitle: String,
    activeProfileName: String,
    activeCurrencyCode: String,
    activeCurrencySymbol: String,
    onProfileClick: () -> Unit,
    onCurrencyClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onSettingsClick: () -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        shadowElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                // Header Content
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Logo & Titles
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        ZakatBrandLogo(size = 34.dp)

                        Column(modifier = Modifier.weight(1f, fill = false)) {
                            Text(
                                text = "Zakat Companion",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary,
                                    fontSize = 14.5.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = currentTabTitle,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = OnSurfaceVariantMuted,
                                    fontSize = 10.5.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Interactive Badges: Profile, Currency, and Privacy
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Currency Chip
                        Surface(
                            shape = RoundedCornerShape(100.dp),
                            color = SurfaceContainerLowest,
                            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceContainerHigh),
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .clickable(onClick = onCurrencyClick)
                                .testTag("top_currency_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(activeCurrencySymbol, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                                Text(activeCurrencyCode, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = OnSurfaceDark)
                            }
                        }

                        // Profile Chip
                        Surface(
                            shape = RoundedCornerShape(100.dp),
                            color = SurfaceContainerLowest,
                            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceContainerHigh),
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .clickable(onClick = onProfileClick)
                                .testTag("top_profile_badge")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(13.dp))
                                Text(
                                    text = activeProfileName,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OnSurfaceDark,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.widthIn(max = 65.dp)
                                )
                            }
                        }

                        // Privacy Button (Clean, balanced surface icon)
                        Surface(
                            shape = CircleShape,
                            color = SurfaceContainerLowest,
                            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceContainerHigh),
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .clickable(onClick = onPrivacyClick)
                                .testTag("top_privacy_button")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = "Security & Privacy",
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        // Settings Button (Direct access to Theme, Dark Mode & Preferences)
                        Surface(
                            shape = CircleShape,
                            color = SurfaceContainerLowest,
                            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceContainerHigh),
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .clickable(onClick = onSettingsClick)
                                .testTag("top_settings_button")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Settings & Theme",
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ZakatBottomNavBar(
    activeTab: AppTab,
    onTabSelected: (AppTab) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.98f),
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp)
                    .height(60.dp)
                    .padding(horizontal = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BottomNavItem(
                    icon = Icons.Default.Home,
                    label = "Home",
                    isSelected = activeTab == AppTab.HOME,
                    onClick = { onTabSelected(AppTab.HOME) },
                    testTag = "tab_home",
                    modifier = Modifier.weight(1f)
                )
                BottomNavItem(
                    icon = Icons.Default.Calculate,
                    label = "Calculation",
                    isSelected = activeTab == AppTab.CALCULATION,
                    onClick = { onTabSelected(AppTab.CALCULATION) },
                    testTag = "tab_calculation",
                    modifier = Modifier.weight(1f)
                )
                BottomNavItem(
                    icon = Icons.Default.History,
                    label = "History",
                    isSelected = activeTab == AppTab.HISTORY,
                    onClick = { onTabSelected(AppTab.HISTORY) },
                    testTag = "tab_history",
                    modifier = Modifier.weight(1f)
                )
                BottomNavItem(
                    icon = Icons.Default.MenuBook,
                    label = "Guidance",
                    isSelected = activeTab == AppTab.GUIDANCE,
                    onClick = { onTabSelected(AppTab.GUIDANCE) },
                    testTag = "tab_guidance",
                    modifier = Modifier.weight(1f)
                )
                BottomNavItem(
                    icon = Icons.Default.Collections,
                    label = "Asset Vault",
                    isSelected = activeTab == AppTab.ASSETS,
                    onClick = { onTabSelected(AppTab.ASSETS) },
                    testTag = "tab_assets",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) EmeraldPrimaryContainer else OnSurfaceVariantMuted,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = label,
            fontSize = 10.5.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) EmeraldPrimaryContainer else OnSurfaceVariantMuted,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
fun CurrencySelectorDialog(
    selectedCurrency: CurrencyInfo,
    onSelectCurrency: (CurrencyInfo) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(22.dp))
                Text("Select Calculation Currency", fontWeight = FontWeight.Bold, color = EmeraldPrimary, fontSize = 16.sp)
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().height(320.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(AvailableCurrencies.all) { curr ->
                    val isSelected = curr.code == selectedCurrency.code
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) EmeraldPrimaryContainer else SurfaceContainerLow,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelectCurrency(curr)
                                onDismiss()
                            }
                            .testTag("currency_option_${curr.code}")
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) Color.White.copy(alpha = 0.2f) else SurfaceContainerDefault),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(curr.symbol, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else EmeraldPrimary, fontSize = 14.sp)
                                }
                                Column {
                                    Text(curr.code, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else OnSurfaceDark, fontSize = 13.sp)
                                    Text(curr.name, fontSize = 11.sp, color = if (isSelected) Color.White.copy(alpha = 0.8f) else OnSurfaceVariantMuted)
                                }
                            }

                            if (isSelected) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done", fontWeight = FontWeight.Bold, color = EmeraldPrimary)
            }
        },
        containerColor = SurfaceContainerLowest
    )
}

@Composable
fun ProfileSelectorDialog(
    profiles: List<PortfolioProfile>,
    activeProfile: PortfolioProfile,
    onSelectProfile: (PortfolioProfile) -> Unit,
    onAddProfile: (String) -> Unit,
    onDismiss: () -> Unit,
    onLogout: (() -> Unit)? = null
) {
    var newProfileName by remember { mutableStateOf("") }
    var isAdding by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(22.dp))
                Text("Household Portfolios", fontWeight = FontWeight.Bold, color = EmeraldPrimary, fontSize = 16.sp)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Manage separate Zakat records for each family member or business while retaining individual ledger audits.",
                    fontSize = 12.sp,
                    color = OnSurfaceVariantMuted,
                    lineHeight = 16.sp
                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth().height(180.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(profiles) { prof ->
                        val isSelected = prof.id == activeProfile.id
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) EmeraldPrimaryContainer else SurfaceContainerLow,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectProfile(prof)
                                    onDismiss()
                                }
                                .testTag("profile_item_${prof.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(prof.name, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else OnSurfaceDark, fontSize = 13.sp)
                                    if (prof.description.isNotBlank()) {
                                        Text(prof.description, fontSize = 10.sp, color = if (isSelected) Color.White.copy(alpha = 0.8f) else OnSurfaceVariantMuted)
                                    }
                                }
                                if (isSelected) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }

                if (isAdding) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = newProfileName,
                            onValueChange = { newProfileName = it },
                            placeholder = { Text("Portfolio Name (e.g. Children Trust)", fontSize = 11.sp) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )
                        Button(
                            onClick = {
                                if (newProfileName.isNotBlank()) {
                                    onAddProfile(newProfileName)
                                    newProfileName = ""
                                    isAdding = false
                                    onDismiss()
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                        ) {
                            Text("Add", fontSize = 11.sp)
                        }
                    }
                } else {
                    TextButton(
                        onClick = { isAdding = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Create Custom Portfolio", color = EmeraldPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                if (onLogout != null) {
                    TextButton(
                        onClick = {
                            onDismiss()
                            onLogout()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Sign Out / Switch Mode", color = OnSurfaceVariantMuted, fontSize = 12.sp)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", fontWeight = FontWeight.Bold, color = EmeraldPrimary)
            }
        },
        containerColor = SurfaceContainerLowest
    )
}

@Composable
fun AsnafModalDialog(
    viewModel: ZakatViewModel? = null,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var activeTab by remember { mutableStateOf(if (viewModel != null && viewModel.getZakatDue() > 0.0) 0 else 1) }

    AlertDialog(
        onDismissRequest = onDismiss,
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
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "8 Quranic Asnaf Distribution",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceDark
                            )
                        )
                        Text(
                            text = "Surah At-Tawbah 9:60 Beneficiary Allocation",
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
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (viewModel != null) {
                    // Segmented Tabs
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(SurfaceContainerLow)
                            .padding(3.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (activeTab == 0) EmeraldPrimary else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { activeTab = 0 }
                        ) {
                            Text(
                                text = "Distribution Planner",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (activeTab == 0) Color.White else OnSurfaceDark,
                                modifier = Modifier.padding(vertical = 6.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (activeTab == 1) EmeraldPrimary else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { activeTab = 1 }
                        ) {
                            Text(
                                text = "Quranic Fiqh Rulings",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (activeTab == 1) Color.White else OnSurfaceDark,
                                modifier = Modifier.padding(vertical = 6.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                if (viewModel != null && activeTab == 0) {
                    // DISTRIBUTION PLANNER TAB
                    val totalDue = viewModel.getZakatDue()
                    val allocations by viewModel.asnafAllocations.collectAsState()
                    val currency by viewModel.selectedCurrency.collectAsState()
                    val totalAllocated = allocations.values.sumOf { it.allocatedAmount }
                    val remaining = totalDue - totalAllocated
                    val fraction = if (totalDue > 0.0) (totalAllocated / totalDue).toFloat().coerceIn(0f, 1f) else 0f

                    // Summary Card
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = EmeraldPrimaryContainer,
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
                                Text(
                                    text = "TOTAL ZAKAT OBLIGATION",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimaryFixed
                                )
                                Text(
                                    text = "${currency.symbol}${String.format(java.util.Locale.US, "%,.2f", totalDue)}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            LinearProgressIndicator(
                                progress = { fraction },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = if (remaining < -0.01) GoldSecondary else EmeraldPrimaryFixed,
                                trackColor = Color.White.copy(alpha = 0.2f)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Allocated: ${currency.symbol}${String.format(java.util.Locale.US, "%,.2f", totalAllocated)}",
                                    fontSize = 10.5.sp,
                                    color = EmeraldPrimaryFixedDim
                                )
                                Text(
                                    text = if (remaining >= 0.0) "Remaining: ${currency.symbol}${String.format(java.util.Locale.US, "%,.2f", remaining)}"
                                    else "Exceeds by +${currency.symbol}${String.format(java.util.Locale.US, "%,.2f", -remaining)}",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (remaining >= 0.0) Color.White else GoldSecondary
                                )
                            }
                        }
                    }

                    // Auto Distribute & Share Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.autoDistributeAsnafRecommended() },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp), tint = EmeraldPrimary)
                                Text("Auto-Allocate", fontSize = 11.sp, color = EmeraldPrimary, fontWeight = FontWeight.Bold)
                            }
                        }

                        Button(
                            onClick = {
                                val shareText = viewModel.generateAsnafDistributionShareText()
                                PortabilityUtils.shareText(context, shareText, "8 Asnaf Zakat Distribution Plan")
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.White)
                                Text("Share Plan", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Allocations List for each of the 8 Asnaf
                    AsnafCategory.entries.forEach { cat ->
                        val allocation = allocations[cat.id] ?: AsnafAllocation(category = cat)
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = SurfaceContainerLow,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = "${cat.arabicName} • ${cat.englishTitle}",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.5.sp,
                                                color = OnSurfaceDark
                                            )
                                        }
                                        Text(
                                            text = cat.quranicDescription,
                                            fontSize = 9.5.sp,
                                            color = OnSurfaceVariantMuted
                                        )
                                    }

                                    // Quick recommended preset button
                                    if (totalDue > 0.0) {
                                        TextButton(
                                            onClick = {
                                                val amt = totalDue * cat.recommendedSharePct
                                                viewModel.setAsnafAllocation(cat.id, amt)
                                            },
                                            modifier = Modifier.height(28.dp)
                                        ) {
                                            Text(
                                                text = "${(cat.recommendedSharePct * 100).toInt()}%",
                                                fontSize = 10.sp,
                                                color = EmeraldPrimary,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = if (allocation.allocatedAmount > 0.0) String.format(java.util.Locale.US, "%.2f", allocation.allocatedAmount) else "",
                                        onValueChange = { str ->
                                            val parsed = str.toDoubleOrNull() ?: 0.0
                                            viewModel.setAsnafAllocation(cat.id, parsed)
                                        },
                                        placeholder = { Text("Amount (${currency.symbol})", fontSize = 10.5.sp) },
                                        modifier = Modifier.weight(1.2f),
                                        singleLine = true,
                                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal
                                        )
                                    )

                                    OutlinedTextField(
                                        value = allocation.beneficiaryNote,
                                        onValueChange = { note ->
                                            viewModel.setAsnafAllocation(cat.id, allocation.allocatedAmount, note)
                                        },
                                        placeholder = { Text("Recipient / Charity note", fontSize = 10.5.sp) },
                                        modifier = Modifier.weight(1.8f),
                                        singleLine = true
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // QURANIC FIQH RULINGS TAB
                    Text(
                        text = "\"Zakah expenditures are only for the poor and for the needy and for those employed to collect [zakah] and for bringing hearts together [for Islam] and for freeing captives [or slaves] and for those in debt and for the cause of Allah and for the [stranded] traveler - an obligation [imposed] by Allah. And Allah is Knowing and Wise.\"",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = OnSurfaceDark,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        text = "— Surah At-Tawbah (9:60)",
                        fontSize = 10.5.sp,
                        color = EmeraldPrimary,
                        fontWeight = FontWeight.Bold
                    )

                    androidx.compose.material3.Divider(color = OnSurfaceVariantMuted.copy(alpha = 0.2f))

                    AsnafCategory.entries.forEach { cat ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = SurfaceContainerLow,
                            modifier = Modifier.fillMaxWidth()
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
                                        text = "${cat.arabicName} — ${cat.englishTitle}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = EmeraldPrimary
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(SurfaceContainerHigh)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "Classical: ${(cat.recommendedSharePct * 100).toInt()}%",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = OnSurfaceVariantMuted
                                        )
                                    }
                                }
                                Text(
                                    text = cat.quranicDescription,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = OnSurfaceDark,
                                        fontSize = 11.sp
                                    )
                                )
                                Text(
                                    text = "Eligibility: ${cat.eligibilityConditions}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = OnSurfaceVariantMuted,
                                        fontSize = 10.sp,
                                        lineHeight = 13.sp
                                    )
                                )
                            }
                        }
                    }

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
                                text = "Important Jurisprudence Boundaries",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.5.sp,
                                color = EmeraldPrimary
                            )
                            Text(
                                text = "• Direct Ownership (Tamleek): Zakat must be transferred into the full ownership of the eligible recipient.\n• Direct Lineage Ineligibility: Parents, grandparents, children, and spouses cannot receive Zakat from you.\n• Banu Hashim: Descendants of Prophet Muhammad (ﷺ) are prohibited from receiving Zakat by prophetic command.",
                                fontSize = 10.sp,
                                color = OnSurfaceDark,
                                lineHeight = 14.sp
                            )
                        }
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

@Composable
fun PrivacyInfoDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Sovereign Privacy Guarantee",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary
                    )
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "100% Client-Side Local Ledger",
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary,
                    fontSize = 14.sp
                )
                Text(
                    text = "Your financial assets, bank balances, precious metals, and debts are processed purely in local device RAM and stored exclusively in your local SQLite Room database.",
                    style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariantMuted)
                )
                Text(
                    text = "• Zero Cloud Sync: No data is ever transmitted to servers.\n• Zero Telemetry: No analytics, tracking or telemetry.\n• Zero Accounts: Fully anonymous, no sign-in required.\n• Offline Certified: Operates completely disconnected from the internet.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = EmeraldPrimary,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Understood", fontWeight = FontWeight.Bold, color = EmeraldPrimary)
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    )
}

@Composable
fun SettingsDialog(
    viewModel: ZakatViewModel,
    onDismiss: () -> Unit
) {
    val isDark by viewModel.isDarkMode.collectAsState()
    val language by viewModel.language.collectAsState()
    val isUrdu = language == "ur"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = if (isUrdu) "ترتیبات اور ظاہری شکل" else "Settings & Preferences",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary
                    )
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Section: Appearance / Theme
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (isUrdu) "ظاہری انداز (تھیم)" else "Appearance & Theme",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        )
                    )

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLow,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (isDark) EmeraldPrimaryContainer else SurfaceMint,
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = if (isDark) Icons.Default.DarkMode else Icons.Default.LightMode,
                                            contentDescription = if (isDark) "Dark Mode Active" else "Light Mode Active",
                                            tint = EmeraldPrimary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Column {
                                    Text(
                                        text = if (isUrdu) "ڈارک موڈ (رات کا انداز)" else "Dark Theme",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = if (isDark) {
                                            if (isUrdu) "گہرے رنگوں کا پرسکون انداز فعال ہے" else "Deep emerald & obsidian palette active"
                                        } else {
                                            if (isUrdu) "ہلکا اور واضح انداز فعال ہے" else "Crisp high-contrast daylight palette active"
                                        },
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 11.5.sp
                                        )
                                    )
                                }
                            }

                            Switch(
                                checked = isDark,
                                onCheckedChange = { checked ->
                                    viewModel.setDarkMode(checked)
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = EmeraldPrimary,
                                    uncheckedThumbColor = EmeraldPrimary,
                                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceContainerHigh
                                ),
                                modifier = Modifier.testTag("dark_mode_switch")
                            )
                        }
                    }
                }

                // Section: Language
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (isUrdu) "زبان منتخب کریں" else "Language Selection",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        )
                    )

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLow,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (isUrdu) "موجودہ زبان:" else "Active:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(
                                    onClick = { viewModel.setLanguage("en") },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (!isUrdu) EmeraldPrimary else Color.Transparent,
                                        contentColor = if (!isUrdu) Color.White else MaterialTheme.colorScheme.onSurface
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Text("English", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = { viewModel.setLanguage("ur") },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isUrdu) EmeraldPrimary else Color.Transparent,
                                        contentColor = if (isUrdu) Color.White else MaterialTheme.colorScheme.onSurface
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Text("اردو", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // Section: Sovereign Privacy Note
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (isUrdu) "تمام ترجیحات مکمل طور پر آپ کے فون میں محفوظ رہتی ہیں۔" else "All settings & theme states are preserved securely on-device.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 10.5.sp
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = if (isUrdu) "مکمل" else "Done",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(18.dp)
    )
}
