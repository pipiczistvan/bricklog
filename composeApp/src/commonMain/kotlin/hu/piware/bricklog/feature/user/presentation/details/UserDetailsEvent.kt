package hu.piware.bricklog.feature.user.presentation.details

sealed interface UserDetailsEvent {
    data object Back : UserDetailsEvent
    data object UserDeleted : UserDetailsEvent
    data object LoginProposed : UserDetailsEvent
}
