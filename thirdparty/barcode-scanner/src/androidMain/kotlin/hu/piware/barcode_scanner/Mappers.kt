package hu.piware.barcode_scanner

import androidx.compose.ui.graphics.toComposeRect
import com.google.mlkit.vision.barcode.common.Barcode

fun List<BarcodeFormat>.toMlKitBarcodeFormat() = map {
    when (it) {
        BarcodeFormat.Codabar -> Barcode.FORMAT_CODABAR
        BarcodeFormat.Code39 -> Barcode.FORMAT_CODE_39
        BarcodeFormat.Code93 -> Barcode.FORMAT_CODE_93
        BarcodeFormat.Code128 -> Barcode.FORMAT_CODE_128
        BarcodeFormat.EAN8 -> Barcode.FORMAT_EAN_8
        BarcodeFormat.EAN13 -> Barcode.FORMAT_EAN_13
        BarcodeFormat.ITF -> Barcode.FORMAT_ITF
        BarcodeFormat.UPCE -> Barcode.FORMAT_UPC_E
        BarcodeFormat.Aztec -> Barcode.FORMAT_AZTEC
        BarcodeFormat.DataMatrix -> Barcode.FORMAT_DATA_MATRIX
        BarcodeFormat.PDF417 -> Barcode.FORMAT_PDF417
        BarcodeFormat.QR -> Barcode.FORMAT_QR_CODE
    }
}.fold(0) { acc, next ->
    acc + next
}

fun Barcode.toBarcodeDetection(): BarcodeDetection? {
    val rawValue = rawValue
    val boundingBox = boundingBox

    return if (rawValue != null && boundingBox != null) {
        BarcodeDetection(
            data = rawValue,
            bounds = boundingBox.toComposeRect()
        )
    } else null
}
