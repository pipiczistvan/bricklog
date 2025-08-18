package hu.piware.bricklog.feature.core.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import hu.piware.bricklog.ui.theme.Dimens

@Composable
fun StaggeredVerticalGrid(
    modifier: Modifier = Modifier,
    numColumns: Int = 2,
    spacing: Dp = Dimens.SmallPadding.size,
    content: @Composable () -> Unit,
) {
    Layout(
        content = content,
        modifier = modifier,
    ) { measurables, constraints ->
        val columnWidth =
            (constraints.maxWidth - spacing.roundToPx() * (numColumns - 1)) / numColumns
        val itemConstraints = constraints.copy(maxWidth = columnWidth)

        val columnHeights = IntArray(numColumns)
        val placeables = measurables.map { measurable ->
            val placeable = measurable.measure(itemConstraints)
            val column = columnHeights.indexOf(columnHeights.minOrNull()!!)
            val x = column * (columnWidth + spacing.roundToPx())
            val y = columnHeights[column]
            columnHeights[column] += placeable.height + spacing.roundToPx()
            Triple(placeable, x, y)
        }

        val height = columnHeights.maxOrNull() ?: constraints.minHeight
        layout(constraints.maxWidth, height) {
            placeables.forEach { (placeable, x, y) ->
                placeable.placeRelative(x, y)
            }
        }
    }
}
