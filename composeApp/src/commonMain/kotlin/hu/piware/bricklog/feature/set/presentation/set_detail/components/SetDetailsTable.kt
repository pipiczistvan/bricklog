package hu.piware.bricklog.feature.set.presentation.set_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import hu.piware.bricklog.feature.set.presentation.set_detail.util.TableColumn
import hu.piware.bricklog.feature.set.presentation.set_detail.util.createFirstSetDetailTableColumns
import hu.piware.bricklog.mock.PreviewData
import hu.piware.bricklog.ui.theme.BricklogTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SetDetailsTable(columns: List<TableColumn>, swapColors: Boolean) {
    val columnWeight = remember { 1f / columns.size }

    val (oddBackgroundColor, evenBackgroundColor) = when (swapColors) {
        false -> Pair(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
        )

        true -> Pair(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f),
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        )
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(oddBackgroundColor)
        ) {
            columns.forEach { column ->
                Box(
                    modifier = Modifier
                        .weight(columnWeight)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when (column) {
                        is TableColumn.DrawableTableColumn ->
                            Icon(
                                modifier = Modifier
                                    .size(22.dp),
                                painter = painterResource(column.drawableRes),
                                contentDescription = stringResource(column.titleRes)
                            )

                        is TableColumn.ImageVectorTableColumn ->
                            Icon(
                                modifier = Modifier
                                    .size(22.dp),
                                imageVector = column.icon,
                                contentDescription = stringResource(column.titleRes)
                            )
                    }

                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(evenBackgroundColor)
        ) {
            columns.forEach { column ->
                Box(
                    modifier = Modifier
                        .weight(columnWeight)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(column.titleRes),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(oddBackgroundColor)
        ) {
            columns.forEach { column ->
                Box(
                    modifier = Modifier
                        .weight(columnWeight)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = column.data ?: "-",
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SetDetailsTablePreview() {
    BricklogTheme {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            SetDetailsTable(
                columns = createFirstSetDetailTableColumns(PreviewData.sets[0]),
                swapColors = false
            )
        }
    }
}
