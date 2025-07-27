package hu.piware.bricklog.feature.onboarding.presentation.data_fetch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.piware.bricklog.feature.core.domain.collectForValue
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.core.domain.onSuccess
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.set.domain.usecase.UpdateSetsWithProgress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DataFetchViewModel(
    private val updateSetsWithProgress: UpdateSetsWithProgress,
) : ViewModel() {

    private var _uiState = MutableStateFlow<DataFetchState>(DataFetchState.Initial)
    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            fetchSets()
        }

    fun onAction(action: DataFetchAction) {
        when (action) {
            is DataFetchAction.OnRetryClick -> fetchSets()
            else -> Unit
        }
    }

    private fun fetchSets() {
        viewModelScope.launch {
            updateSetsWithProgress(force = true)
                .collectForValue { progress -> _uiState.update { DataFetchState.Loading(progress) } }
                .showSnackbarOnError()
                .onError { _uiState.update { DataFetchState.Error } }
                .onSuccess { _uiState.update { DataFetchState.Success } }
        }
    }
}
