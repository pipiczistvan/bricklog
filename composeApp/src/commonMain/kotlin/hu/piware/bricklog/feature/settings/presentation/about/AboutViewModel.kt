package hu.piware.bricklog.feature.settings.presentation.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.settings.domain.usecase.GetEurRateDataSyncInfo
import hu.piware.bricklog.feature.settings.domain.usecase.GetSetDataSyncInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class AboutViewModel(
    private val getEurRateDataSyncInfo: GetEurRateDataSyncInfo,
    private val getSetDataSyncInfo: GetSetDataSyncInfo,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AboutState())

    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            loadEurRateDataSyncInfo()
            loadSetDataSyncInfo()
        }

    private fun loadEurRateDataSyncInfo() {
        viewModelScope.launch {
            val dataSyncInfo = getEurRateDataSyncInfo()
                .showSnackbarOnError()
                .data()

            _uiState.update { it.copy(eurRateDataSyncInfo = dataSyncInfo) }
        }
    }

    private fun loadSetDataSyncInfo() {
        viewModelScope.launch {
            val dataSyncInfo = getSetDataSyncInfo()
                .showSnackbarOnError()
                .data()

            _uiState.update { it.copy(setDataSyncInfo = dataSyncInfo) }
        }
    }
}
