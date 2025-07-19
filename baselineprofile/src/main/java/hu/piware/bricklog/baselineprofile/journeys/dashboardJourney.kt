package hu.piware.bricklog.baselineprofile.journeys

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.Direction
import hu.piware.bricklog.baselineprofile.util.waitUntilGone
import hu.piware.bricklog.baselineprofile.util.waitUntilObject
import hu.piware.bricklog.baselineprofile.util.waitUntilObjects
import hu.piware.bricklog.baselineprofile.util.waitUntilScreenLoads

fun MacrobenchmarkScope.dashboardJourney() {
    waitUntilScreenLoads("dashboard_screen")

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

    val latestSetsFeaturedSetsRow = waitUntilObject("dashboard:latest_sets_row")
    val featuredSetsTitle = latestSetsFeaturedSetsRow.waitUntilObject("featured_sets_row:title")
    featuredSetsTitle.click()
    device.waitForIdle()

    setListScreenJourney()
    searchBarJourney()
}

private fun MacrobenchmarkScope.setListScreenJourney() {
    waitUntilScreenLoads("set_list_screen")

    val list = waitUntilObject("set_list:sets")
    list.setGestureMargin(device.displayWidth / 5)
    list.fling(Direction.DOWN)
    device.waitForIdle()
    list.fling(Direction.UP)
    device.waitForIdle()

    val items = waitUntilObjects("set_list:item")
    val index = (iteration ?: 0) % items.size
    items[index].click()

    setDetailJourney()

    device.pressBack()
    waitUntilGone("set_list_screen")
}

private fun MacrobenchmarkScope.setDetailJourney() {
    waitUntilScreenLoads("set_detail_screen")

    waitUntilObject("set_detail:image").click()

    setImageJourney()

    device.pressBack()
    waitUntilGone("set_detail_screen")
}

private fun MacrobenchmarkScope.setImageJourney() {
    waitUntilScreenLoads("set_image_screen")

    device.pressBack()
    waitUntilGone("set_image_screen")
}

fun MacrobenchmarkScope.searchBarJourney() {
    waitUntilObject("search_bar:input_field").click()
    waitUntilObject("search_bar:content")

    device.pressBack()
    waitUntilGone("set_image_screen")
}
