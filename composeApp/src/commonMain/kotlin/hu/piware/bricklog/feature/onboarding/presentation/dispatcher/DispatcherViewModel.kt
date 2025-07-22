package hu.piware.bricklog.feature.onboarding.presentation.dispatcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.onboarding.domain.usecase.HasAnySets
import hu.piware.bricklog.feature.onboarding.domain.usecase.InitializeChangelogReadVersion
import hu.piware.bricklog.feature.user.domain.usecase.InitializeSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DispatcherViewModel(
    private val hasAnySets: HasAnySets,
    private val initializeSession: InitializeSession,
    private val initializeChangelogReadVersion: InitializeChangelogReadVersion,
) : ViewModel() {

    private var _uiState = MutableStateFlow<DispatcherState>(DispatcherState.Loading)
    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            initializeData()
            checkSets()
        }

    private fun initializeData() {
        viewModelScope.launch {
            initializeSession()
                .showSnackbarOnError()
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
