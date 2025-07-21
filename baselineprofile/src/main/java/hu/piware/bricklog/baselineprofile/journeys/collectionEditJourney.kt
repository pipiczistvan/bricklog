package hu.piware.bricklog.baselineprofile.journeys

import androidx.benchmark.macro.MacrobenchmarkScope
import hu.piware.bricklog.baselineprofile.util.waitUntilGone
import hu.piware.bricklog.baselineprofile.util.waitUntilScreenLoads

fun MacrobenchmarkScope.collectionEditJourney() {
    waitUntilScreenLoads("collection_edit_screen")

    device.pressBack()
    waitUntilGone("collection_edit_screen")
}
