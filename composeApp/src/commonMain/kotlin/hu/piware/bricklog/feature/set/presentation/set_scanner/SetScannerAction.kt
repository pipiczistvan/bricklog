package hu.piware.bricklog.feature.set.presentation.set_scanner

sealed interface SetScannerAction {
    data object OnBackClick : SetScannerAction
    data class OnCodeScanned(val code: String) : SetScannerAction
    data class OnSetClick(val id: Int) : SetScannerAction
}
