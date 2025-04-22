package hu.piware.bricklog.feature.set.presentation.set_list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

enum class ChipSize {
    SMALL, REGULAR
}

@Composable
fun SetChip(
    modifier: Modifier = Modifier,
    size: ChipSize = ChipSize.REGULAR,
    color: Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
    content: @Composable RowScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .widthIn(
                min = when (size) {
                    ChipSize.SMALL -> 50.dp
                    ChipSize.REGULAR -> 80.dp
                }
            )
            .clip(RoundedCornerShape(8.dp))
            .background(color)
            .padding(
                vertical = 4.dp,
                horizontal = 6.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            content()
        }
    }
}

@Composable
fun SetAttributeChip(
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    size: ChipSize = ChipSize.REGULAR,
    color: Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
    drawableResource: DrawableResource? = null,
) {
    SetChip(
        size = size,
        color = color
    ) {
        drawableResource?.let { res ->
            Image(
                modifier = Modifier
                    .height(
                        height = when (size) {
                            ChipSize.SMALL -> 14.dp
                            ChipSize.REGULAR -> 18.dp
                        }
                    )
                    .aspectRatio(1f, matchHeightConstraintsFirst = true),
                painter = painterResource(res),
                contentDescription = null
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            fontSize = when (size) {
                ChipSize.SMALL -> 12.sp
                ChipSize.REGULAR -> 16.sp
            },
            color = textColor
        )
    }
}
