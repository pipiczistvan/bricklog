package hu.piware.barcode_scanner

import androidx.compose.ui.geometry.Rect

data class BarcodeDetection(
    val data: String,
    val bounds: Rect,
)
