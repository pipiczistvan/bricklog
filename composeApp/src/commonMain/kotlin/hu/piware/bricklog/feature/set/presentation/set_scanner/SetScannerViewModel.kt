@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.set.presentation.set_scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.usecase.WatchSetDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SetScannerViewModel(
    private val watchSetDetails: WatchSetDetails,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SetScannerState())
    private val _scannedCode = MutableStateFlow("")

    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            observeScannedCode()
        }

    fun onAction(action: SetScannerAction) {
        when (action) {
            is SetScannerAction.OnCodeScanned -> {
                _scannedCode.update { action.code }
            }

            else -> Unit
        }
    }

    private fun observeScannedCode() {
        _scannedCode
            .filter { it.isNotEmpty() }
            .distinctUntilChanged()
            .map { SetFilter(limit = 1, barcode = it) }
            .flatMapLatest { watchSetDetails(it) }
            .onEach { sets ->
                _uiState.update { it.copy(setDetails = sets.firstOrNull()) }
            }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }
}
