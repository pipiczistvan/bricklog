package hu.piware.bricklog.feature.core.presentation.util

import androidx.compose.ui.Modifier

// https://stackoverflow.com/a/72554087

inline fun Modifier.applyIf(condition: Boolean, modifier: Modifier.() -> Modifier): Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}
