package hu.piware.bricklog.feature.set.presentation.set_scanner.util

import androidx.compose.ui.geometry.Rect
import hu.piware.bricklog.feature.set.domain.model.SetDetails

data class SetBarcodeDetection(
    val set: SetDetails? = null,
    val barcode: String,
    val bounds: Rect,
)
