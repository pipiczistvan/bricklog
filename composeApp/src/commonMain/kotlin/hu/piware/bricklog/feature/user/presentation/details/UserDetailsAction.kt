package hu.piware.bricklog.feature.user.presentation.details

sealed interface UserDetailsAction {
    data object OnBackClick : UserDetailsAction
}
