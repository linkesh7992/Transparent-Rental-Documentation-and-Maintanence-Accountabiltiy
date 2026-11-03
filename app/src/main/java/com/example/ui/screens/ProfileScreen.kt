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
import com.example.ui.components.EditProfileDialog
import com.example.ui.components.GovIdVerificationDialog
import com.example.ui.components.PropertyDetailModal
import com.example.viewmodel.MainUiState
import com.example.viewmodel.MainViewModel

@Composable
fun ProfileScreen(
    viewModel: MainViewModel,
    uiState: MainUiState,
    onOpenTheme: () -> Unit,
    onOpenLanguage: () -> Unit
) {
    val isTamil = uiState.currentLanguage == AppLanguage.TAMIL
    val currentUser = uiState.currentUser

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showGovIdDialog by remember { mutableStateOf(false) }
    var selectedSavedProperty by remember { mutableStateOf<Property?>(null) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showSupportDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
    ) {
        // 1. User Profile Header Card
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            MaterialTheme.colorScheme.primary,
                                            Color(0xFF0077B6)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (uiState.activeRole == UserRole.TENANT) Icons.Default.Person else Icons.Default.Apartment,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = currentUser?.name ?: "Arun Kumar",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                if (currentUser?.isGovtIdVerified == true) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "Verified ID",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Text(
                                text = currentUser?.email ?: "arun.kumar@rentwise.in",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = currentUser?.phone ?: "+91 98840 12345",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = uiState.activeRole.getLocalized(isTamil),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        IconButton(
                            onClick = { showEditProfileDialog = true },
                            modifier = Modifier.testTag("edit_profile_icon_button")
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = MaterialTheme.colorScheme.primary)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(10.dp))

                    // Gov ID Verification Status Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (currentUser?.isGovtIdVerified == true) Icons.Default.VerifiedUser else Icons.Default.GppMaybe,
                                contentDescription = null,
                                tint = if (currentUser?.isGovtIdVerified == true) MaterialTheme.colorScheme.primary else Color(0xFFFF9F1C)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = if (isTamil) "அரசு அடையாள சரிபார்ப்பு" else "Govt. ID Verification",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = if (currentUser?.isGovtIdVerified == true) "Aadhaar Verified (XXXX-1234)" else "Verification Pending",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (currentUser?.isGovtIdVerified != true) {
                            FilledTonalButton(
                                onClick = { showGovIdDialog = true },
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                modifier = Modifier.height(30.dp).testTag("verify_id_cta")
                            ) {
                                Text(if (isTamil) "சரிபார்" else "Verify Now", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        // 2. Saved Properties Carousel
        if (uiState.savedProperties.isNotEmpty()) {
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isTamil) "சேமிக்கப்பட்ட வீடுகள்" else "Saved Properties",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${uiState.savedProperties.size} saved",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(uiState.savedProperties) { prop ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surface,
                                shadowElevation = 1.dp,
                                modifier = Modifier
                                    .width(180.dp)
                                    .clickable { selectedSavedProperty = prop }
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        text = prop.getDisplayTitle(isTamil),
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "₹${prop.price.toInt()}/mo",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                    Text(
                                        text = "${prop.bedrooms} BHK • ${prop.city}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. App Settings Section (Theme, Colors, Language)
        item {
            Column {
                Text(
                    text = if (isTamil) "செயலி அமைப்புகள் & வண்ணங்கள்" else "Customization & App Settings",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        ProfileSettingRow(
                            icon = Icons.Default.Palette,
                            title = if (isTamil) "வண்ண தீம் & பயன்முறை" else "Theme & Color Palette",
                            subtitle = "${uiState.currentTheme.getLocalized(isTamil)} (${if (uiState.isDarkMode) "Dark" else "Light"})",
                            onClick = onOpenTheme,
                            testTag = "settings_theme_row"
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                        ProfileSettingRow(
                            icon = Icons.Default.Language,
                            title = if (isTamil) "மொழி மாற்றம்" else "App Language",
                            subtitle = uiState.currentLanguage.nativeName,
                            onClick = onOpenLanguage,
                            testTag = "settings_lang_row"
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                        ProfileSettingRow(
                            icon = Icons.Default.Lock,
                            title = if (isTamil) "கடவுச்சொல் & பாதுகாப்பு" else "Security & Password",
                            subtitle = "Last changed 14 days ago",
                            onClick = {},
                            testTag = "settings_security_row"
                        )
                    }
                }
            }
        }

        // 4. Help, Support & About RentWise
        item {
            Column {
                Text(
                    text = if (isTamil) "உதவி & ஆதரவு" else "Help, Policy & About",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        ProfileSettingRow(
                            icon = Icons.Default.SupportAgent,
                            title = if (isTamil) "வாடிக்கையாளர் உதவி மையம்" else "24/7 Support & Helpdesk",
                            subtitle = "support@rentwise.in • 1800-RENT-WISE",
                            onClick = { showSupportDialog = true },
                            testTag = "settings_support_row"
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                        ProfileSettingRow(
                            icon = Icons.Default.Policy,
                            title = if (isTamil) "விதிமுறைகள் & தனியுரிமை கொள்கை" else "Terms of Service & Privacy Policy",
                            subtitle = "Version 2.4.0 (Tamil Nadu Tenancy Act Compliant)",
                            onClick = {},
                            testTag = "settings_policy_row"
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                        ProfileSettingRow(
                            icon = Icons.Default.Info,
                            title = if (isTamil) "RentWise பற்றி" else "About RentWise Platform",
                            subtitle = "Smart Object-Oriented Rental Management System",
                            onClick = { showAboutDialog = true },
                            testTag = "settings_about_row"
                        )
                    }
                }
            }
        }

        // 5. Switch Role / Log Out
        item {
            Button(
                onClick = {
                    viewModel.switchRole(if (uiState.activeRole == UserRole.TENANT) UserRole.LANDLORD else UserRole.TENANT)
                },
                modifier = Modifier.fillMaxWidth().testTag("profile_switch_role_button")
            ) {
                Icon(Icons.Default.SwapHoriz, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isTamil) {
                        if (uiState.activeRole == UserRole.TENANT) "உரிமையாளர் பயன்முறைக்கு மாறு (Switch to Landlord)"
                        else "குடியிருப்பாளர் பயன்முறைக்கு மாறு (Switch to Tenant)"
                    } else {
                        if (uiState.activeRole == UserRole.TENANT) "Switch to Landlord Mode"
                        else "Switch to Tenant Mode"
                    }
                )
            }
        }
    }

    // Edit Profile Modal
    if (showEditProfileDialog && currentUser != null) {
        EditProfileDialog(
            user = currentUser,
            isTamil = isTamil,
            onSaveProfile = { name, email, phone, address ->
                viewModel.updateUserProfile(name, email, phone, address)
            },
            onDismiss = { showEditProfileDialog = false }
        )
    }

    // Gov ID Verification Modal
    if (showGovIdDialog) {
        GovIdVerificationDialog(
            isTamil = isTamil,
            onVerify = { docType ->
                viewModel.verifyGovernmentId(docType)
            },
            onDismiss = { showGovIdDialog = false }
        )
    }

    // Saved Property Modal
    selectedSavedProperty?.let { prop ->
        PropertyDetailModal(
            property = prop,
            isTamil = isTamil,
            isSaved = true,
            onToggleSave = {
                viewModel.toggleSaveProperty(prop.id)
                selectedSavedProperty = null
            },
            onBookInquire = { selectedSavedProperty = null },
            onDismiss = { selectedSavedProperty = null }
        )
    }

    // About Dialog
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.HomeWork, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("RentWise v2.4.0", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (isTamil) "RentWise - நவீன வாடகை மற்றும் பராமரிப்பு மேலாண்மை தளம்." else "RentWise is a comprehensive rental management platform built for modern tenants and landlords.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "• 9-Stage Maintenance Lifecycle with SLA Tracking\n• Multi-Channel Dual Digital Payment Receipts\n• Multi-Theme & Color Customization\n• English & தமிழ் (Tamil) Localization\n• Government ID & Tenancy Compliance",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(onClick = { showAboutDialog = false }) { Text("OK") }
            }
        )
    }

    // Support Dialog
    if (showSupportDialog) {
        AlertDialog(
            onDismissRequest = { showSupportDialog = false },
            title = { Text(if (isTamil) "24/7 வாடிக்கையாளர் உதவி" else "RentWise Support") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Helpline: 1800-RENT-WISE (Toll Free)")
                    Text("Email: support@rentwise.in")
                    Text("Emergency Maintenance: +91 94440 99999")
                    Text("Working Hours: 24 Hours, 7 Days a Week")
                }
            },
            confirmButton = {
                Button(onClick = { showSupportDialog = false }) { Text("Got it") }
            }
        )
    }
}

@Composable
private fun ProfileSettingRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(14.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
            Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
