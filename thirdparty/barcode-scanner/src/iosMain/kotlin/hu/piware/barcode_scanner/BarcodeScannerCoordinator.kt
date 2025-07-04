@file:OptIn(DelicateCoroutinesApi::class, ExperimentalForeignApi::class)

package hu.piware.barcode_scanner

import co.touchlab.kermit.Logger
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.useContents
import kotlinx.cinterop.value
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCaptureDevicePositionBack
import platform.AVFoundation.AVCaptureMetadataOutput
import platform.AVFoundation.AVCaptureMetadataOutputObjectsDelegateProtocol
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureVideoOrientationLandscapeLeft
import platform.AVFoundation.AVCaptureVideoOrientationLandscapeRight
import platform.AVFoundation.AVCaptureVideoOrientationPortrait
import platform.AVFoundation.AVCaptureVideoOrientationPortraitUpsideDown
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.AVMetadataMachineReadableCodeObject
import platform.AVFoundation.AVMetadataObjectType
import platform.AVFoundation.position
import platform.CoreGraphics.CGRect
import platform.Foundation.NSError
import platform.QuartzCore.CALayer
import platform.UIKit.UIDevice
import platform.UIKit.UIDeviceOrientation
import platform.darwin.NSObject
import platform.darwin.dispatch_get_main_queue

class BarcodeScannerCoordinator(
    val onScanned: (List<BarcodeDetection>) -> Unit,
) : AVCaptureMetadataOutputObjectsDelegateProtocol, NSObject() {

    private val logger = Logger.withTag("BarcodeScannerCoordinator")

    private var previewLayer: AVCaptureVideoPreviewLayer? = null
    private lateinit var captureSession: AVCaptureSession

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    fun prepare(layer: CALayer, allowedMetadataTypes: List<AVMetadataObjectType>) {
        captureSession = AVCaptureSession()
        val devices =
            AVCaptureDevice.devicesWithMediaType(AVMediaTypeVideo).map { it as AVCaptureDevice }

        val device = devices.firstOrNull { device ->
            device.position == AVCaptureDevicePositionBack
        } ?: run {
            logger.e("Could not find back camera")
            AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo)
        }

        if (device == null) {
            return
        }

        logger.d("Initializing video input")
        val videoInput = memScoped {
            val error: ObjCObjectVar<NSError?> = alloc<ObjCObjectVar<NSError?>>()
            val videoInput = AVCaptureDeviceInput(device = device, error = error.ptr)
            if (error.value != null) {
                logger.e { error.value.toString() }
                null
            } else {
                videoInput
            }
        }

        logger.d("Adding video input")
        if (videoInput != null && captureSession.canAddInput(videoInput)) {
            captureSession.addInput(videoInput)
        } else {
            logger.e("Could not add input")
            return
        }

        val metadataOutput = AVCaptureMetadataOutput()

        logger.d("Adding metadata output")
        if (captureSession.canAddOutput(metadataOutput)) {
            captureSession.addOutput(metadataOutput)

            metadataOutput.setMetadataObjectsDelegate(this, queue = dispatch_get_main_queue())
            metadataOutput.metadataObjectTypes = allowedMetadataTypes
        } else {
            logger.e("Could not add output")
            return
        }
        logger.d("Adding preview layer")
        previewLayer = AVCaptureVideoPreviewLayer(session = captureSession).also {
            it.frame = layer.bounds
            it.videoGravity = AVLayerVideoGravityResizeAspectFill
            logger.d("Set orientation")
            setCurrentOrientation(newOrientation = UIDevice.currentDevice.orientation)
            logger.d("Adding sublayer")
            layer.bounds.useContents {
                logger.d("Bounds: ${this.size.width}x${this.size.height}")
            }
            layer.frame.useContents {
                logger.d("Frame: ${this.size.width}x${this.size.height}")
            }
            layer.addSublayer(it)
        }

        logger.d("Launching capture session")
        GlobalScope.launch(Dispatchers.Default) {
            captureSession.startRunning()
        }
    }

    fun setCurrentOrientation(newOrientation: UIDeviceOrientation) {
        when (newOrientation) {
            UIDeviceOrientation.UIDeviceOrientationLandscapeLeft ->
                previewLayer?.connection?.videoOrientation = AVCaptureVideoOrientationLandscapeRight

            UIDeviceOrientation.UIDeviceOrientationLandscapeRight ->
                previewLayer?.connection?.videoOrientation = AVCaptureVideoOrientationLandscapeLeft

            UIDeviceOrientation.UIDeviceOrientationPortrait ->
                previewLayer?.connection?.videoOrientation = AVCaptureVideoOrientationPortrait

            UIDeviceOrientation.UIDeviceOrientationPortraitUpsideDown ->
                previewLayer?.connection?.videoOrientation =
                    AVCaptureVideoOrientationPortraitUpsideDown

            else ->
                previewLayer?.connection?.videoOrientation = AVCaptureVideoOrientationPortrait
        }
    }

    override fun captureOutput(
        output: platform.AVFoundation.AVCaptureOutput,
        didOutputMetadataObjects: List<*>,
        fromConnection: platform.AVFoundation.AVCaptureConnection,
    ) {
        val scanResults = didOutputMetadataObjects.mapNotNull {
            val metadataObj = it as? AVMetadataMachineReadableCodeObject ?: return@mapNotNull null
            (previewLayer?.transformedMetadataObjectForMetadataObject(metadataObj) as? AVMetadataMachineReadableCodeObject)
                ?.toBarcodeDetection()
        }

        onScanned(scanResults)
    }

    fun setFrame(rect: CValue<CGRect>) {
        previewLayer?.setFrame(rect)
    }
}
