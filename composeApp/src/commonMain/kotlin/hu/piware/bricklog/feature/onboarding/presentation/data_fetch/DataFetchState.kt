package hu.piware.bricklog.feature.onboarding.presentation.data_fetch

import hu.piware.bricklog.feature.core.presentation.UiTextUpdateProgress

sealed interface DataFetchState {
    data object Initial : DataFetchState
    data class Loading(val progress: UiTextUpdateProgress) : DataFetchState
    data object Success : DataFetchState
    data object Error : DataFetchState
}
