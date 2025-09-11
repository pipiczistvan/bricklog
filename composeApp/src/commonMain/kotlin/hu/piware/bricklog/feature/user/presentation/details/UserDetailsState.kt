package hu.piware.bricklog.feature.user.presentation.details

import hu.piware.bricklog.feature.user.domain.manager.SessionManager.Companion.GUEST_PREFERENCES
import hu.piware.bricklog.feature.user.domain.manager.SessionManager.Companion.GUEST_USER
import hu.piware.bricklog.feature.user.domain.model.User
import hu.piware.bricklog.feature.user.domain.model.UserPreferences

data class UserDetailsState(
    val isLoading: Boolean = false,
    val currentUser: User = GUEST_USER,
    val userPreferences: UserPreferences = GUEST_PREFERENCES,
)
