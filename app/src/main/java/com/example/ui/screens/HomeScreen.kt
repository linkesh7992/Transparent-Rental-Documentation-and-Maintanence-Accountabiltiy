package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.backend.models.*
import com.example.ui.components.AddPropertyDialog
import com.example.ui.components.NoticeDetailDialog
import com.example.ui.components.PropertyDetailModal
import com.example.viewmodel.MainUiState
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    uiState: MainUiState,
    onNavigateToTab: (com.example.ui.components.ScreenTab) -> Unit
) {
    val isTamil = uiState.currentLanguage == AppLanguage.TAMIL
    var searchQuery by remember { mutableStateOf("") }
    var selectedPropertyType by remember { mutableStateOf("All") }
    var selectedPropertyForModal by remember { mutableStateOf<Property?>(null) }
    var selectedNoticeForModal by remember { mutableStateOf<NoticeItem?>(null) }
    var showAddPropertyDialog by remember { mutableStateOf(false) }

    val isTenant = uiState.activeRole == UserRole.TENANT
    val currentUser = uiState.currentUser

    // Filter properties for discovery
    val filteredProperties = remember(uiState.properties, searchQuery, selectedPropertyType) {
        uiState.properties.filter { p ->
            val matchesQuery = searchQuery.isBlank() ||
                    p.title.contains(searchQuery, ignoreCase = true) ||
                    p.titleTamil.contains(searchQuery, ignoreCase = true) ||
                    p.address.contains(searchQuery, ignoreCase = true) ||
                    p.city.contains(searchQuery, ignoreCase = true)

            val matchesType = selectedPropertyType == "All" || p.propertyType.equals(selectedPropertyType, ignoreCase = true)
            matchesQuery && matchesType
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        // 1. Welcome Header & User Badge
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.secondary
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isTenant) Icons.Default.Person else Icons.Default.Apartment,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isTamil) "வணக்கம்," else "Welcome Back,",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = currentUser?.name ?: "Arun Kumar",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Text(
                                        text = uiState.activeRole.getLocalized(isTamil),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                if (currentUser?.isGovtIdVerified == true) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "Verified",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Quick Switch Role Button
                    FilledTonalButton(
                        onClick = {
                            viewModel.switchRole(if (isTenant) UserRole.LANDLORD else UserRole.TENANT)
                        },
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(34.dp).testTag("quick_switch_role_header_button")
                    ) {
                        Icon(Icons.Default.SwapHoriz, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isTamil) "மாற்று" else "Switch", fontSize = 11.sp)
                    }
                }
            }
        }

        // 2. Search & Discovery Filters
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(if (isTamil) "இடம், பட்ஜெட், வகை மூலம் தேடுங்கள்..." else "Search by location, budget, type...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.primary) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().testTag("home_search_bar"),
                    singleLine = true
                )

                // Property Type Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val types = listOf("All", "Apartment", "Villa", "Independent House", "Studio")
                    items(types) { type ->
                        FilterChip(
                            selected = selectedPropertyType == type,
                            onClick = { selectedPropertyType = type },
                            label = { Text(type, fontSize = 12.sp) }
                        )
                    }
                }
            }
        }

        // 3. Role-Based Dashboard Summary (Page 2 Requirement)
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isTamil) "டாஷ்போர்டு சுருக்கம்" else "Dashboard Summary",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Icon(
                            imageVector = if (isTenant) Icons.Default.Dashboard else Icons.Default.Assessment,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (isTenant) {
                        // Tenant Summary Metrics
                        val tenant = currentUser as? Tenant
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            SummaryMetric("Current Rent", "₹${tenant?.monthlyRent?.toInt() ?: 24500}", Icons.Default.AccountBalanceWallet)
                            SummaryMetric("Next Due Date", tenant?.nextDueDate ?: "05 Oct 2026", Icons.Default.CalendarToday)
                            SummaryMetric("Active Issues", "${uiState.activeMaintenanceCount}", Icons.Default.Build)
                        }
                    } else {
                        // Landlord Summary Metrics
                        val landlord = currentUser as? Landlord
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            SummaryMetric("Properties", "${landlord?.totalProperties ?: 6}", Icons.Default.Apartment)
                            SummaryMetric("Occupied", "${landlord?.occupiedRooms ?: 5}", Icons.Default.MeetingRoom)
                            SummaryMetric("Monthly Rev", "₹${(landlord?.monthlyRevenue?.toInt() ?: 148000) / 1000}k", Icons.Default.TrendingUp)
                        }
                    }
                }
            }
        }

        // 4. Quick Actions Row
        item {
            Column {
                Text(
                    text = if (isTamil) "விரைவுச் செயல்கள்" else "Quick Actions",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickActionCard(
                        title = if (isTamil) "வீடு தேடு" else "Search House",
                        icon = Icons.Default.Search,
                        modifier = Modifier.weight(1f),
                        onClick = { searchQuery = "Chennai" }
                    )

                    if (!isTenant) {
                        QuickActionCard(
                            title = if (isTamil) "சொத்து சேர்" else "Add Property",
                            icon = Icons.Default.AddHome,
                            modifier = Modifier.weight(1f),
                            onClick = { showAddPropertyDialog = true }
                        )
                        QuickActionCard(
                            title = if (isTamil) "பட்டியல் நிர்வகி" else "Manage Listings",
                            icon = Icons.Default.ListAlt,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedPropertyType = "Apartment" }
                        )
                    } else {
                        QuickActionCard(
                            title = if (isTamil) "புகார் பதிவு" else "Raise Issue",
                            icon = Icons.Default.Build,
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigateToTab(com.example.ui.components.ScreenTab.MAINTENANCE) }
                        )
                        QuickActionCard(
                            title = if (isTamil) "வாடகை செலுத்து" else "Pay Rent",
                            icon = Icons.Default.Payment,
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigateToTab(com.example.ui.components.ScreenTab.PAYMENTS) }
                        )
                    }

                    QuickActionCard(
                        title = if (isTamil) "சேமித்தவை" else "Saved",
                        icon = Icons.Default.Bookmark,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToTab(com.example.ui.components.ScreenTab.PROFILE) }
                    )
                }
            }
        }

        // 5. Announcements & Notice Center (Pages 2 & 7 Requirements)
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isTamil) "அறிவிப்பு மையம் & எச்சரிக்கைகள்" else "Notice Center & Reminders",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "${uiState.notices.size} active",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(uiState.notices) { notice ->
                        NoticeCard(
                            notice = notice,
                            isTamil = isTamil,
                            onClick = { selectedNoticeForModal = notice }
                        )
                    }
                }
            }
        }

        // 6. Property Discovery: Recommended Properties
        item {
            Column {
                Text(
                    text = if (isTamil) "பரிந்துரைக்கப்பட்ட வீடுகள்" else "Recommended Properties",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val recommended = filteredProperties.filter { it.isRecommended }
                    items(recommended) { prop ->
                        PropertyCard(
                            property = prop,
                            isTamil = isTamil,
                            isSaved = uiState.savedProperties.any { it.id == prop.id },
                            onToggleSave = { viewModel.toggleSaveProperty(prop.id) },
                            onClick = { selectedPropertyForModal = prop }
                        )
                    }
                }
            }
        }

        // 7. Property Discovery: Nearby & Verified Properties
        item {
            Column {
                Text(
                    text = if (isTamil) "அருகிலுள்ள & சரிபார்க்கப்பட்ட வீடுகள்" else "Nearby & Verified Properties",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    val nearbyOrVerified = filteredProperties.filter { it.isNearby || it.isVerified }
                    nearbyOrVerified.forEach { prop ->
                        PropertyHorizontalCard(
                            property = prop,
                            isTamil = isTamil,
                            isSaved = uiState.savedProperties.any { it.id == prop.id },
                            onToggleSave = { viewModel.toggleSaveProperty(prop.id) },
                            onClick = { selectedPropertyForModal = prop }
                        )
                    }
                }
            }
        }
    }

    // Modals
    selectedPropertyForModal?.let { prop ->
        PropertyDetailModal(
            property = prop,
            isTamil = isTamil,
            isSaved = uiState.savedProperties.any { it.id == prop.id },
            onToggleSave = { viewModel.toggleSaveProperty(prop.id) },
            onBookInquire = { selectedPropertyForModal = null },
            onDismiss = { selectedPropertyForModal = null }
        )
    }

    selectedNoticeForModal?.let { notice ->
        NoticeDetailDialog(
            notice = notice,
            isTamil = isTamil,
            activeRole = uiState.activeRole,
            onAcknowledge = { viewModel.acknowledgeNotice(notice.id, isTenant) },
            onDismiss = { selectedNoticeForModal = null }
        )
    }

    if (showAddPropertyDialog) {
        AddPropertyDialog(
            isTamil = isTamil,
            landlordName = currentUser?.name ?: "Suresh Ramanathan",
            landlordPhone = currentUser?.phone ?: "+91 94440 98765",
            onAddProperty = { viewModel.addProperty(it) },
            onDismiss = { showAddPropertyDialog = false }
        )
    }
}

@Composable
private fun SummaryMetric(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold))
        Text(label, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.SemiBold),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun NoticeCard(notice: NoticeItem, isTamil: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.width(220.dp).clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "${notice.reminderDays}d Reminder",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
                Text(
                    text = notice.createdAt,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = notice.title,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = notice.content,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )
        }
    }
}

@Composable
private fun PropertyCard(
    property: Property,
    isTamil: Boolean,
    isSaved: Boolean,
    onToggleSave: () -> Unit,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
        modifier = Modifier.width(220.dp).clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // Placeholder Image Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF0B132B), Color(0xFF1C2541), Color(0xFF0077B6))
                        )
                    )
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Text(
                            text = property.propertyType,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White),
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                    IconButton(
                        onClick = onToggleSave,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Save",
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = property.getDisplayTitle(isTamil),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 1
            )
            Text(
                text = "${property.bedrooms} BHK • ${property.squareFeet} sq.ft",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "₹${property.price.toInt()}/mo",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFFFB703), modifier = Modifier.size(12.dp))
                    Text(" ${property.rating}", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}

@Composable
private fun PropertyHorizontalCard(
    property: Property,
    isTamil: Boolean,
    isSaved: Boolean,
    onToggleSave: () -> Unit,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF0F172A)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.HomeWork, contentDescription = null, tint = Color(0xFF00B4D8), modifier = Modifier.size(32.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = property.getDisplayTitle(isTamil),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                    if (property.isVerified) {
                        Icon(Icons.Default.Verified, contentDescription = "Verified", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    }
                }
                Text(
                    text = "${property.address}, ${property.city}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹${property.price.toInt()}/mo",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                    )
                    Text(
                        text = "${property.bedrooms} BHK • ${property.furnishingStatus}",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
