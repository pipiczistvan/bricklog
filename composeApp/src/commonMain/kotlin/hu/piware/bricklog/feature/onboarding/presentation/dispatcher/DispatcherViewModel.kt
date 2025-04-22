package hu.piware.bricklog.feature.onboarding.presentation.dispatcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.onboarding.domain.usecase.HasAnySets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DispatcherViewModel(
    private val hasAnySets: HasAnySets,
) : ViewModel() {

    private var _uiState = MutableStateFlow<DispatcherState>(DispatcherState.Loading)
    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            checkSets()
        }

    private fun checkSets() {
        viewModelScope.launch(Dispatchers.IO) {
            val hasSetsResult = hasAnySets()
            if (hasSetsResult is Result.Success && hasSetsResult.data) {
                _uiState.update { DispatcherState.NavigateToHome }
            } else {
                _uiState.update { DispatcherState.NavigateToDataFetch }
            }
        }
    }
}
