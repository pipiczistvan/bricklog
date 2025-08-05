package hu.piware.bricklog.feature.onboarding.presentation.preload

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
class PreloadViewModel(
    private val updateDataWithProgress: UpdateDataWithProgress,
) : ViewModel() {

    private val logger = Logger.withTag("PreloadViewModel")

    private var _uiState = MutableStateFlow<PreloadState>(PreloadState.Initial)
    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            fetchData()
        }

    fun onAction(action: PreloadAction) {
        when (action) {
            is PreloadAction.OnRetryClick -> fetchData()
            else -> Unit
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            updateDataWithProgress()
                .collectForValue { progress ->
                    logger.d { "Progress: $progress" }
                    _uiState.update { PreloadState.Loading(progress) }
                }
                .showSnackbarOnError()
                .onError { _uiState.update { PreloadState.Error } }
                .onSuccess {
                    delay(500) // Slight delay to display 100% progress
                    _uiState.update { PreloadState.Success }
                }
        }
    }
}
