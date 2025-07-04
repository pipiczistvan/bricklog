package hu.piware.barcode_scanner

import android.graphics.Rect
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.core.util.Consumer
import co.touchlab.kermit.Logger
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.common.Barcode
import kotlin.math.max
import kotlin.math.sqrt

class CachingMlKitBarcodeConsumer(
    private val scanner: BarcodeScanner,
    private val onScanResult: (List<BarcodeDetection>) -> Unit,
    private val keepAliveLimit: Int = 10,
    private val throttleMs: Long = 25L,
) : Consumer<MlKitAnalyzer.Result> {

    private val logger = Logger.withTag("CachingMlKitBarcodeConsumer")

    private val cache = mutableMapOf<String, MutableList<CachedBarcode>>()
    private var lastTime = 0L

    override fun accept(value: MlKitAnalyzer.Result) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime < throttleMs) {
            logger.d { "Throttling scan result" }
            return
        }
        lastTime = currentTime
        logger.d { "Accepting scan result" }

        val scannedResults = value.getValue(scanner) ?: emptyList()
        updateCache(scannedResults)
        cacheNewBarcodes(scannedResults)

        onScanResult(cache.flatMap { it.value }.mapNotNull { it.barcode.toBarcodeDetection() })
    }

    private fun cacheNewBarcodes(barcodes: List<Barcode>) {
        barcodes.filter { it.rawValue != null }.groupBy { it.rawValue }
            .forEach { (barcode, result) ->
                if (!cache.containsKey(barcode)) {
                    logger.d { "Adding new barcode to cache: barcode: $barcode" }
                    cache[barcode!!] =
                        result.map { CachedBarcode(it, keepAliveLimit) }.toMutableList()
                }
            }
    }

    private fun updateCache(barcodes: List<Barcode>) {
        cache.keys.forEach { code ->
            val cachedBarcodes = cache.getOrDefault(code, mutableListOf())
            val newBarcodes = barcodes.filter { it.rawValue == code }
            val pairedBarcodes = pairCachedBarcodeWithNewBarcode(cachedBarcodes, newBarcodes)

            // Refresh cache or reuse if necessary
            cachedBarcodes.forEach {
                val newBarcodeForCached = pairedBarcodes[it]

                if (newBarcodeForCached != null) {
                    it.barcode = newBarcodeForCached
                    it.reuseCounter = keepAliveLimit
                    logger.d { "Refreshing cached barcode, remaining reuse counter: ${it.reuseCounter}" }
                } else {
                    it.reuseCounter--
                    logger.d { "Reusing cached barcode, remaining reuse counter: ${it.reuseCounter}" }
                }
            }

            cachedBarcodes.removeIf {
                if (it.reuseCounter < 1) {
                    logger.d { "Removing cached barcode from cache" }
                }
                it.reuseCounter < 1
            }

            newBarcodes.forEach {
                if (!pairedBarcodes.containsValue(it)) {
                    logger.d { "Adding unpaired barcode to cache: barcode: ${it.rawValue}" }
                    cachedBarcodes.add(CachedBarcode(it, keepAliveLimit))
                }
            }
        }
    }

    private fun pairCachedBarcodeWithNewBarcode(
        cachedBarcodes: List<CachedBarcode>,
        newBarcodes: List<Barcode>,
    ): Map<CachedBarcode, Barcode> {
        val result = mutableMapOf<CachedBarcode, Barcode>()

        newBarcodes.forEach { scannedBarcode ->
            val closestBarcode =
                (cachedBarcodes - result.keys).findClosestTo(scannedBarcode) ?: return@forEach
            result[closestBarcode] = scannedBarcode
        }

        return result
    }

    private fun List<CachedBarcode>.findClosestTo(barcode: Barcode): CachedBarcode? {
        return minByOrNull { distanceBetween(it.barcode.boundingBox!!, barcode.boundingBox!!) }
    }

    private fun distanceBetween(a: Rect, b: Rect): Float {
        // Compute horizontal gap (positive if disjoint, 0 if overlapping)
        val dx = max(0, max(b.left - a.right, a.left - b.right))
        // Compute vertical gap
        val dy = max(0, max(b.top - a.bottom, a.top - b.bottom))
        // Full distance is Euclidean
        return sqrt((dx * dx + dy * dy).toFloat())
    }

    private data class CachedBarcode(
        var barcode: Barcode,
        var reuseCounter: Int,
    )
}
