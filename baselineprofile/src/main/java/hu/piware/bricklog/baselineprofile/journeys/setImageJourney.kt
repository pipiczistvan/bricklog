package hu.piware.bricklog.baselineprofile.journeys

import androidx.benchmark.macro.MacrobenchmarkScope
import hu.piware.bricklog.baselineprofile.util.waitUntilGone
import hu.piware.bricklog.baselineprofile.util.waitUntilScreenLoads

fun MacrobenchmarkScope.setImageJourney() {
    waitUntilScreenLoads("set_image_screen")

    device.pressBack()
    waitUntilGone("set_image_screen")
}
