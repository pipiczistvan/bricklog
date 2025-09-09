@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package hu.piware.bricklog.feature.set.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics

@Composable
fun CombinedFilledIconButton(
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.filledIconButtonColors(),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable () -> Unit,
) =
    CombinedSurfaceIconButton(
        onClick = onClick,
        onLongClick = onLongClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        border = null,
        interactionSource = interactionSource,
        content = content,
    )

@Composable
private fun CombinedSurfaceIconButton(
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    colors: IconButtonColors,
    border: BorderStroke?,
    interactionSource: MutableInteractionSource?,
    content: @Composable () -> Unit,
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

    CombinedSurfaceIconButton(
        onClick = onClick,
        onLongClick = onLongClick,
        modifier = modifier,
        enabled = enabled,
        shape = IconButtonDefaults.filledShape,
        colors = colors,
        border = border,
        interactionSource = interactionSource,
        content = content,
    )
}

@Composable
private fun CombinedSurfaceIconButton(
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    shape: Shape,
    colors: IconButtonColors,
    border: BorderStroke?,
    interactionSource: MutableInteractionSource?,
    content: @Composable () -> Unit,
) =
    Surface(
        modifier = modifier
            .semantics { role = Role.Button }
            .combinedClickable(
                enabled = enabled,
                onClick = onClick,
                onLongClick = onLongClick,
                interactionSource = interactionSource,
            ),
        shape = shape,
        color = colors.containerColor(enabled),
        contentColor = colors.contentColor(enabled),
        border = border,
    ) {
        Box(
            modifier = Modifier.size(IconButtonDefaults.smallContainerSize()),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }

@Stable
private fun IconButtonColors.containerColor(enabled: Boolean): Color =
    if (enabled) containerColor else disabledContainerColor

@Stable
private fun IconButtonColors.contentColor(enabled: Boolean): Color =
    if (enabled) contentColor else disabledContentColor
