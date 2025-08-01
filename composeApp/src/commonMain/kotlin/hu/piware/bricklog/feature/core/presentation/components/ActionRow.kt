package hu.piware.bricklog.feature.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.piware.bricklog.ui.theme.BricklogTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class ActionNavigationType {
    IN_APP,
    LINK,
}

@Composable
fun ActionRow(
    title: String,
    onClick: () -> Unit,
    startIcon: @Composable () -> Unit = {},
    navigationType: ActionNavigationType? = null,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            startIcon()
            Text(
                modifier = Modifier.padding(8.dp),
                text = title,
            )
            if (navigationType != null) {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = when (navigationType) {
                        ActionNavigationType.IN_APP -> Icons.AutoMirrored.Filled.KeyboardArrowRight
                        ActionNavigationType.LINK -> Icons.AutoMirrored.Filled.OpenInNew
                    },
                    contentDescription = null,
                    tint = LocalContentColor.current.copy(alpha = 0.6f),
                )
            }
        }
    }
}

@Preview
@Composable
private fun ActionRowPreview() {
    BricklogTheme {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
        ) {
            ActionRow(
                title = "Test",
                onClick = {},
            )
        }
    }
}
