package hu.piware.bricklog.baselineprofile.journeys

import androidx.benchmark.macro.MacrobenchmarkScope
import hu.piware.bricklog.baselineprofile.util.waitUntilGone
import hu.piware.bricklog.baselineprofile.util.waitUntilScreenLoads

fun MacrobenchmarkScope.notificationsJourney() {
    waitUntilScreenLoads("notifications_screen")

    device.pressBack()
    waitUntilGone("notifications_screen")
}
