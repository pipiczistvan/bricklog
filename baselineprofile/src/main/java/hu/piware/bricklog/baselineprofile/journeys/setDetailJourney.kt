package hu.piware.bricklog.baselineprofile.journeys

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.Direction
import hu.piware.bricklog.baselineprofile.util.waitAndClick
import hu.piware.bricklog.baselineprofile.util.waitAndFling
import hu.piware.bricklog.baselineprofile.util.waitUntilGone
import hu.piware.bricklog.baselineprofile.util.waitUntilObject
import hu.piware.bricklog.baselineprofile.util.waitUntilScreenLoads

fun MacrobenchmarkScope.setDetailJourney() {
    waitUntilScreenLoads("set_detail_screen")

    waitUntilObject("set_detail:image")
        .waitAndClick()
    setImageJourney()

    waitUntilObject("set_detail:favourite_btn")
        .waitAndClick()
        .waitAndClick()

    scrollBody()

    device.pressBack()
    waitUntilGone("set_detail_screen")
}

private fun MacrobenchmarkScope.scrollBody() {
    val body = waitUntilObject("set_detail:body")
    body.setGestureMargin(device.displayWidth / 5)
    body.waitAndFling(Direction.DOWN)
    device.waitForIdle()
}
