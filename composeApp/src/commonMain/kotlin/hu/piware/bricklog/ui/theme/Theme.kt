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
    val future: ColorFamily,
    val futureVariant: ColorFamily,
    val active: ColorFamily,
    val retired: ColorFamily,
    val retiredVariant: ColorFamily,
    val budget: ColorFamily,
    val affordable: ColorFamily,
    val expensive: ColorFamily,
    val premium: ColorFamily,
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
    retired = ColorFamily(
        retiredLight,
        onRetiredLight,
        retiredContainerLight,
        onRetiredContainerLight,
    ),
    retiredVariant = ColorFamily(
        retiredVariantLight,
        onRetiredVariantLight,
        retiredVariantContainerLight,
        onRetiredVariantContainerLight,
    ),
    budget = ColorFamily(
        budgetLight,
        onBudgetLight,
        budgetContainerLight,
        onBudgetContainerLight,
    ),
    affordable = ColorFamily(
        affordableLight,
        onAffordableLight,
        affordableContainerLight,
        onAffordableContainerLight,
    ),
    expensive = ColorFamily(
        expensiveLight,
        onExpensiveLight,
        expensiveContainerLight,
        onExpensiveContainerLight,
    ),
    premium = ColorFamily(
        premiumLight,
        onPremiumLight,
        premiumContainerLight,
        onPremiumContainerLight,
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
    retired = ColorFamily(
        retiredDark,
        onRetiredDark,
        retiredContainerDark,
        onRetiredContainerDark,
    ),
    retiredVariant = ColorFamily(
        retiredVariantDark,
        onRetiredVariantDark,
        retiredVariantContainerDark,
        onRetiredVariantContainerDark,
    ),
    budget = ColorFamily(
        budgetDark,
        onBudgetDark,
        budgetContainerDark,
        onBudgetContainerDark,
    ),
    affordable = ColorFamily(
        affordableDark,
        onAffordableDark,
        affordableContainerDark,
        onAffordableContainerDark,
    ),
    expensive = ColorFamily(
        expensiveDark,
        onExpensiveDark,
        expensiveContainerDark,
        onExpensiveContainerDark,
    ),
    premium = ColorFamily(
        premiumDark,
        onPremiumDark,
        premiumContainerDark,
        onPremiumContainerDark,
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
