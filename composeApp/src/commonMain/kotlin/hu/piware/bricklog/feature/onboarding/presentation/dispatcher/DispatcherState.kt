package hu.piware.bricklog.feature.onboarding.presentation.dispatcher

sealed interface DispatcherState {
    data object Loading : DispatcherState
    data object NavigateToHome : DispatcherState
    data object NavigateToDataFetch : DispatcherState
}
