package hu.piware.bricklog.feature.settings.presentation.appearance

import hu.piware.bricklog.feature.settings.domain.model.ThemeOption

sealed interface AppearanceAction {
    data object OnBackClick : AppearanceAction
    data class OnThemeOptionChange(val option: ThemeOption) : AppearanceAction
}
