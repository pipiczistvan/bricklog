package hu.piware.bricklog.feature.onboarding.presentation.dispatcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.onboarding.domain.usecase.HasAnySets
import hu.piware.bricklog.feature.onboarding.domain.usecase.InitializeChangelogReadVersion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DispatcherViewModel(
    private val hasAnySets: HasAnySets,
    private val initializeChangelogReadVersion: InitializeChangelogReadVersion,
) : ViewModel() {

    private var _uiState = MutableStateFlow<DispatcherState>(DispatcherState.Loading)
    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            initializeChangelogStatus()
            checkSets()
        }

    private fun initializeChangelogStatus() {
        viewModelScope.launch {
            initializeChangelogReadVersion()
        }
    }

    private fun checkSets() {
        viewModelScope.launch {
            val hasSetsResult = hasAnySets()
            if (hasSetsResult is Result.Success && hasSetsResult.data) {
                _uiState.update { DispatcherState.NavigateToHome }
            } else {
                _uiState.update { DispatcherState.NavigateToDataFetch }
            }
        }
    }
}
