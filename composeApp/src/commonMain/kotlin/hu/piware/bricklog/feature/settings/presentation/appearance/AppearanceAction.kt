package hu.piware.bricklog.feature.settings.presentation.appearance

import hu.piware.bricklog.feature.settings.domain.model.ThemeOption
import hu.piware.bricklog.feature.user.domain.model.UserPreferences

sealed interface AppearanceAction {
    data object OnBackClick : AppearanceAction
    data class OnThemeOptionChange(val option: ThemeOption) : AppearanceAction
    data class OnUserPreferencesChange(val preferences: UserPreferences, val showLoading: Boolean) :
        AppearanceAction
}
