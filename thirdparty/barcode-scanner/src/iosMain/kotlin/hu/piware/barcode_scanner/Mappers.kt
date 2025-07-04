@file:OptIn(ExperimentalForeignApi::class)

package hu.piware.barcode_scanner

import androidx.compose.ui.geometry.Rect
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.AVFoundation.AVMetadataMachineReadableCodeObject
import platform.AVFoundation.AVMetadataObjectTypeAztecCode
import platform.AVFoundation.AVMetadataObjectTypeCodabarCode
import platform.AVFoundation.AVMetadataObjectTypeCode128Code
import platform.AVFoundation.AVMetadataObjectTypeCode39Code
import platform.AVFoundation.AVMetadataObjectTypeCode93Code
import platform.AVFoundation.AVMetadataObjectTypeDataMatrixCode
import platform.AVFoundation.AVMetadataObjectTypeEAN13Code
import platform.AVFoundation.AVMetadataObjectTypeEAN8Code
import platform.AVFoundation.AVMetadataObjectTypeITF14Code
import platform.AVFoundation.AVMetadataObjectTypePDF417Code
import platform.AVFoundation.AVMetadataObjectTypeQRCode
import platform.AVFoundation.AVMetadataObjectTypeUPCECode

fun List<BarcodeFormat>.toAVMetadataObjectTypeFormat() = map {
    when (it) {
        BarcodeFormat.Codabar -> if (iosVersionIsMin(15, 4)) {
            AVMetadataObjectTypeCodabarCode
        } else error("AVMetadataObjectTypeCodabarCode not available on iOS ${iosVersion()}")

        BarcodeFormat.Code39 -> AVMetadataObjectTypeCode39Code
        BarcodeFormat.Code93 -> AVMetadataObjectTypeCode93Code
        BarcodeFormat.Code128 -> AVMetadataObjectTypeCode128Code
        BarcodeFormat.EAN8 -> AVMetadataObjectTypeEAN8Code
        BarcodeFormat.EAN13 -> AVMetadataObjectTypeEAN13Code
        BarcodeFormat.ITF -> AVMetadataObjectTypeITF14Code
        BarcodeFormat.UPCE -> AVMetadataObjectTypeUPCECode
        BarcodeFormat.Aztec -> AVMetadataObjectTypeAztecCode
        BarcodeFormat.DataMatrix -> AVMetadataObjectTypeDataMatrixCode
        BarcodeFormat.PDF417 -> AVMetadataObjectTypePDF417Code
        BarcodeFormat.QR -> AVMetadataObjectTypeQRCode
    }
}

fun AVMetadataMachineReadableCodeObject.toBarcodeDetection(): BarcodeDetection? {
    val bounds = bounds.useContents {
        Rect(
            left = origin.x.toFloat(),
            top = origin.y.toFloat(),
            right = origin.x.toFloat() + size.width.toFloat(),
            bottom = origin.y.toFloat() + size.height.toFloat()
        )
    }

    val data = stringValue ?: return null

    return BarcodeDetection(
        data = data,
        bounds = bounds
    )
}
