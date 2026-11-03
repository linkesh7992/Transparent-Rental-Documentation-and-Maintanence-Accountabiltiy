package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.backend.models.AppLanguage

enum class ScreenTab(val english: String, val tamil: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector) {
    HOME("Home", "முகப்பு", Icons.Filled.Home, Icons.Outlined.Home),
    MAINTENANCE("Maintenance", "பராமரிப்பு", Icons.Filled.Build, Icons.Outlined.Build),
    PAYMENTS("Payments", "கட்டணம்", Icons.Filled.AccountBalanceWallet, Icons.Outlined.AccountBalanceWallet),
    PROFILE("Profile", "சுயவிவரம்", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
fun RentWiseBottomNav(
    currentTab: ScreenTab,
    onTabSelected: (ScreenTab) -> Unit,
    activeMaintenanceCount: Int,
    currentLanguage: AppLanguage
) {
    NavigationBar(
        tonalElevation = 8.dp,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        ScreenTab.values().forEach { tab ->
            val isSelected = currentTab == tab
            val label = if (currentLanguage == AppLanguage.TAMIL) tab.tamil else tab.english

            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    if (tab == ScreenTab.MAINTENANCE && activeMaintenanceCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(containerColor = MaterialTheme.colorScheme.error) {
                                    Text(text = "$activeMaintenanceCount")
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = label
                            )
                        }
                    } else {
                        Icon(
                            imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                            contentDescription = label
                        )
                    }
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
            )
        }
    }
}
