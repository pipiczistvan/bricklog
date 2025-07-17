package hu.piware.bricklog.feature.set.presentation.set_scanner_manual

sealed interface SetScannerManualAction {
    data object OnBackClick : SetScannerManualAction
}
