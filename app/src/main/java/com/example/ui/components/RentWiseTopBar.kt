package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.backend.models.AppLanguage
import com.example.backend.models.UserRole
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentWiseTopBar(
    viewModel: MainViewModel,
    activeRole: UserRole,
    currentLanguage: AppLanguage,
    unreadNotifCount: Int,
    isDarkMode: Boolean,
    onOpenNotifications: () -> Unit,
    onOpenLanguage: () -> Unit,
    onOpenTheme: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Brand Header with Icon & Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { /* Brand click */ }
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        Color(0xFF0B132B),
                                        Color(0xFF00B4D8)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.HomeWork,
                            contentDescription = "RentWise Logo",
                            tint = Color(0xFF00E5FF),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "RentWise",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 20.sp,
                                    letterSpacing = (-0.5).sp
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(
                            text = if (currentLanguage == AppLanguage.TAMIL) "வாடகை மேலாண்மை" else "Smart Rental OS",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Action Icons (Role Toggle, Language, Theme, Notifications)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Language Chip (Quick switch)
                    OutlinedButton(
                        onClick = onOpenLanguage,
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.height(32.dp).testTag("language_switch_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Translate,
                            contentDescription = "Language",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (currentLanguage == AppLanguage.TAMIL) "தமிழ்" else "EN",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    // Theme Palette Button
                    IconButton(
                        onClick = onOpenTheme,
                        modifier = Modifier.size(36.dp).testTag("theme_selector_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Palette,
                            contentDescription = "Themes",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Notification Button with Badge
                    IconButton(
                        onClick = onOpenNotifications,
                        modifier = Modifier.size(36.dp).testTag("notification_button")
                    ) {
                        BadgedBox(
                            badge = {
                                if (unreadNotifCount > 0) {
                                    Badge(containerColor = MaterialTheme.colorScheme.error) {
                                        Text(text = "$unreadNotifCount")
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "Notifications",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Role Switcher Bar (Prominent & High-Contrast)
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(42.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tenant Segment
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(9.dp))
                            .background(
                                if (activeRole == UserRole.TENANT) MaterialTheme.colorScheme.primary
                                else Color.Transparent
                            )
                            .clickable { viewModel.switchRole(UserRole.TENANT) }
                            .testTag("switch_to_tenant_role"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = null,
                                tint = if (activeRole == UserRole.TENANT) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (currentLanguage == AppLanguage.TAMIL) "குடியிருப்பாளர் (Tenant)" else "Tenant Mode",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (activeRole == UserRole.TENANT) FontWeight.Bold else FontWeight.Medium
                                ),
                                color = if (activeRole == UserRole.TENANT) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Landlord Segment
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(9.dp))
                            .background(
                                if (activeRole == UserRole.LANDLORD) MaterialTheme.colorScheme.primary
                                else Color.Transparent
                            )
                            .clickable { viewModel.switchRole(UserRole.LANDLORD) }
                            .testTag("switch_to_landlord_role"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Apartment,
                                contentDescription = null,
                                tint = if (activeRole == UserRole.LANDLORD) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (currentLanguage == AppLanguage.TAMIL) "உரிமையாளர் (Landlord)" else "Landlord Mode",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (activeRole == UserRole.LANDLORD) FontWeight.Bold else FontWeight.Medium
                                ),
                                color = if (activeRole == UserRole.LANDLORD) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
