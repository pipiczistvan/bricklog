package hu.piware.bricklog.feature.onboarding.presentation.dispatcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.piware.bricklog.feature.core.domain.AppEvent
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.core.domain.onSuccess
import hu.piware.bricklog.feature.core.presentation.AppEventController
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.currency.domain.usecase.HasEurRates
import hu.piware.bricklog.feature.set.domain.usecase.HasAnySets
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DispatcherViewModel(
    private val hasAnySets: HasAnySets,
    private val hasEurRates: HasEurRates,
) : ViewModel() {

    private var _uiState = MutableStateFlow<DispatcherState>(DispatcherState.Loading)
    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            dispatch()
        }

    private fun dispatch() {
        viewModelScope.launch {
            checkData()
                .showSnackbarOnError()
                .onSuccess { hasData ->
                    if (hasData) {
                        AppEventController.sendEvent(AppEvent.Initialize)
                        _uiState.update { DispatcherState.NavigateToHome }
                    } else {
                        _uiState.update { DispatcherState.NavigateToPreload }
                    }
                }
        }
    }

    private suspend fun checkData(): Result<Boolean, DataError> {
        val hasSets = hasAnySets()
            .onError { return it }
            .data()

        val hasEurRates = hasEurRates()
            .onError { return it }
            .data()

        return Result.Success(hasSets && hasEurRates)
    }
}
