package hu.piware.bricklog.feature.settings.domain.model

import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_settings_appearance_label_theme_dark
import bricklog.composeapp.generated.resources.feature_settings_appearance_label_theme_light
import bricklog.composeapp.generated.resources.feature_settings_appearance_label_theme_system
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

@Serializable
enum class ThemeOption(
    val titleRes: StringResource,
) {
    SYSTEM(
        titleRes = Res.string.feature_settings_appearance_label_theme_system,
    ),
    LIGHT(
        titleRes = Res.string.feature_settings_appearance_label_theme_light,
    ),
    DARK(
        titleRes = Res.string.feature_settings_appearance_label_theme_dark,
    ),
}
