package hu.piware.bricklog.feature.user.presentation.details

import hu.piware.bricklog.feature.user.domain.model.UserPreferences

sealed interface UserDetailsAction {
    data object OnBackClick : UserDetailsAction
    data class OnUserPreferencesChange(val preferences: UserPreferences) : UserDetailsAction
    data object OnUserDelete : UserDetailsAction
}
