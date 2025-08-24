@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package hu.piware.bricklog.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
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
    val purple: ColorFamily,
    val green: ColorFamily,
    val red: ColorFamily,
    val blue: ColorFamily,
    val yellow: ColorFamily,
    val orange: ColorFamily,
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

val extendedLight = ExtendedColorScheme(
    purple = ColorFamily(
        purpleLight,
        onPurpleLight,
        purpleContainerLight,
        onPurpleContainerLight,
    ),
    green = ColorFamily(
        greenLight,
        onGreenLight,
        greenContainerLight,
        onGreenContainerLight,
    ),
    red = ColorFamily(
        redLight,
        onRedLight,
        redContainerLight,
        onRedContainerLight,
    ),
    blue = ColorFamily(
        blueLight,
        onBlueLight,
        blueContainerLight,
        onBlueContainerLight,
    ),
    yellow = ColorFamily(
        yellowLight,
        onYellowLight,
        yellowContainerLight,
        onYellowContainerLight,
    ),
    orange = ColorFamily(
        orangeLight,
        onOrangeLight,
        orangeContainerLight,
        onOrangeContainerLight,
    ),
)

val extendedDark = ExtendedColorScheme(
    purple = ColorFamily(
        purpleDark,
        onPurpleDark,
        purpleContainerDark,
        onPurpleContainerDark,
    ),
    green = ColorFamily(
        greenDark,
        onGreenDark,
        greenContainerDark,
        onGreenContainerDark,
    ),
    red = ColorFamily(
        redDark,
        onRedDark,
        redContainerDark,
        onRedContainerDark,
    ),
    blue = ColorFamily(
        blueDark,
        onBlueDark,
        blueContainerDark,
        onBlueContainerDark,
    ),
    yellow = ColorFamily(
        yellowDark,
        onYellowDark,
        yellowContainerDark,
        onYellowContainerDark,
    ),
    orange = ColorFamily(
        orangeDark,
        onOrangeDark,
        orangeContainerDark,
        onOrangeContainerDark,
    ),
)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color,
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
            motionScheme = MotionScheme.expressive(),
            typography = OverpassTypography(),
            content = content,
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
