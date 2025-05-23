package hu.piware.bricklog.feature.onboarding.presentation.data_fetch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.core.domain.onSuccess
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.onboarding.domain.usecase.HasAnySets
import hu.piware.bricklog.feature.set.domain.usecase.UpdateSets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DataFetchViewModel(
    private val updateSets: UpdateSets,
    private val hasAnySets: HasAnySets,
) : ViewModel() {

    private var _uiState = MutableStateFlow<DataFetchState>(DataFetchState.Loading)
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
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { DataFetchState.Loading }

            updateSets(force = true)
                .showSnackbarOnError()

            hasAnySets()
                .showSnackbarOnError()
                .onSuccess { hasSets ->
                    if (hasSets) {
                        _uiState.update { DataFetchState.Success }
                    } else {
                        _uiState.update { DataFetchState.Error }
                    }
                }
                .onError {
                    _uiState.update { DataFetchState.Error }
                }
        }
    }
}
