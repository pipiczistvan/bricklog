@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalComposeUiApi::class)

package hu.piware.bricklog.feature.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_dashboard_navigation_drawer_btn_login
import org.jetbrains.compose.resources.stringResource

@Composable
fun AuthenticationOverlay(
    isAuthenticated: Boolean,
    onLoginClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    BlurOverlay(
        isOverlayActive = !isAuthenticated,
        content = content,
    ) {
        Button(onClick = onLoginClick) {
            Text(text = stringResource(Res.string.feature_set_dashboard_navigation_drawer_btn_login))
        }
    }
}

@Composable
fun LoadingOverlay(
    isLoading: Boolean,
    blockBackNavigation: Boolean = false,
    content: @Composable () -> Unit,
) {
    if (blockBackNavigation) {
        BackHandler { }
    }

    BlurOverlay(
        isOverlayActive = isLoading,
        content = content,
    ) {
        ContainedLoadingIndicator()
    }
}

@Composable
fun BlurOverlay(
    isOverlayActive: Boolean,
    content: @Composable () -> Unit,
    overlayContent: @Composable () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier.blur(
                radius = if (isOverlayActive) 10.dp else 0.dp,
                edgeTreatment = BlurredEdgeTreatment.Unbounded,
            ),
        ) {
            content()
        }

        if (isOverlayActive) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {},
                    ),
            )
            overlayContent()
        }
    }
}
