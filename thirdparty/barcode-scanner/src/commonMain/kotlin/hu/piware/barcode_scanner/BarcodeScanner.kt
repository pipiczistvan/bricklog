@file:OptIn(ExperimentalTime::class)

package hu.piware.barcode_scanner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import co.touchlab.kermit.Logger
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.camera.CAMERA
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

@Composable
expect fun BarcodeScanner(
    onScanResult: (List<BarcodeDetection>) -> Unit,
    formats: List<BarcodeFormat>,
    modifier: Modifier = Modifier,
)

private val logger = Logger.withTag("BarcodeScanner")

@Composable
fun BarcodeScannerWithPermission(
    onScanResult: (List<BarcodeDetection>) -> Unit,
    formats: List<BarcodeFormat>,
    permissionsController: PermissionsController,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()

    var cameraPermissionGranted by remember { mutableStateOf(false) }

    scope.launch {
        try {
            permissionsController.providePermission(Permission.CAMERA)
            cameraPermissionGranted = true
        } catch (e: DeniedAlwaysException) {
            logger.w { "Camera permission denied always." }
        } catch (e: DeniedException) {
            logger.w { "Camera permission denied." }
        } catch (e: RequestCanceledException) {
            logger.w { "Camera permission canceled." }
        }
    }

    if (cameraPermissionGranted) {
        BarcodeScanner(
            onScanResult = onScanResult,
            formats = formats,
            modifier = modifier
        )
    }
}

