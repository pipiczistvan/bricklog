package hu.piware.bricklog.feature.set.presentation.set_scanner

import hu.piware.barcode_scanner.BarcodeDetection

sealed interface SetScannerAction {
    data object OnBackClick : SetScannerAction
    data object OnManualClick : SetScannerAction
    data class OnBarcodeDetected(val detections: List<BarcodeDetection>) : SetScannerAction
    data class OnSetClick(val id: Int) : SetScannerAction
}
