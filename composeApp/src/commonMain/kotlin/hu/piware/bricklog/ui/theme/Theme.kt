package hu.piware.bricklog.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import hu.piware.bricklog.feature.settings.domain.model.ThemeOption

@Immutable
data class ExtendedColorScheme(
    val future: ColorFamily,
    val futureVariant: ColorFamily,
    val active: ColorFamily,
    val discontinued: ColorFamily,
    val discontinuedVariant: ColorFamily,
)

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

private val mediumContrastLightColorScheme = lightColorScheme(
    primary = primaryLightMediumContrast,
    onPrimary = onPrimaryLightMediumContrast,
    primaryContainer = primaryContainerLightMediumContrast,
    onPrimaryContainer = onPrimaryContainerLightMediumContrast,
    secondary = secondaryLightMediumContrast,
    onSecondary = onSecondaryLightMediumContrast,
    secondaryContainer = secondaryContainerLightMediumContrast,
    onSecondaryContainer = onSecondaryContainerLightMediumContrast,
    tertiary = tertiaryLightMediumContrast,
    onTertiary = onTertiaryLightMediumContrast,
    tertiaryContainer = tertiaryContainerLightMediumContrast,
    onTertiaryContainer = onTertiaryContainerLightMediumContrast,
    error = errorLightMediumContrast,
    onError = onErrorLightMediumContrast,
    errorContainer = errorContainerLightMediumContrast,
    onErrorContainer = onErrorContainerLightMediumContrast,
    background = backgroundLightMediumContrast,
    onBackground = onBackgroundLightMediumContrast,
    surface = surfaceLightMediumContrast,
    onSurface = onSurfaceLightMediumContrast,
    surfaceVariant = surfaceVariantLightMediumContrast,
    onSurfaceVariant = onSurfaceVariantLightMediumContrast,
    outline = outlineLightMediumContrast,
    outlineVariant = outlineVariantLightMediumContrast,
    scrim = scrimLightMediumContrast,
    inverseSurface = inverseSurfaceLightMediumContrast,
    inverseOnSurface = inverseOnSurfaceLightMediumContrast,
    inversePrimary = inversePrimaryLightMediumContrast,
    surfaceDim = surfaceDimLightMediumContrast,
    surfaceBright = surfaceBrightLightMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
    surfaceContainerLow = surfaceContainerLowLightMediumContrast,
    surfaceContainer = surfaceContainerLightMediumContrast,
    surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
)

private val highContrastLightColorScheme = lightColorScheme(
    primary = primaryLightHighContrast,
    onPrimary = onPrimaryLightHighContrast,
    primaryContainer = primaryContainerLightHighContrast,
    onPrimaryContainer = onPrimaryContainerLightHighContrast,
    secondary = secondaryLightHighContrast,
    onSecondary = onSecondaryLightHighContrast,
    secondaryContainer = secondaryContainerLightHighContrast,
    onSecondaryContainer = onSecondaryContainerLightHighContrast,
    tertiary = tertiaryLightHighContrast,
    onTertiary = onTertiaryLightHighContrast,
    tertiaryContainer = tertiaryContainerLightHighContrast,
    onTertiaryContainer = onTertiaryContainerLightHighContrast,
    error = errorLightHighContrast,
    onError = onErrorLightHighContrast,
    errorContainer = errorContainerLightHighContrast,
    onErrorContainer = onErrorContainerLightHighContrast,
    background = backgroundLightHighContrast,
    onBackground = onBackgroundLightHighContrast,
    surface = surfaceLightHighContrast,
    onSurface = onSurfaceLightHighContrast,
    surfaceVariant = surfaceVariantLightHighContrast,
    onSurfaceVariant = onSurfaceVariantLightHighContrast,
    outline = outlineLightHighContrast,
    outlineVariant = outlineVariantLightHighContrast,
    scrim = scrimLightHighContrast,
    inverseSurface = inverseSurfaceLightHighContrast,
    inverseOnSurface = inverseOnSurfaceLightHighContrast,
    inversePrimary = inversePrimaryLightHighContrast,
    surfaceDim = surfaceDimLightHighContrast,
    surfaceBright = surfaceBrightLightHighContrast,
    surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
    surfaceContainerLow = surfaceContainerLowLightHighContrast,
    surfaceContainer = surfaceContainerLightHighContrast,
    surfaceContainerHigh = surfaceContainerHighLightHighContrast,
    surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
)

private val mediumContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkMediumContrast,
    onPrimary = onPrimaryDarkMediumContrast,
    primaryContainer = primaryContainerDarkMediumContrast,
    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
    secondary = secondaryDarkMediumContrast,
    onSecondary = onSecondaryDarkMediumContrast,
    secondaryContainer = secondaryContainerDarkMediumContrast,
    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
    tertiary = tertiaryDarkMediumContrast,
    onTertiary = onTertiaryDarkMediumContrast,
    tertiaryContainer = tertiaryContainerDarkMediumContrast,
    onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
    error = errorDarkMediumContrast,
    onError = onErrorDarkMediumContrast,
    errorContainer = errorContainerDarkMediumContrast,
    onErrorContainer = onErrorContainerDarkMediumContrast,
    background = backgroundDarkMediumContrast,
    onBackground = onBackgroundDarkMediumContrast,
    surface = surfaceDarkMediumContrast,
    onSurface = onSurfaceDarkMediumContrast,
    surfaceVariant = surfaceVariantDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
    outline = outlineDarkMediumContrast,
    outlineVariant = outlineVariantDarkMediumContrast,
    scrim = scrimDarkMediumContrast,
    inverseSurface = inverseSurfaceDarkMediumContrast,
    inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
    inversePrimary = inversePrimaryDarkMediumContrast,
    surfaceDim = surfaceDimDarkMediumContrast,
    surfaceBright = surfaceBrightDarkMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
    surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
    surfaceContainer = surfaceContainerDarkMediumContrast,
    surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
)

private val highContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    secondaryContainer = secondaryContainerDarkHighContrast,
    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    tertiaryContainer = tertiaryContainerDarkHighContrast,
    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
    error = errorDarkHighContrast,
    onError = onErrorDarkHighContrast,
    errorContainer = errorContainerDarkHighContrast,
    onErrorContainer = onErrorContainerDarkHighContrast,
    background = backgroundDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast,
    surface = surfaceDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    surfaceVariant = surfaceVariantDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineVariantDarkHighContrast,
    scrim = scrimDarkHighContrast,
    inverseSurface = inverseSurfaceDarkHighContrast,
    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
    inversePrimary = inversePrimaryDarkHighContrast,
    surfaceDim = surfaceDimDarkHighContrast,
    surfaceBright = surfaceBrightDarkHighContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
    surfaceContainer = surfaceContainerDarkHighContrast,
    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
)

val extendedLight = ExtendedColorScheme(
    future = ColorFamily(
        futureLight,
        onFutureLight,
        futureContainerLight,
        onFutureContainerLight,
    ),
    futureVariant = ColorFamily(
        futureVariantLight,
        onFutureVariantLight,
        futureVariantContainerLight,
        onFutureVariantContainerLight,
    ),
    active = ColorFamily(
        activeLight,
        onActiveLight,
        activeContainerLight,
        onActiveContainerLight,
    ),
    discontinued = ColorFamily(
        discontinuedLight,
        onDiscontinuedLight,
        discontinuedContainerLight,
        onDiscontinuedContainerLight,
    ),
    discontinuedVariant = ColorFamily(
        discontinuedVariantLight,
        onDiscontinuedVariantLight,
        discontinuedVariantContainerLight,
        onDiscontinuedVariantContainerLight,
    ),
)

val extendedDark = ExtendedColorScheme(
    future = ColorFamily(
        futureDark,
        onFutureDark,
        futureContainerDark,
        onFutureContainerDark,
    ),
    futureVariant = ColorFamily(
        futureVariantDark,
        onFutureVariantDark,
        futureVariantContainerDark,
        onFutureVariantContainerDark,
    ),
    active = ColorFamily(
        activeDark,
        onActiveDark,
        activeContainerDark,
        onActiveContainerDark,
    ),
    discontinued = ColorFamily(
        discontinuedDark,
        onDiscontinuedDark,
        discontinuedContainerDark,
        onDiscontinuedContainerDark,
    ),
    discontinuedVariant = ColorFamily(
        discontinuedVariantDark,
        onDiscontinuedVariantDark,
        discontinuedVariantContainerDark,
        onDiscontinuedVariantContainerDark,
    ),
)

val extendedLightMediumContrast = ExtendedColorScheme(
    future = ColorFamily(
        futureLightMediumContrast,
        onFutureLightMediumContrast,
        futureContainerLightMediumContrast,
        onFutureContainerLightMediumContrast,
    ),
    futureVariant = ColorFamily(
        futureVariantLightMediumContrast,
        onFutureVariantLightMediumContrast,
        futureVariantContainerLightMediumContrast,
        onFutureVariantContainerLightMediumContrast,
    ),
    active = ColorFamily(
        activeLightMediumContrast,
        onActiveLightMediumContrast,
        activeContainerLightMediumContrast,
        onActiveContainerLightMediumContrast,
    ),
    discontinued = ColorFamily(
        discontinuedLightMediumContrast,
        onDiscontinuedLightMediumContrast,
        discontinuedContainerLightMediumContrast,
        onDiscontinuedContainerLightMediumContrast,
    ),
    discontinuedVariant = ColorFamily(
        discontinuedVariantLightMediumContrast,
        onDiscontinuedVariantLightMediumContrast,
        discontinuedVariantContainerLightMediumContrast,
        onDiscontinuedVariantContainerLightMediumContrast,
    ),
)

val extendedLightHighContrast = ExtendedColorScheme(
    future = ColorFamily(
        futureLightHighContrast,
        onFutureLightHighContrast,
        futureContainerLightHighContrast,
        onFutureContainerLightHighContrast,
    ),
    futureVariant = ColorFamily(
        futureVariantLightHighContrast,
        onFutureVariantLightHighContrast,
        futureVariantContainerLightHighContrast,
        onFutureVariantContainerLightHighContrast,
    ),
    active = ColorFamily(
        activeLightHighContrast,
        onActiveLightHighContrast,
        activeContainerLightHighContrast,
        onActiveContainerLightHighContrast,
    ),
    discontinued = ColorFamily(
        discontinuedLightHighContrast,
        onDiscontinuedLightHighContrast,
        discontinuedContainerLightHighContrast,
        onDiscontinuedContainerLightHighContrast,
    ),
    discontinuedVariant = ColorFamily(
        discontinuedVariantLightHighContrast,
        onDiscontinuedVariantLightHighContrast,
        discontinuedVariantContainerLightHighContrast,
        onDiscontinuedVariantContainerLightHighContrast,
    ),
)

val extendedDarkMediumContrast = ExtendedColorScheme(
    future = ColorFamily(
        futureDarkMediumContrast,
        onFutureDarkMediumContrast,
        futureContainerDarkMediumContrast,
        onFutureContainerDarkMediumContrast,
    ),
    futureVariant = ColorFamily(
        futureVariantDarkMediumContrast,
        onFutureVariantDarkMediumContrast,
        futureVariantContainerDarkMediumContrast,
        onFutureVariantContainerDarkMediumContrast,
    ),
    active = ColorFamily(
        activeDarkMediumContrast,
        onActiveDarkMediumContrast,
        activeContainerDarkMediumContrast,
        onActiveContainerDarkMediumContrast,
    ),
    discontinued = ColorFamily(
        discontinuedDarkMediumContrast,
        onDiscontinuedDarkMediumContrast,
        discontinuedContainerDarkMediumContrast,
        onDiscontinuedContainerDarkMediumContrast,
    ),
    discontinuedVariant = ColorFamily(
        discontinuedVariantDarkMediumContrast,
        onDiscontinuedVariantDarkMediumContrast,
        discontinuedVariantContainerDarkMediumContrast,
        onDiscontinuedVariantContainerDarkMediumContrast,
    ),
)

val extendedDarkHighContrast = ExtendedColorScheme(
    future = ColorFamily(
        futureDarkHighContrast,
        onFutureDarkHighContrast,
        futureContainerDarkHighContrast,
        onFutureContainerDarkHighContrast,
    ),
    futureVariant = ColorFamily(
        futureVariantDarkHighContrast,
        onFutureVariantDarkHighContrast,
        futureVariantContainerDarkHighContrast,
        onFutureVariantContainerDarkHighContrast,
    ),
    active = ColorFamily(
        activeDarkHighContrast,
        onActiveDarkHighContrast,
        activeContainerDarkHighContrast,
        onActiveContainerDarkHighContrast,
    ),
    discontinued = ColorFamily(
        discontinuedDarkHighContrast,
        onDiscontinuedDarkHighContrast,
        discontinuedContainerDarkHighContrast,
        onDiscontinuedContainerDarkHighContrast,
    ),
    discontinuedVariant = ColorFamily(
        discontinuedVariantDarkHighContrast,
        onDiscontinuedVariantDarkHighContrast,
        discontinuedVariantContainerDarkHighContrast,
        onDiscontinuedVariantContainerDarkHighContrast,
    ),
)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color,
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Composable
fun BricklogTheme(
    themeOption: ThemeOption = ThemeOption.SYSTEM,
    content: @Composable () -> Unit,
) {
    val extendedColorScheme = when (themeOption) {
        ThemeOption.SYSTEM -> if (isSystemInDarkTheme()) extendedDark else extendedLight
        ThemeOption.LIGHT -> extendedLight
        ThemeOption.DARK -> extendedDark
    }
    val colorScheme = when (themeOption) {
        ThemeOption.SYSTEM -> if (isSystemInDarkTheme()) darkScheme else lightScheme
        ThemeOption.LIGHT -> lightScheme
        ThemeOption.DARK -> darkScheme
    }

    CompositionLocalProvider(
        LocalExtendedColorScheme provides extendedColorScheme,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

object BricklogTheme {
    val colorScheme: ExtendedColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalExtendedColorScheme.current
}

internal val LocalExtendedColorScheme = staticCompositionLocalOf { extendedLight }
