package hu.piware.bricklog.feature.settings.domain.model

import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.language_english_label
import bricklog.composeapp.generated.resources.language_hungarian_label
import bricklog.composeapp.generated.resources.language_system_label
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
        titleRes = Res.string.language_system_label,
        emoji = "⚙\uFE0F",
        intValue = 0,
        isoFormat = ""
    ),
    ENGLISH(
        titleRes = Res.string.language_english_label,
        emoji = "\uD83C\uDDEC\uD83C\uDDE7",
        intValue = 1,
        isoFormat = "en"
    ),
    HUNGARIAN(
        titleRes = Res.string.language_hungarian_label,
        emoji = "\uD83C\uDDED\uD83C\uDDFA",
        intValue = 2,
        isoFormat = "hu"
    )
}
