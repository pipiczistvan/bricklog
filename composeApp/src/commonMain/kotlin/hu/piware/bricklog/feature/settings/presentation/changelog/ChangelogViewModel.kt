package hu.piware.bricklog.feature.settings.presentation.changelog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.piware.bricklog.feature.core.domain.onSuccess
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.settings.domain.usecase.GetChangelog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChangelogViewModel(
    private val getChangelog: GetChangelog,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangelogState())

    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            loadLicense()
        }

    private fun loadLicense() {
        viewModelScope.launch {
            getChangelog()
                .showSnackbarOnError()
                .onSuccess { changelog ->
                    _uiState.update { it.copy(changelog = changelog) }
                }
        }
    }
}
