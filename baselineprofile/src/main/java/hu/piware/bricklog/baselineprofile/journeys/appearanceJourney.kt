package hu.piware.bricklog.baselineprofile.journeys

import androidx.benchmark.macro.MacrobenchmarkScope
import hu.piware.bricklog.baselineprofile.util.waitAndClick
import hu.piware.bricklog.baselineprofile.util.waitUntilGone
import hu.piware.bricklog.baselineprofile.util.waitUntilObjects
import hu.piware.bricklog.baselineprofile.util.waitUntilScreenLoads

fun MacrobenchmarkScope.appearanceJourney() {
    waitUntilScreenLoads("appearance_screen")

    val themeOptionButtons = waitUntilObjects("theme_settings:theme_option")
    themeOptionButtons.forEach {
        it.waitAndClick()
    }

    device.pressBack()
    waitUntilGone("appearance_screen")
}
