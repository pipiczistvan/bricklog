package hu.piware.bricklog.feature.user.presentation.user_scanner

import hu.piware.barcode_scanner.BarcodeDetection

sealed interface UserScannerAction {
    data object OnBackClick : UserScannerAction
    data class OnBarcodeDetected(val detections: List<BarcodeDetection>) : UserScannerAction
}
