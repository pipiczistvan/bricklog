package hu.piware.bricklog.feature.set.presentation.set_scanner_manual

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.set.domain.usecase.WatchCollectibleSetDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SetScannerManualViewModel(
    private val watchCollectibleSetDetails: WatchCollectibleSetDetails,
) : ViewModel() {

    private var _uiState = MutableStateFlow(SetScannerManualState())
    val uiState = _uiState.asStateFlowIn(viewModelScope) {
        observeCollectibleSetDetails()
    }

    private fun observeCollectibleSetDetails() {
        watchCollectibleSetDetails()
            .onEach { sets -> _uiState.update { it.copy(collectibleSetDetails = sets) } }
            .launchIn(viewModelScope)
    }
}
