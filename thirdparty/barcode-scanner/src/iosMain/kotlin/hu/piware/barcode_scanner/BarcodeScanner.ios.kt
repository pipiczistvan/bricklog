@file:OptIn(ExperimentalForeignApi::class)

package hu.piware.barcode_scanner

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import co.touchlab.kermit.Logger
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cValue
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRect
import platform.CoreGraphics.CGRectZero
import platform.QuartzCore.CATransaction
import platform.QuartzCore.kCATransactionDisableActions
import platform.UIKit.UIView

private val logger = Logger.withTag("BarcodeScanner")

@Composable
actual fun BarcodeScanner(
    onScanResult: (List<BarcodeDetection>) -> Unit,
    formats: List<BarcodeFormat>,
    modifier: Modifier,
) {
    var previewSize by remember { mutableStateOf(IntSize.Zero) }
    var scanSize by remember { mutableStateOf(IntSize.Zero) }

    val previewScaleX by derivedStateOf {
        if (previewSize.width > 0 && scanSize.width > 0) {
            previewSize.width / scanSize.width.toFloat()
        } else {
            1f
        }
    }

    val previewScaleY by derivedStateOf {
        if (previewSize.height > 0 && scanSize.height > 0) {
            previewSize.height / scanSize.height.toFloat()
        } else {
            1f
        }
    }

    val coordinator = remember {
        BarcodeScannerCoordinator(
            onScanned = { results ->
                onScanResult(
                    results.map { result ->
                        BarcodeDetection(
                            data = result.data,
                            bounds = Rect(
                                left = result.bounds.left * previewScaleX,
                                top = result.bounds.top * previewScaleY,
                                right = result.bounds.right * previewScaleX,
                                bottom = result.bounds.bottom * previewScaleY
                            )
                        )
                    }
                )
            }
        )
    }

    DisposableEffect(Unit) {
        val listener = OrientationListener { orientation ->
            coordinator.setCurrentOrientation(orientation)
        }

        listener.register()

        onDispose {
            listener.unregister()
        }
    }

    Box(
        modifier = modifier
    ) {
        UIKitView<UIView>(
            modifier = Modifier
                .onSizeChanged { size ->
                    previewSize = size
                }
                .fillMaxSize(),
            factory = {
                val previewContainer = ScannerPreviewView(
                    coordinator = coordinator,
                    onFrameChange = {
                        scanSize = IntSize(it.size.width.toInt(), it.size.height.toInt())
                    }
                )
                coordinator.prepare(previewContainer.layer, formats.toAVMetadataObjectTypeFormat())
                previewContainer
            },
            properties = UIKitInteropProperties(
                isInteractive = true,
                isNativeAccessibilityEnabled = true,
            )
        )
    }
}

class ScannerPreviewView(
    private val coordinator: BarcodeScannerCoordinator,
    private val onFrameChange: (CGRect) -> Unit,
) : UIView(
    frame = cValue { CGRectZero }
) {
    @OptIn(ExperimentalForeignApi::class)
    override fun layoutSubviews() {
        super.layoutSubviews()
        CATransaction.begin()
        CATransaction.setValue(true, kCATransactionDisableActions)

        layer.setFrame(frame)
        coordinator.setFrame(frame)
        frame.useContents {
            onFrameChange(this)
            logger.d { "setframe: ${this.size.width}x${this.size.height}" }
        }
        CATransaction.commit()
    }
}
