package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.backend.models.ThemePreset

@Composable
fun RentWiseTheme(
    themePreset: ThemePreset = ThemePreset.RENTWISE_CYAN,
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val primaryColor = Color(themePreset.primaryColor)
    val secondaryColor = Color(themePreset.secondaryColor)
    val tertiaryColor = Color(themePreset.tertiaryColor)

    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = primaryColor,
            onPrimary = Color.Black,
            primaryContainer = secondaryColor.copy(alpha = 0.3f),
            onPrimaryContainer = Color.White,
            secondary = secondaryColor,
            onSecondary = Color.White,
            tertiary = tertiaryColor,
            onTertiary = Color.White,
            background = DarkBackground,
            onBackground = DarkTextPrimary,
            surface = DarkSurface,
            onSurface = DarkTextPrimary,
            surfaceVariant = DarkCard,
            onSurfaceVariant = DarkTextSecondary,
            outline = DarkBorder
        )
    } else {
        lightColorScheme(
            primary = primaryColor,
            onPrimary = Color.White,
            primaryContainer = primaryColor.copy(alpha = 0.15f),
            onPrimaryContainer = NavyDeep,
            secondary = secondaryColor,
            onSecondary = Color.White,
            tertiary = tertiaryColor,
            onTertiary = Color.White,
            background = OffWhite,
            onBackground = SlateTextPrimary,
            surface = SlateCardLight,
            onSurface = SlateTextPrimary,
            surfaceVariant = Color(0xFFF1F5F9),
            onSurfaceVariant = SlateTextSecondary,
            outline = SlateBorderLight
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
