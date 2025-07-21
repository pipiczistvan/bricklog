package hu.piware.bricklog.baselineprofile.journeys

import androidx.benchmark.macro.MacrobenchmarkScope
import hu.piware.bricklog.baselineprofile.util.waitAndClick
import hu.piware.bricklog.baselineprofile.util.waitUntilGone
import hu.piware.bricklog.baselineprofile.util.waitUntilObject
import hu.piware.bricklog.baselineprofile.util.waitUntilScreenLoads

fun MacrobenchmarkScope.aboutJourney() {
    waitUntilScreenLoads("about_screen")

    waitUntilObject("about:changelog_btn").waitAndClick()
    changelogJourney()

    waitUntilObject("about:license_btn").waitAndClick()
    licenseJourney()

    device.pressBack()
    waitUntilGone("about_screen")
}
