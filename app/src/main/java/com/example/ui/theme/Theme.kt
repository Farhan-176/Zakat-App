package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val SereneColorScheme = lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = EmeraldOnPrimary,
    primaryContainer = EmeraldPrimaryContainer,
    onPrimaryContainer = EmeraldOnPrimaryContainer,
    inversePrimary = EmeraldPrimaryFixedDim,

    secondary = GoldSecondary,
    onSecondary = GoldOnSecondary,
    secondaryContainer = GoldSecondaryContainer,
    onSecondaryContainer = GoldOnSecondaryContainer,

    tertiary = SageTertiary,
    onTertiary = SageOnTertiary,
    tertiaryContainer = SageTertiaryContainer,
    onTertiaryContainer = SageOnTertiaryContainer,

    background = SurfaceMint,
    onBackground = OnSurfaceDark,

    surface = SurfaceMint,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceContainerHighest,
    onSurfaceVariant = OnSurfaceVariantMuted,

    surfaceContainerLowest = SurfaceContainerLowest,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainer = SurfaceContainerDefault,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest,

    outline = OutlineBorder,
    outlineVariant = OutlineVariantBorder,

    error = ErrorRed,
    onError = EmeraldOnPrimary,
    errorContainer = ErrorContainerRed,
    onErrorContainer = OnErrorContainerRed,

    inverseSurface = InverseSurfaceDark,
    inverseOnSurface = InverseOnSurfaceLight
)

private val DarkSereneColorScheme = darkColorScheme(
    primary = EmeraldPrimaryDark,
    onPrimary = EmeraldOnPrimaryDark,
    primaryContainer = EmeraldPrimaryContainerDark,
    onPrimaryContainer = EmeraldOnPrimaryContainerDark,
    inversePrimary = EmeraldPrimary,

    secondary = GoldSecondaryDark,
    onSecondary = GoldOnSecondaryDark,
    secondaryContainer = GoldSecondaryContainerDark,
    onSecondaryContainer = GoldOnSecondaryContainerDark,

    tertiary = SageTertiaryDark,
    onTertiary = SageOnTertiaryDark,
    tertiaryContainer = SageTertiaryContainerDark,
    onTertiaryContainer = SageOnTertiaryContainerDark,

    background = DarkSurfaceBackground,
    onBackground = DarkOnSurface,

    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceContainerHighest,
    onSurfaceVariant = DarkOnSurfaceVariant,

    surfaceContainerLowest = DarkSurfaceContainerLowest,
    surfaceContainerLow = DarkSurfaceContainerLow,
    surfaceContainer = DarkSurfaceContainerDefault,
    surfaceContainerHigh = DarkSurfaceContainerHigh,
    surfaceContainerHighest = DarkSurfaceContainerHighest,

    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,

    error = DarkErrorRed,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,

    inverseSurface = SurfaceContainerLowest,
    inverseOnSurface = OnSurfaceDark
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkSereneColorScheme else SereneColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

