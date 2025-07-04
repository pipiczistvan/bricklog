package hu.piware.bricklog.feature.set.presentation.set_scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import hu.piware.barcode_scanner.BarcodeDetection
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.usecase.FindCollectibleSet
import hu.piware.bricklog.feature.set.domain.usecase.WatchSetDetails
import hu.piware.bricklog.feature.set.presentation.set_scanner.util.SetBarcodeDetection
import hu.piware.bricklog.feature.set.presentation.set_scanner.util.parseCmfBoxDataMatrix
import hu.piware.bricklog.util.getOrPutNullable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SetScannerViewModel(
    private val watchSetDetails: WatchSetDetails,
    private val findCollectibleSet: FindCollectibleSet,
) : ViewModel() {

    private val logger = Logger.withTag("SetScannerViewModel")

    private val _uiState = MutableStateFlow(SetScannerState())
    private val _detections = MutableStateFlow(emptyList<BarcodeDetection>())
    private val _setBarcodeCache = mutableMapOf<String, SetDetails?>()

    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            observeScannedCode()
        }

    fun onAction(action: SetScannerAction) {
        when (action) {
            is SetScannerAction.OnBarcodeDetected -> {
                _detections.update { action.detections }
            }

            else -> Unit
        }
    }

    private fun observeScannedCode() {
        _detections
            .onEach { detections ->
                _uiState.update {
                    it.copy(
                        detections = detections.map { detection ->
                            val set = _setBarcodeCache.getOrPutNullable(detection.data) {
                                logger.i { "barcode not found in cache: ${detection.data}" }
                                findSetByScannedCode(detection.data)
                            }.also { set ->
                                if (set == null) {
                                    logger.i { "Set not found for barcode: ${detection.data}" }
                                } else {
                                    logger.i { "Set found for barcode: ${detection.data}" }
                                }
                            }

                            SetBarcodeDetection(
                                set = set,
                                barcode = detection.data,
                                bounds = detection.bounds
                            )
                        }
                    )
                }
            }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    private suspend fun findSetByScannedCode(barcode: String): SetDetails? {
        logger.i { "Searching set by scanned code: $barcode" }

        val cmfBoxDataMatrix = barcode.parseCmfBoxDataMatrix()

        return if (cmfBoxDataMatrix != null) {
            findCollectibleSet(cmfBoxDataMatrix.productNumber)
                .showSnackbarOnError()
                .onError { return null }
                .data()
        } else {
            watchSetDetails(SetFilter(limit = 1, barcode = barcode))
                .firstOrNull()
                ?.firstOrNull()
        }
    }
}
