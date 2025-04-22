package hu.piware.bricklog.feature.onboarding.presentation.data_fetch

sealed interface DataFetchAction {
    data object OnRetryClick : DataFetchAction
    data object OnContinueClick : DataFetchAction
}
