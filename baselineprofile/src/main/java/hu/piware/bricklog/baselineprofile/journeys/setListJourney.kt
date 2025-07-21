package hu.piware.bricklog.baselineprofile.journeys

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.Direction
import hu.piware.bricklog.baselineprofile.util.waitAndClick
import hu.piware.bricklog.baselineprofile.util.waitAndFling
import hu.piware.bricklog.baselineprofile.util.waitUntilGone
import hu.piware.bricklog.baselineprofile.util.waitUntilObject
import hu.piware.bricklog.baselineprofile.util.waitUntilObjects
import hu.piware.bricklog.baselineprofile.util.waitUntilScreenLoads

fun MacrobenchmarkScope.setListJourney() {
    waitUntilScreenLoads("set_list_screen")

    val list = waitUntilObject("set_list:sets")
    list.setGestureMargin(device.displayWidth / 5)
    list.waitAndFling(Direction.DOWN)
    device.waitForIdle()
    list.waitAndFling(Direction.UP)
    device.waitForIdle()

    val items = waitUntilObjects("set_list:item")
    val index = (iteration ?: 0) % items.size
    items[index].waitAndClick()

    setDetailJourney()

    waitUntilObject("set_list:display_mode_btn")
        .waitAndClick()
        .waitAndClick()
        .waitAndClick()

    device.pressBack()
    waitUntilGone("set_list_screen")
}
