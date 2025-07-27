package hu.piware.bricklog.feature.onboarding.presentation.data_fetch

import hu.piware.bricklog.feature.set.domain.model.UpdateSetsProgress

sealed interface DataFetchState {
    data object Initial : DataFetchState
    data class Loading(val progress: UpdateSetsProgress) : DataFetchState
    data object Success : DataFetchState
    data object Error : DataFetchState
}
