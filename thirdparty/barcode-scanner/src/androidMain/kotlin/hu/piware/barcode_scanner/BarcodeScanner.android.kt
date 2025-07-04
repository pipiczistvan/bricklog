package hu.piware.barcode_scanner

import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning

@Composable
actual fun BarcodeScanner(
    onScanResult: (List<BarcodeDetection>) -> Unit,
    formats: List<BarcodeFormat>,
    modifier: Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    val cameraController = remember {
        LifecycleCameraController(context)
    }

    Box(
        modifier = modifier
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                PreviewView(ctx).apply {
                    val options = BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(formats.toMlKitBarcodeFormat())
                        .build()

                    val barcodeScanner = BarcodeScanning.getClient(options)

                    cameraController.apply {
                        setImageAnalysisAnalyzer(
                            ContextCompat.getMainExecutor(ctx),
                            MlKitAnalyzer(
                                listOf(barcodeScanner),
                                ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED,
                                ContextCompat.getMainExecutor(ctx),
                                CachingMlKitBarcodeConsumer(barcodeScanner, onScanResult)
                            )
                        )
                        bindToLifecycle(lifecycleOwner)
                    }

                    this.controller = cameraController
                }
            }
        )
    }
}
