package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.data.model.AssetGroup
import com.example.data.model.AssetGroupCategories
import com.example.ui.ZakatViewModel
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldPrimaryContainer
import com.example.ui.theme.EmeraldPrimaryFixedDim
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AssetGroupsScreen(viewModel: ZakatViewModel) {
    val groups by viewModel.filteredAssetGroups.collectAsState()
    val allGroups by viewModel.assetGroups.collectAsState()
    val searchQuery by viewModel.assetGroupSearchQuery.collectAsState()
    val selectedCategory by viewModel.selectedAssetCategory.collectAsState()

    val activeGroup by viewModel.activeAssetGroup.collectAsState()
    val showCreateDialog by viewModel.showCreateAssetGroupDialog.collectAsState()
    val showAddImageToId by viewModel.showAddImageToGroupId.collectAsState()
    val previewUrl by viewModel.previewHotlinkUrl.collectAsState()

    var groupToEdit by remember { mutableStateOf<AssetGroup?>(null) }
    var groupToDelete by remember { mutableStateOf<AssetGroup?>(null) }

    val context = LocalContext.current
    val totalImages = allGroups.sumOf { it.imageUrls.size }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceMint)
                .testTag("asset_groups_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Hero Banner
            item {
                AssetVaultHeaderBanner(
                    totalGroups = allGroups.size,
                    totalImages = totalImages,
                    onCreateGroupClick = { viewModel.showCreateAssetGroupDialog.value = true }
                )
            }

            // Search Bar & Filter Chips
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.assetGroupSearchQuery.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("asset_group_search_input"),
                        placeholder = { Text("Search asset groups, receipts, or tags...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = EmeraldPrimary)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.assetGroupSearchQuery.value = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear Search")
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceContainerLowest,
                            unfocusedContainerColor = SurfaceContainerLowest,
                            focusedBorderColor = EmeraldPrimary,
                            unfocusedBorderColor = OutlineBorder
                        )
                    )

                    // Categories Horizontal Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val categories = listOf(AssetGroupCategories.ALL) + AssetGroupCategories.list
                        categories.forEach { category ->
                            val isSelected = selectedCategory.equals(category, ignoreCase = true)
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) EmeraldPrimary else SurfaceContainerLowest,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) EmeraldPrimary else OutlineBorder
                                ),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable { viewModel.selectedAssetCategory.value = category }
                                    .testTag("category_chip_$category")
                            ) {
                                Text(
                                    text = category,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else OnSurfaceDark
                                )
                            }
                        }
                    }
                }
            }

            // Group List Section Title
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (selectedCategory == AssetGroupCategories.ALL) "All Asset Collections" else "$selectedCategory Collections",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceDark
                    )
                    Text(
                        text = "${groups.size} groups",
                        fontSize = 12.sp,
                        color = OnSurfaceVariantMuted
                    )
                }
            }

            // Empty State
            if (groups.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp)
                            .testTag("empty_asset_groups_card"),
                        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldPrimaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.PhotoLibrary,
                                    contentDescription = null,
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Text(
                                text = if (searchQuery.isNotBlank()) "No Matching Asset Groups" else "No Asset Groups Found",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceDark
                            )
                            Text(
                                text = if (searchQuery.isNotBlank()) {
                                    "No collections match '$searchQuery'. Try checking other categories or clearing your search."
                                } else {
                                    "Organize hotlinked images of your physical assets, gold vault bars, charity receipts, and business inventory in Room local database."
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = OnSurfaceVariantMuted,
                                textAlign = TextAlign.Center
                            )
                            Button(
                                onClick = { viewModel.showCreateAssetGroupDialog.value = true },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("create_first_group_button")
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Create First Asset Group")
                            }
                        }
                    }
                }
            } else {
                items(groups, key = { it.id }) { group ->
                    AssetGroupCard(
                        group = group,
                        onViewGallery = { viewModel.activeAssetGroup.value = group },
                        onAddImage = { viewModel.showAddImageToGroupId.value = group.id },
                        onEdit = { groupToEdit = group },
                        onDelete = { groupToDelete = group },
                        onImageClick = { url -> viewModel.previewHotlinkUrl.value = url }
                    )
                }
            }

            // Bottom Spacing for FAB
            item {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }

        // Floating Action Button to quickly add a group
        FloatingActionButton(
            onClick = { viewModel.showCreateAssetGroupDialog.value = true },
            containerColor = EmeraldPrimary,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("fab_create_asset_group")
        ) {
            Icon(Icons.Default.AddPhotoAlternate, contentDescription = "Create New Asset Group")
        }
    }

    // Modal: Create / Edit Asset Group Dialog
    if (showCreateDialog) {
        CreateOrEditAssetGroupDialog(
            initialGroup = null,
            onDismiss = { viewModel.showCreateAssetGroupDialog.value = false },
            onSave = { name, desc, category, urls, tags ->
                viewModel.createAssetGroup(name, desc, category, urls, tags)
            }
        )
    }

    groupToEdit?.let { group ->
        CreateOrEditAssetGroupDialog(
            initialGroup = group,
            onDismiss = { groupToEdit = null },
            onSave = { name, desc, category, urls, tags ->
                viewModel.updateAssetGroup(
                    group.copy(
                        name = name,
                        description = desc,
                        category = category,
                        imageUrls = urls,
                        tags = tags,
                        coverImageUrl = if (urls.contains(group.coverImageUrl)) group.coverImageUrl else urls.firstOrNull() ?: ""
                    )
                )
                groupToEdit = null
            }
        )
    }

    // Modal: Delete Confirmation Dialog
    groupToDelete?.let { group ->
        AlertDialog(
            onDismissRequest = { groupToDelete = null },
            title = { Text("Delete Asset Group?") },
            text = {
                Text("Are you sure you want to delete '${group.name}'? This will remove all ${group.imageUrls.size} hotlinked image records from your local Room database.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteAssetGroup(group)
                        groupToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBA1A1A))
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { groupToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Modal: Add Hotlink URL to specific group
    showAddImageToId?.let { groupId ->
        val targetGroup = allGroups.firstOrNull { it.id == groupId }
        AddImageUrlDialog(
            groupName = targetGroup?.name ?: "Asset Group",
            onDismiss = { viewModel.showAddImageToGroupId.value = null },
            onAddUrl = { url ->
                viewModel.addHotlinkedImageToGroup(groupId, url)
            }
        )
    }

    // Modal: Active Group Gallery View
    activeGroup?.let { group ->
        AssetGroupGalleryModal(
            group = group,
            onDismiss = { viewModel.activeAssetGroup.value = null },
            onAddImage = { viewModel.showAddImageToGroupId.value = group.id },
            onRemoveImage = { url -> viewModel.removeHotlinkedImageFromGroup(group.id, url) },
            onSetCoverImage = { url -> viewModel.setGroupCoverImage(group.id, url) },
            onImageClick = { url -> viewModel.previewHotlinkUrl.value = url }
        )
    }

    // Modal: Full-screen Image Preview
    previewUrl?.let { url ->
        HotlinkImagePreviewDialog(
            imageUrl = url,
            onDismiss = { viewModel.previewHotlinkUrl.value = null }
        )
    }
}

@Composable
fun AssetVaultHeaderBanner(
    totalGroups: Int,
    totalImages: Int,
    onCreateGroupClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("asset_vault_header_banner"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
        border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
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
                            .size(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(EmeraldPrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Collections,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Asset Collections Vault",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceDark
                        )
                        Text(
                            text = "Room Local Database • Offline Sovereign",
                            fontSize = 11.sp,
                            color = EmeraldPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = EmeraldPrimaryContainer.copy(alpha = 0.5f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(12.dp))
                        Text("SQLite Encrypted", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                    }
                }
            }

            Text(
                text = "Save, organize, and name collections of hotlinked images for your physical assets, 24K gold bullion, charity disbursements, and business inventories.",
                fontSize = 13.sp,
                color = OnSurfaceVariantMuted,
                lineHeight = 18.sp
            )

            HorizontalDivider(color = SurfaceContainerHigh)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                    Column {
                        Text(
                            text = "$totalGroups",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = EmeraldPrimary
                        )
                        Text(
                            text = "Asset Groups",
                            fontSize = 11.sp,
                            color = OnSurfaceVariantMuted
                        )
                    }
                    Column {
                        Text(
                            text = "$totalImages",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldSecondary
                        )
                        Text(
                            text = "Hotlinked Images",
                            fontSize = 11.sp,
                            color = OnSurfaceVariantMuted
                        )
                    }
                }

                Button(
                    onClick = onCreateGroupClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                    modifier = Modifier.testTag("header_create_group_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New Group", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun AssetGroupCard(
    group: AssetGroup,
    onViewGallery: () -> Unit,
    onAddImage: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onImageClick: (String) -> Unit
) {
    val dateString = remember(group.updatedAt) {
        val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        sdf.format(Date(group.updatedAt))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onViewGallery)
            .testTag("asset_group_card_${group.id}"),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Title and Category Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = group.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceDark
                    )
                    if (group.description.isNotBlank()) {
                        Text(
                            text = group.description,
                            fontSize = 12.sp,
                            color = OnSurfaceVariantMuted,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = EmeraldPrimaryContainer.copy(alpha = 0.4f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.2f)),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = group.category,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = EmeraldPrimary
                    )
                }
            }

            // Image Thumbnails Grid Preview
            if (group.imageUrls.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val previewUrls = group.imageUrls.take(3)
                    val remainingCount = group.imageUrls.size - 3

                    previewUrls.forEachIndexed { index, url ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1.2f)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, SurfaceContainerHigh, RoundedCornerShape(10.dp))
                                .clickable { onImageClick(url) }
                        ) {
                            AsyncImage(
                                model = url,
                                contentDescription = "Asset image preview ${index + 1}",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            if (url == group.effectiveCoverUrl) {
                                Surface(
                                    color = EmeraldPrimary,
                                    shape = RoundedCornerShape(bottomEnd = 8.dp),
                                    modifier = Modifier.align(Alignment.TopStart)
                                ) {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = "Cover Image",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .size(14.dp)
                                            .padding(2.dp)
                                    )
                                }
                            }
                        }
                    }

                    if (remainingCount > 0) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1.2f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(SurfaceContainerHigh)
                                .clickable(onClick = onViewGallery),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "+$remainingCount",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary
                                )
                                Text("more", fontSize = 10.sp, color = OnSurfaceVariantMuted)
                            }
                        }
                    }
                }
            } else {
                // Empty images banner
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(onClick = onAddImage),
                    color = SurfaceContainerLow,
                    border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add hotlinked images to this group", fontSize = 12.sp, color = EmeraldPrimary, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            HorizontalDivider(color = SurfaceContainerHigh)

            // Card Footer: Metadata and Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.Image, contentDescription = null, tint = OnSurfaceVariantMuted, modifier = Modifier.size(14.dp))
                    Text(
                        text = "${group.itemCount} images",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OnSurfaceDark
                    )
                    Text("•", fontSize = 12.sp, color = OnSurfaceVariantMuted)
                    Text(
                        text = dateString,
                        fontSize = 11.sp,
                        color = OnSurfaceVariantMuted
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = onAddImage,
                        modifier = Modifier.size(32.dp).testTag("card_add_image_${group.id}")
                    ) {
                        Icon(Icons.Default.AddPhotoAlternate, contentDescription = "Add Image", tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                    }
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(32.dp).testTag("card_edit_group_${group.id}")
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Group", tint = OnSurfaceVariantMuted, modifier = Modifier.size(17.dp))
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp).testTag("card_delete_group_${group.id}")
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Group", tint = Color(0xFFBA1A1A), modifier = Modifier.size(17.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CreateOrEditAssetGroupDialog(
    initialGroup: AssetGroup?,
    onDismiss: () -> Unit,
    onSave: (name: String, description: String, category: String, imageUrls: List<String>, tags: String) -> Unit
) {
    var name by remember { mutableStateOf(initialGroup?.name ?: "") }
    var description by remember { mutableStateOf(initialGroup?.description ?: "") }
    var category by remember { mutableStateOf(initialGroup?.category ?: AssetGroupCategories.ZAKAT_VERIFICATION) }
    var rawUrlsText by remember {
        mutableStateOf(initialGroup?.imageUrls?.joinToString("\n") ?: "")
    }
    var tags by remember { mutableStateOf(initialGroup?.tags ?: "") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("create_asset_group_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Dialog Title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (initialGroup == null) "Create Asset Group" else "Edit Asset Group",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceDark
                        )
                        Text(
                            text = "Saved in local Room SQLite database",
                            fontSize = 11.sp,
                            color = EmeraldPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                HorizontalDivider(color = SurfaceContainerHigh)

                // Group Name Field
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        if (errorMessage != null) errorMessage = null
                    },
                    label = { Text("Asset Group Name *") },
                    placeholder = { Text("e.g., 2026 Gold Bullion Vault") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_asset_group_name"),
                    singleLine = true,
                    isError = errorMessage != null,
                    supportingText = {
                        if (errorMessage != null) {
                            Text(errorMessage!!, color = MaterialTheme.colorScheme.error)
                        } else {
                            Text("A descriptive name for this collection")
                        }
                    },
                    shape = RoundedCornerShape(12.dp)
                )

                // Category Selection Chips
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Category",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OnSurfaceDark
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AssetGroupCategories.list.forEach { cat ->
                            val isSelected = category.equals(cat, ignoreCase = true)
                            Surface(
                                shape = RoundedCornerShape(100.dp),
                                color = if (isSelected) EmeraldPrimaryContainer else SurfaceContainerLow,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) EmeraldPrimary else OutlineBorder
                                ),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(100.dp))
                                    .clickable { category = cat }
                            ) {
                                Text(
                                    text = cat,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) EmeraldPrimary else OnSurfaceDark,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                }

                // Description Field
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (Optional)") },
                    placeholder = { Text("Notes on asset valuation, hawl date, or provenance...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_asset_group_description"),
                    maxLines = 3,
                    shape = RoundedCornerShape(12.dp)
                )

                // Hotlinked Image URLs
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Hotlinked Image URLs",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OnSurfaceDark
                        )
                        TextButton(
                            onClick = {
                                val samples = when (category) {
                                    AssetGroupCategories.GOLD_SILVER -> listOf(
                                        "https://images.unsplash.com/photo-1610375461246-83df859d849d?auto=format&fit=crop&w=800&q=80",
                                        "https://images.unsplash.com/photo-1624365168968-f283b5d96379?auto=format&fit=crop&w=800&q=80"
                                    )
                                    AssetGroupCategories.CHARITY_RECEIPTS -> listOf(
                                        "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?auto=format&fit=crop&w=800&q=80",
                                        "https://images.unsplash.com/photo-1532629345422-7515f3d16bb9?auto=format&fit=crop&w=800&q=80"
                                    )
                                    else -> listOf(
                                        "https://images.unsplash.com/photo-1586528116311-ad8dd3c8310d?auto=format&fit=crop&w=800&q=80",
                                        "https://images.unsplash.com/photo-1553413077-190dd305871c?auto=format&fit=crop&w=800&q=80"
                                    )
                                }
                                val current = rawUrlsText.lines().map { it.trim() }.filter { it.isNotBlank() }
                                val merged = (current + samples).distinct()
                                rawUrlsText = merged.joinToString("\n")
                            }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("Insert Sample URLs", fontSize = 11.sp)
                        }
                    }

                    OutlinedTextField(
                        value = rawUrlsText,
                        onValueChange = { rawUrlsText = it },
                        placeholder = { Text("Paste hotlinked image URLs (one per line)...\nhttps://example.com/vault-photo.jpg\nhttps://example.com/receipt.png") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp, max = 150.dp)
                            .testTag("input_asset_group_urls"),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Text(
                        text = "Enter direct hotlinked URLs starting with http:// or https://",
                        fontSize = 11.sp,
                        color = OnSurfaceVariantMuted
                    )
                }

                // Tags Field
                OutlinedTextField(
                    value = tags,
                    onValueChange = { tags = it },
                    label = { Text("Tags (Optional)") },
                    placeholder = { Text("e.g., gold, bullion, 2026, certified") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                // Dialog Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val trimmedName = name.trim()
                            if (trimmedName.isBlank()) {
                                errorMessage = "Group name is required"
                                return@Button
                            }
                            val urls = rawUrlsText.lines()
                                .map { it.trim() }
                                .filter { it.isNotBlank() }
                            onSave(trimmedName, description.trim(), category, urls, tags.trim())
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("save_asset_group_button")
                    ) {
                        Text(if (initialGroup == null) "Save to Vault" else "Update Group")
                    }
                }
            }
        }
    }
}

@Composable
fun AddImageUrlDialog(
    groupName: String,
    onDismiss: () -> Unit,
    onAddUrl: (String) -> Unit
) {
    var url by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("add_image_url_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Add Hotlinked Image", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("To: $groupName", fontSize = 12.sp, color = EmeraldPrimary)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                OutlinedTextField(
                    value = url,
                    onValueChange = {
                        url = it
                        if (error != null) error = null
                    },
                    label = { Text("Image Hotlink URL") },
                    placeholder = { Text("https://example.com/asset.jpg") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_single_image_url"),
                    singleLine = true,
                    isError = error != null,
                    supportingText = {
                        if (error != null) Text(error!!, color = MaterialTheme.colorScheme.error)
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Link, contentDescription = null, tint = EmeraldPrimary)
                    },
                    shape = RoundedCornerShape(12.dp)
                )

                // Quick presets
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Quick Sample Presets:", fontSize = 11.sp, color = OnSurfaceVariantMuted)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val presets = listOf(
                            "Gold Bars" to "https://images.unsplash.com/photo-1610375461246-83df859d849d?auto=format&fit=crop&w=800&q=80",
                            "Silver Coins" to "https://images.unsplash.com/photo-1589782182703-2aaa69037b5b?auto=format&fit=crop&w=800&q=80",
                            "Charity Aid" to "https://images.unsplash.com/photo-1532629345422-7515f3d16bb9?auto=format&fit=crop&w=800&q=80",
                            "Warehouse" to "https://images.unsplash.com/photo-1586528116311-ad8dd3c8310d?auto=format&fit=crop&w=800&q=80"
                        )
                        presets.forEach { (label, sampleUrl) ->
                            OutlinedButton(
                                onClick = { url = sampleUrl },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                shape = RoundedCornerShape(100.dp)
                            ) {
                                Text(label, fontSize = 11.sp)
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val trimmed = url.trim()
                            if (trimmed.isBlank() || (!trimmed.startsWith("http://") && !trimmed.startsWith("https://"))) {
                                error = "Please enter a valid HTTP/HTTPS image URL"
                                return@Button
                            }
                            onAddUrl(trimmed)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("confirm_add_image_url_button")
                    ) {
                        Text("Add to Group")
                    }
                }
            }
        }
    }
}

@Composable
fun AssetGroupGalleryModal(
    group: AssetGroup,
    onDismiss: () -> Unit,
    onAddImage: () -> Unit,
    onRemoveImage: (String) -> Unit,
    onSetCoverImage: (String) -> Unit,
    onImageClick: (String) -> Unit
) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize(0.92f)
                .testTag("asset_group_gallery_modal"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Modal Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                        Column {
                            Text(
                                text = group.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${group.imageUrls.size} hotlinked images • ${group.category}",
                                fontSize = 11.sp,
                                color = EmeraldPrimary
                            )
                        }
                    }

                    Button(
                        onClick = onAddImage,
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add URL", fontSize = 12.sp)
                    }
                }

                if (group.description.isNotBlank()) {
                    Text(
                        text = group.description,
                        fontSize = 12.sp,
                        color = OnSurfaceVariantMuted
                    )
                }

                HorizontalDivider(color = SurfaceContainerHigh)

                // Grid of Images
                if (group.imageUrls.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = OnSurfaceVariantMuted, modifier = Modifier.size(48.dp))
                            Text("No images in this collection yet", fontWeight = FontWeight.SemiBold, color = OnSurfaceDark)
                            Button(onClick = onAddImage, colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)) {
                                Text("Add First Hotlink Image")
                            }
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(group.imageUrls) { url ->
                            val isCover = url == group.effectiveCoverUrl
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLow),
                                border = androidx.compose.foundation.BorderStroke(
                                    if (isCover) 2.dp else 1.dp,
                                    if (isCover) EmeraldPrimary else OutlineBorder
                                )
                            ) {
                                Column {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(1f)
                                            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                                            .clickable { onImageClick(url) }
                                    ) {
                                        AsyncImage(
                                            model = url,
                                            contentDescription = "Hotlinked image item",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )

                                        if (isCover) {
                                            Surface(
                                                color = EmeraldPrimary,
                                                shape = RoundedCornerShape(bottomEnd = 8.dp),
                                                modifier = Modifier.align(Alignment.TopStart)
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                                ) {
                                                    Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(10.dp))
                                                    Text("Cover", fontSize = 9.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                                }
                                            }
                                        }

                                        // Expand preview icon button
                                        IconButton(
                                            onClick = { onImageClick(url) },
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .padding(4.dp)
                                                .size(26.dp)
                                                .clip(CircleShape)
                                                .background(Color.Black.copy(alpha = 0.5f))
                                        ) {
                                            Icon(Icons.Default.OpenInFull, contentDescription = "Expand", tint = Color.White, modifier = Modifier.size(14.dp))
                                        }
                                    }

                                    // Action bar below image
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 6.dp, vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(
                                            onClick = {
                                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                val clip = ClipData.newPlainText("Image URL", url)
                                                clipboard.setPrimaryClip(clip)
                                            },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy Link", tint = OnSurfaceVariantMuted, modifier = Modifier.size(15.dp))
                                        }

                                        if (!isCover) {
                                            IconButton(
                                                onClick = { onSetCoverImage(url) },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(Icons.Default.Star, contentDescription = "Set as Cover", tint = GoldSecondary, modifier = Modifier.size(16.dp))
                                            }
                                        }

                                        IconButton(
                                            onClick = { onRemoveImage(url) },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color(0xFFBA1A1A), modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HotlinkImagePreviewDialog(
    imageUrl: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var copied by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
                .testTag("hotlink_image_preview_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
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
                    Text("Image Inspection", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.25f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Full-size preview",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // URL & Copy Action
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SurfaceContainerLow,
                    border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = imageUrl,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f),
                            color = OnSurfaceVariantMuted
                        )
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Image URL", imageUrl)
                                clipboard.setPrimaryClip(clip)
                                copied = true
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                if (copied) Icons.Default.Check else Icons.Default.ContentCopy,
                                contentDescription = "Copy URL",
                                tint = if (copied) EmeraldPrimary else OnSurfaceVariantMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
