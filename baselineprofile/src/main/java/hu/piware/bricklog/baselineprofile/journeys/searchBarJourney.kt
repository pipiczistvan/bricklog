package hu.piware.bricklog.baselineprofile.journeys

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.Direction
import hu.piware.bricklog.baselineprofile.util.waitUntilGone
import hu.piware.bricklog.baselineprofile.util.waitUntilObject
import hu.piware.bricklog.baselineprofile.util.waitUntilObjects
import hu.piware.bricklog.baselineprofile.util.waitUntilScreenLoads

fun MacrobenchmarkScope.searchBarJourney() {
    waitUntilObject("search_bar:input_field").click()

    // status filter
    //waitUntilObject("search_bar:status_chip").click()
    //waitUntilObject("search_bar:status_filter_bottom_sheet")
    //device.pressBack()
    //waitUntilGone("search_bar:status_filter_bottom_sheet")

    // release date filter
    //waitUntilObject("search_bar:release_date_chip").click()
    //waitUntilObject("search_bar:date_filter_bottom_sheet")
    //device.pressBack()
    //waitUntilGone("search_bar:date_filter_bottom_sheet")

    // theme filter
    waitUntilObject("search_bar:theme_chip").click()
    themeMultiSelectJourney()

    // show all
    waitUntilObject("search_bar:show_all_button").click()
    setListJourney()

    device.pressBack()
    waitUntilGone("search_bar:show_all_button")

    // settings
    waitUntilObject("search_bar:settings_button").click()
    settingsJourney()

    device.pressBack()
}

fun MacrobenchmarkScope.themeMultiSelectJourney() {
    waitUntilScreenLoads("theme_multi_select_screen")
    val list = waitUntilObject("theme_multi_select:list")
    list.setGestureMargin(device.displayWidth / 5)
    list.fling(Direction.DOWN)
    device.waitForIdle()
    device.pressBack()
    waitUntilGone("theme_multi_select_screen")
}

fun MacrobenchmarkScope.setListJourney() {
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

fun MacrobenchmarkScope.setDetailJourney() {
    waitUntilScreenLoads("set_detail_screen")

    waitUntilObject("set_detail:image").click()
    setImageJourney()

    device.pressBack()
    waitUntilGone("set_detail_screen")
}

fun MacrobenchmarkScope.setImageJourney() {
    waitUntilScreenLoads("set_image_screen")

    device.pressBack()
    waitUntilGone("set_image_screen")
}

fun MacrobenchmarkScope.settingsJourney() {
    waitUntilScreenLoads("settings_screen")

    device.pressBack()
    waitUntilGone("settings_screen")
}
