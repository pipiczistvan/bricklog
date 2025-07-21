package hu.piware.bricklog.baselineprofile.journeys

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.Direction
import hu.piware.bricklog.baselineprofile.util.waitAndFling
import hu.piware.bricklog.baselineprofile.util.waitUntilGone
import hu.piware.bricklog.baselineprofile.util.waitUntilObject
import hu.piware.bricklog.baselineprofile.util.waitUntilScreenLoads

fun MacrobenchmarkScope.changelogJourney() {
    waitUntilScreenLoads("changelog_screen")

    scrollBody()

    device.pressBack()
    waitUntilGone("changelog_screen")
}

private fun MacrobenchmarkScope.scrollBody() {
    val body = waitUntilObject("changelog:body")
    body.setGestureMargin(device.displayWidth / 5)
    body.waitAndFling(Direction.DOWN)
    device.waitForIdle()
}
