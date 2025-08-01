package hu.piware.bricklog.feature.settings.domain.model

import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_settings_appearance_language_label_english
import bricklog.composeapp.generated.resources.feature_settings_appearance_language_label_hungarian
import bricklog.composeapp.generated.resources.feature_settings_appearance_language_label_system
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

@Serializable
enum class LanguageOption(
    val titleRes: StringResource,
    val emoji: String,
    val intValue: Int,
    val isoFormat: String,
) {
    SYSTEM(
        titleRes = Res.string.feature_settings_appearance_language_label_system,
        emoji = "⚙\uFE0F",
        intValue = 0,
        isoFormat = "",
    ),
    ENGLISH(
        titleRes = Res.string.feature_settings_appearance_language_label_english,
        emoji = "\uD83C\uDDEC\uD83C\uDDE7",
        intValue = 1,
        isoFormat = "en",
    ),
    HUNGARIAN(
        titleRes = Res.string.feature_settings_appearance_language_label_hungarian,
        emoji = "\uD83C\uDDED\uD83C\uDDFA",
        intValue = 2,
        isoFormat = "hu",
    ),
}
