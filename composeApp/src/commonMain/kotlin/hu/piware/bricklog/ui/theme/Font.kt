package hu.piware.bricklog.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import bricklog.composeapp.generated.resources.OverpassMono_Bold
import bricklog.composeapp.generated.resources.OverpassMono_Light
import bricklog.composeapp.generated.resources.OverpassMono_Medium
import bricklog.composeapp.generated.resources.OverpassMono_Regular
import bricklog.composeapp.generated.resources.OverpassMono_SemiBold
import bricklog.composeapp.generated.resources.Overpass_Bold
import bricklog.composeapp.generated.resources.Overpass_Light
import bricklog.composeapp.generated.resources.Overpass_Medium
import bricklog.composeapp.generated.resources.Overpass_Regular
import bricklog.composeapp.generated.resources.Overpass_SemiBold
import bricklog.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font

@Composable
fun OverpassFontFamily() = FontFamily(
    Font(Res.font.Overpass_Light, weight = FontWeight.Light),
    Font(Res.font.Overpass_Regular, weight = FontWeight.Normal),
    Font(Res.font.Overpass_Medium, weight = FontWeight.Medium),
    Font(Res.font.Overpass_SemiBold, weight = FontWeight.SemiBold),
    Font(Res.font.Overpass_Bold, weight = FontWeight.Bold),
)

@Composable
fun OverpassMonoFontFamily() = FontFamily(
    Font(Res.font.OverpassMono_Light, weight = FontWeight.Light),
    Font(Res.font.OverpassMono_Regular, weight = FontWeight.Normal),
    Font(Res.font.OverpassMono_Medium, weight = FontWeight.Medium),
    Font(Res.font.OverpassMono_SemiBold, weight = FontWeight.SemiBold),
    Font(Res.font.OverpassMono_Bold, weight = FontWeight.Bold),
)

@Composable
fun OverpassTypography() = typography(OverpassFontFamily())

@Composable
fun OverpassMonoTypography() = typography(OverpassMonoFontFamily())

private fun typography(fontFamily: FontFamily) = Typography().run {
    copy(
        displayLarge = displayLarge.copy(fontFamily = fontFamily),
        displayMedium = displayMedium.copy(fontFamily = fontFamily),
        displaySmall = displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = titleLarge.copy(fontFamily = fontFamily),
        titleMedium = titleMedium.copy(fontFamily = fontFamily),
        titleSmall = titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = bodySmall.copy(fontFamily = fontFamily),
        labelLarge = labelLarge.copy(fontFamily = fontFamily),
        labelMedium = labelMedium.copy(fontFamily = fontFamily),
        labelSmall = labelSmall.copy(fontFamily = fontFamily),
    )
}
