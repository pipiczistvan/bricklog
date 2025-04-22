package hu.piware.bricklog.baselineprofile.journeys

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.Direction
import hu.piware.bricklog.baselineprofile.util.waitUntilObject
import hu.piware.bricklog.baselineprofile.util.waitUntilScreenLoads

fun MacrobenchmarkScope.dashboardJourney() {
    waitUntilScreenLoads("dashboard_screen", 30_000)

    val body = waitUntilObject("dashboard:body")
    body.setGestureMargin(device.displayWidth / 5)
    body.fling(Direction.DOWN)
    device.waitForIdle()
    body.fling(Direction.UP)
    device.waitForIdle()

    val list = waitUntilObject("dashboard:featured_themes")
    list.setGestureMargin(device.displayWidth / 5)
    list.fling(Direction.RIGHT)
    device.waitForIdle()
    list.fling(Direction.LEFT)
    device.waitForIdle()

    //searchBarJourney()
}