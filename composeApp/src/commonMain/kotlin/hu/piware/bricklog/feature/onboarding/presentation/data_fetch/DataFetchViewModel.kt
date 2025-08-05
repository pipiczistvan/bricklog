package hu.piware.bricklog.feature.onboarding.presentation.data_fetch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.domain.collectForValue
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.core.domain.onSuccess
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.onboarding.domain.usecase.UpdateDataWithProgress
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DataFetchViewModel(
    private val updateDataWithProgress: UpdateDataWithProgress,
) : ViewModel() {

    private val logger = Logger.withTag("DataFetchViewModel")

    private var _uiState = MutableStateFlow<DataFetchState>(DataFetchState.Initial)
    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            fetchData()
        }

    fun onAction(action: DataFetchAction) {
        when (action) {
            is DataFetchAction.OnRetryClick -> fetchData()
            else -> Unit
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            updateDataWithProgress()
                .collectForValue { progress ->
                    logger.d { "Progress: $progress" }
                    _uiState.update { DataFetchState.Loading(progress) }
                }
                .showSnackbarOnError()
                .onError { _uiState.update { DataFetchState.Error } }
                .onSuccess {
                    delay(500) // Slight delay to display 100% progress
                    _uiState.update { DataFetchState.Success }
                }
        }
    }
}
