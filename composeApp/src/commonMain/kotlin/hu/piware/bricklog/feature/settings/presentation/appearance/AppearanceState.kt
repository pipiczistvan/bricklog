package hu.piware.bricklog.feature.settings.presentation.appearance

import hu.piware.bricklog.feature.settings.domain.model.ThemeOption
import hu.piware.bricklog.feature.user.domain.manager.SessionManager.Companion.GUEST_PREFERENCES
import hu.piware.bricklog.feature.user.domain.manager.SessionManager.Companion.GUEST_USER
import hu.piware.bricklog.feature.user.domain.model.User
import hu.piware.bricklog.feature.user.domain.model.UserPreferences

data class AppearanceState(
    val themeOption: ThemeOption = ThemeOption.SYSTEM,
    val currentUser: User = GUEST_USER,
    val userPreferences: UserPreferences = GUEST_PREFERENCES,
    val isLoading: Boolean = false,
)
