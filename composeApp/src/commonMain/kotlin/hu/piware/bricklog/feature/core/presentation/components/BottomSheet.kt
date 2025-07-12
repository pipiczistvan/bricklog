package hu.piware.bricklog.feature.core.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import hu.piware.bricklog.ui.theme.BricklogTheme
import hu.piware.bricklog.ui.theme.Dimens
import hu.piware.bricklog.ui.theme.Shapes
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun BottomSheetHeader(
    title: String,
    onCloseClick: () -> Unit = { },
    primaryActionIcon: ImageVector? = null,
    onPrimaryActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.SmallPadding.size)
    ) {
        IconButton(onClick = onCloseClick) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Text(
            modifier = Modifier.padding(horizontal = Dimens.MediumPadding.size),
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        if (primaryActionIcon != null && onPrimaryActionClick != null) {
            IconButton(
                onClick = onPrimaryActionClick
            ) {
                Icon(
                    imageVector = primaryActionIcon,
                    contentDescription = null
                )
            }
        } else {
            PlaceHolderIconButton()
        }
    }
}

@Composable
fun BottomSheetOption(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary
                .copy(alpha = .2f)
        } else Color.Transparent,
        animationSpec = tween(
            durationMillis = 400,
            easing = LinearOutSlowInEasing
        ), label = "backgroundColor"
    )

    Box(
        modifier = modifier
            .clip(Shapes.large)
            .background(
                color = backgroundColor,
                shape = Shapes.large
            )
            .clickable { onClick() }
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.MediumPadding.size)
        ) {
            content()
        }
    }
}

@Composable
fun BottomSheetButton(
    title: String,
    icon: ImageVector,
    color: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = .2f),
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(Shapes.large)
            .background(
                color = color,
                shape = Shapes.large
            )
            .clickable { onClick() }
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.MediumPadding.size)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(Dimens.SmallPadding.size))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun PlaceHolderIconButton() {
    IconButton(
        onClick = { },
        enabled = false,
        modifier = Modifier.alpha(0f)
    ) {
        Icon(
            imageVector = Icons.Outlined.Close,
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun BottomSheetOptionPreview() {
    BricklogTheme {
        BottomSheetOption(
            selected = false,
            onClick = { },
        ) {
            Text("OK")
        }
    }
}

@Preview
@Composable
private fun BottomSheetButtonPreview() {
    BricklogTheme {
        BottomSheetButton(
            title = "OK",
            icon = Icons.Outlined.Close,
            onClick = { }
        )
    }
}
