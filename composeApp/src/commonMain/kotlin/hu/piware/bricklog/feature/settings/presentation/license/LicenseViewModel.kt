package hu.piware.bricklog.feature.settings.presentation.license

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.settings.domain.usecase.ReadTextFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LicenseViewModel(
    private val readTextFile: ReadTextFile,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LicenseState())

    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            loadLicense()
        }

    private fun loadLicense() {
        viewModelScope.launch {
            val license = readTextFile("files/LICENSE")
                .showSnackbarOnError()
                .data()

            _uiState.update { it.copy(license = license) }
        }
    }
}
