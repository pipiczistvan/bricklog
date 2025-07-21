package hu.piware.bricklog.baselineprofile.journeys

import androidx.benchmark.macro.MacrobenchmarkScope
import hu.piware.bricklog.baselineprofile.util.waitAndClick
import hu.piware.bricklog.baselineprofile.util.waitUntilObject
import hu.piware.bricklog.baselineprofile.util.waitUntilObjects

fun MacrobenchmarkScope.openDrawer() {
    waitUntilObject("search_bar_input_field:drawer_button")
        .waitAndClick()
}

fun MacrobenchmarkScope.openCollectionEdit(index: Int = 0) {
    val collectionEditButtons = waitUntilObjects("navigation_drawer:collection_edit_btn")
    collectionEditButtons[index].waitAndClick()
}

fun MacrobenchmarkScope.openLogin() {
    waitUntilObject("navigation_drawer:login_btn")
        .waitAndClick()
}

fun MacrobenchmarkScope.openNotifications() {
    pressButton("notifications_btn")
}

fun MacrobenchmarkScope.openAppearance() {
    pressButton("appearance_btn")
}

fun MacrobenchmarkScope.openAbout() {
    pressButton("about_btn")
}

private fun MacrobenchmarkScope.pressButton(buttonName: String) {
    waitUntilObject("navigation_drawer:$buttonName")
        .waitAndClick()
}
