package hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import hu.piware.bricklog.ui.theme.BricklogTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SearchBarChip(
    title: String,
    isDefaultSelected: Boolean,
    showTrailingIcon: Boolean = false,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val colors = when (isDefaultSelected) {
        true -> AssistChipDefaults.assistChipColors()
        else -> AssistChipDefaults.assistChipColors()
            .copy(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
    }

    AssistChip(
        modifier = modifier,
        label = {
            Text(text = title)
        },
        colors = colors,
        trailingIcon = {
            if (showTrailingIcon) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                )
            }
        },
        enabled = enabled,
        onClick = onClick
    )
}

@Preview
@Composable
private fun SearchBarChipPreview() {
    BricklogTheme {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            SearchBarChip(
                title = "Search",
                isDefaultSelected = true,
                showTrailingIcon = true,
                onClick = {}
            )
        }
    }
}
