package hu.piware.bricklog.feature.onboarding.presentation.data_fetch

sealed interface DataFetchState {
    data object Loading : DataFetchState
    data object Success : DataFetchState
    data object Error : DataFetchState
}
