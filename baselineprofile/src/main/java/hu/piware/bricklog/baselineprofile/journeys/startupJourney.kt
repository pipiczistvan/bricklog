package hu.piware.bricklog.baselineprofile.journeys

import androidx.benchmark.macro.MacrobenchmarkScope
import hu.piware.bricklog.baselineprofile.util.waitUntilScreenLoads

fun MacrobenchmarkScope.startupJourney() {
    waitUntilScreenLoads("dashboard_screen", 30_000)
}
