package hu.piware.bricklog.feature.core.domain

sealed interface AppEvent {
    data object StartSync : AppEvent
}