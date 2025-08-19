package hu.piware.bricklog.feature.core.domain

sealed interface AppEvent {
    data object Initialize : AppEvent
    data object UserChanged : AppEvent
}
