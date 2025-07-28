package hu.piware.bricklog.baselineprofile.journeys

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.Direction
import hu.piware.bricklog.baselineprofile.util.waitAndClick
import hu.piware.bricklog.baselineprofile.util.waitAndFling
import hu.piware.bricklog.baselineprofile.util.waitUntilGone
import hu.piware.bricklog.baselineprofile.util.waitUntilObject
import hu.piware.bricklog.baselineprofile.util.waitUntilObjects
import hu.piware.bricklog.baselineprofile.util.waitUntilScreenLoads

fun MacrobenchmarkScope.dashboardJourney() {
    waitUntilScreenLoads("dashboard_screen")

    scrollBody()
    scrollFeaturedThemes()

    openDrawer()
    openCollectionEdit()
    collectionEditJourney()

    //openDrawer()
    openNotifications()
    notificationsJourney()

    openDrawer()
    openAppearance()
    appearanceJourney()

    openDrawer()
    openAbout()
    aboutJourney()

    openLatestSetsList()
    setListJourney()

    openFirstLatestSet()
    waitUntilScreenLoads("set_detail_screen")
    device.pressBack()
    waitUntilGone("set_detail_screen")

    openDrawer()
    openLogin()
    loginJourney()

//    searchBarJourney()
}

private fun MacrobenchmarkScope.scrollBody() {
    val body = waitUntilObject("dashboard:body")
    body.setGestureMargin(device.displayWidth / 5)
    body.waitAndFling(Direction.DOWN)
    device.waitForIdle()
    body.waitAndFling(Direction.UP)
    device.waitForIdle()
}

private fun MacrobenchmarkScope.scrollFeaturedThemes() {
    val list = waitUntilObject("dashboard:featured_themes")
    list.setGestureMargin(device.displayWidth / 5)
    list.waitAndFling(Direction.RIGHT)
    device.waitForIdle()
    list.waitAndFling(Direction.LEFT)
    device.waitForIdle()
}

private fun MacrobenchmarkScope.openLatestSetsList() {
    val latestSetsFeaturedSetsRow = waitUntilObject("featured_sets:latest_sets")
    val featuredSetsTitle = latestSetsFeaturedSetsRow.waitUntilObject("featured_sets_row:title")
    featuredSetsTitle.waitAndClick()
}

private fun MacrobenchmarkScope.openFirstLatestSet() {
    val latestSetsFeaturedSetsRow = waitUntilObject("featured_sets:latest_sets")
    val cards = latestSetsFeaturedSetsRow.waitUntilObjects("set_card")
    cards[0].waitAndClick()
}

fun MacrobenchmarkScope.searchBarJourney() {
    waitUntilObject("dashboard_screen:search_bar").waitAndClick()
    waitUntilObject("search_bar:content")
    device.pressBack() // focus
    device.pressBack()
    waitUntilGone("search_bar:content")
}
