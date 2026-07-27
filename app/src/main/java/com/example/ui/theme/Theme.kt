package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = ImmersiveRed,
    onPrimary = Color.White,
    secondary = ImmersiveGreen,
    onSecondary = Color.Black,
    tertiary = ImmersiveBlue,
    background = ImmersiveBg,
    onBackground = ImmersiveTextWhite,
    surface = ImmersiveSurface,
    onSurface = ImmersiveTextWhite,
    surfaceVariant = ImmersiveCard,
    onSurfaceVariant = ImmersiveTextMuted,
    outline = ImmersiveBorder
)

private val LightColorScheme = darkColorScheme(
    primary = ImmersiveRed,
    onPrimary = Color.White,
    secondary = ImmersiveGreen,
    onSecondary = Color.Black,
    tertiary = ImmersiveBlue,
    background = ImmersiveBg,
    onBackground = ImmersiveTextWhite,
    surface = ImmersiveSurface,
    onSurface = ImmersiveTextWhite,
    surfaceVariant = ImmersiveCard,
    onSurfaceVariant = ImmersiveTextMuted,
    outline = ImmersiveBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force dark casino vibe for consistency
    dynamicColor: Boolean = false, // Keep exact Aviator colors
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
