package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.backend.models.AppLanguage
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.RentWiseTheme
import com.example.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainViewModel: MainViewModel = viewModel()
            val uiState by mainViewModel.uiState.collectAsState()

            RentWiseTheme(
                themePreset = uiState.currentTheme,
                darkTheme = uiState.isDarkMode
            ) {
                RentWiseApp(viewModel = mainViewModel)
            }
        }
    }
}

@Composable
fun RentWiseApp(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var currentTab by remember { mutableStateOf(ScreenTab.HOME) }
    var showNotificationDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }

    val isTamil = uiState.currentLanguage == AppLanguage.TAMIL

    Scaffold(
        topBar = {
            RentWiseTopBar(
                viewModel = viewModel,
                activeRole = uiState.activeRole,
                currentLanguage = uiState.currentLanguage,
                unreadNotifCount = uiState.unreadNotifCount,
                isDarkMode = uiState.isDarkMode,
                onOpenNotifications = { showNotificationDialog = true },
                onOpenLanguage = { showLanguageDialog = true },
                onOpenTheme = { showThemeDialog = true }
            )
        },
        bottomBar = {
            RentWiseBottomNav(
                currentTab = currentTab,
                onTabSelected = { currentTab = it },
                activeMaintenanceCount = uiState.activeMaintenanceCount,
                currentLanguage = uiState.currentLanguage
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                ScreenTab.HOME -> HomeScreen(
                    viewModel = viewModel,
                    uiState = uiState,
                    onNavigateToTab = { tab -> currentTab = tab }
                )
                ScreenTab.MAINTENANCE -> MaintenanceScreen(
                    viewModel = viewModel,
                    uiState = uiState
                )
                ScreenTab.PAYMENTS -> PaymentsScreen(
                    viewModel = viewModel,
                    uiState = uiState
                )
                ScreenTab.PROFILE -> ProfileScreen(
                    viewModel = viewModel,
                    uiState = uiState,
                    onOpenTheme = { showThemeDialog = true },
                    onOpenLanguage = { showLanguageDialog = true }
                )
            }
        }
    }

    // Top-level Dialogs
    if (showNotificationDialog) {
        NotificationDialog(
            notifications = uiState.notifications,
            isTamil = isTamil,
            onMarkAllRead = { viewModel.markNotificationsAsRead() },
            onDismiss = { showNotificationDialog = false }
        )
    }

    if (showLanguageDialog) {
        LanguageSelectorDialog(
            currentLanguage = uiState.currentLanguage,
            onSelectLanguage = { viewModel.setLanguage(it) },
            onDismiss = { showLanguageDialog = false }
        )
    }

    if (showThemeDialog) {
        ThemeSelectorDialog(
            currentTheme = uiState.currentTheme,
            isDarkMode = uiState.isDarkMode,
            currentLanguage = uiState.currentLanguage,
            onSelectTheme = { viewModel.setTheme(it) },
            onToggleDarkMode = { viewModel.toggleDarkMode() },
            onDismiss = { showThemeDialog = false }
        )
    }
}
