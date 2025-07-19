package hu.piware.bricklog.feature.settings.presentation.appearance

import hu.piware.bricklog.feature.settings.domain.model.ThemeOption
import hu.piware.bricklog.feature.settings.domain.model.UserPreferences
import hu.piware.bricklog.feature.user.domain.model.User

data class AppearanceState(
    val themeOption: ThemeOption = ThemeOption.SYSTEM,
    val currentUser: User? = null,
    val userPreferences: UserPreferences = UserPreferences(),
    val isLoading: Boolean = false,
)
