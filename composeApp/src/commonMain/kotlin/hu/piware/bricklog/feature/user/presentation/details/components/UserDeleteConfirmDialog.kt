package hu.piware.bricklog.feature.user.presentation.details.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_user_delete_user_data_confirm_btn_cancel
import bricklog.composeapp.generated.resources.feature_user_delete_user_data_confirm_btn_confirm
import bricklog.composeapp.generated.resources.feature_user_delete_user_data_confirm_label
import bricklog.composeapp.generated.resources.feature_user_delete_user_data_confirm_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun UserDeleteConfirmDialog(
    onConfirmation: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        modifier = Modifier.testTag("user_details:delete_user_confirmation_dialog"),
        icon = {
            Icon(
                imageVector = Icons.Outlined.DeleteOutline,
                contentDescription = null,
            )
        },
        title = {
            Text(stringResource(Res.string.feature_user_delete_user_data_confirm_title))
        },
        text = {
            Text(stringResource(Res.string.feature_user_delete_user_data_confirm_label))
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                    onDismiss()
                },
            ) {
                Text(stringResource(Res.string.feature_user_delete_user_data_confirm_btn_confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
            ) {
                Text(stringResource(Res.string.feature_user_delete_user_data_confirm_btn_cancel))
            }
        },
    )
}
