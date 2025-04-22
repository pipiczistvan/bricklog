package hu.piware.bricklog

import androidx.compose.ui.window.ComposeUIViewController
import hu.piware.bricklog.util.AppInitializer

fun MainViewController() = ComposeUIViewController(
    configure = {
        AppInitializer.initialize()
    }
) { App() }