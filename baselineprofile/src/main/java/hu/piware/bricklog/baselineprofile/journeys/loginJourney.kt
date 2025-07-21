package hu.piware.bricklog.baselineprofile.journeys

import androidx.benchmark.macro.MacrobenchmarkScope
import hu.piware.bricklog.baselineprofile.util.waitAndClick
import hu.piware.bricklog.baselineprofile.util.waitUntilGone
import hu.piware.bricklog.baselineprofile.util.waitUntilObject
import hu.piware.bricklog.baselineprofile.util.waitUntilScreenLoads

fun MacrobenchmarkScope.loginJourney() {
    waitUntilScreenLoads("login_screen")

    val emailField = waitUntilObject("email_field")
    emailField.text = "test@test.com"

    val passwordField = waitUntilObject("password_field")
    passwordField.text = "test"

    val submitButton = waitUntilObject("submit_button")
    submitButton.waitAndClick()

    waitUntilGone("login_screen")
}
