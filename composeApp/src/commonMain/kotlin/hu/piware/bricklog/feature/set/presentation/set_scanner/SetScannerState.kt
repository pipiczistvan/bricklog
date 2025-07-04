package hu.piware.bricklog.feature.set.presentation.set_scanner

import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.presentation.set_scanner.util.SetBarcodeDetection

data class SetScannerState(
    val setDetails: SetDetails? = null,
    val detections: List<SetBarcodeDetection> = emptyList(),
)
