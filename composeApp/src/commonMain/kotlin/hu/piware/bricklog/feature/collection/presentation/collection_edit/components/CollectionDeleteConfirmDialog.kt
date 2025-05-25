package hu.piware.bricklog.feature.collection.presentation.collection_edit.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.collection_delete_confirm_dialog_button_cancel
import bricklog.composeapp.generated.resources.collection_delete_confirm_dialog_button_confirm
import bricklog.composeapp.generated.resources.collection_delete_confirm_dialog_text
import bricklog.composeapp.generated.resources.collection_delete_confirm_dialog_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun CollectionDeleteConfirmDialog(
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
            Text(stringResource(Res.string.collection_delete_confirm_dialog_title))
        },
        text = {
            Text(stringResource(Res.string.collection_delete_confirm_dialog_text))
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onConfirmation
            ) {
                Text(stringResource(Res.string.collection_delete_confirm_dialog_button_confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(stringResource(Res.string.collection_delete_confirm_dialog_button_cancel))
            }
        }
    )
}
