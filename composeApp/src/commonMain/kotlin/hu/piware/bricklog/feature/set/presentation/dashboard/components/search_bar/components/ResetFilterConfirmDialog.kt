package hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_search_reset_filter_confirm_dialog_btn_cancel
import bricklog.composeapp.generated.resources.feature_set_search_reset_filter_confirm_dialog_btn_confirm
import bricklog.composeapp.generated.resources.feature_set_search_reset_filter_confirm_dialog_label
import bricklog.composeapp.generated.resources.feature_set_search_reset_filter_confirm_dialog_title
import hu.piware.bricklog.ui.theme.BricklogTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ResetFilterConfirmDialog(
    onConfirmation: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Default.Replay,
                contentDescription = null
            )
        },
        title = {
            Text(stringResource(Res.string.feature_set_search_reset_filter_confirm_dialog_title))
        },
        text = {
            Text(stringResource(Res.string.feature_set_search_reset_filter_confirm_dialog_label))
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onConfirmation
            ) {
                Text(stringResource(Res.string.feature_set_search_reset_filter_confirm_dialog_btn_confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(stringResource(Res.string.feature_set_search_reset_filter_confirm_dialog_btn_cancel))
            }
        }
    )
}

@Preview
@Composable
fun ResetFilterConfirmDialogPreview() {
    BricklogTheme {
        ResetFilterConfirmDialog(
            onConfirmation = {},
            onDismiss = {},
        )
    }
}
