@file:OptIn(ExperimentalSharedTransitionApi::class)

package hu.piware.bricklog.feature.core.presentation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import hu.piware.bricklog.LocalNavAnimatedVisibilityScope
import hu.piware.bricklog.LocalSharedTransitionScope

@Composable
fun Modifier.sharedElement(
    key: String,
): Modifier {
    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current

    val modifier = if (sharedTransitionScope != null && animatedVisibilityScope != null) {
        with(sharedTransitionScope) {
            sharedElement(
                sharedContentState = rememberSharedContentState(key = key),
                animatedVisibilityScope = animatedVisibilityScope,
            )
        }
    } else {
        Modifier
    }

    return this.then(modifier)
}
