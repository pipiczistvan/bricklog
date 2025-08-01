@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.dashboard.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
import hu.piware.bricklog.ui.theme.BricklogTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DeleteUserConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        modifier = Modifier.testTag("dashboard:delete_user_confirmation_dialog"),
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
                    onConfirm()
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

@Preview
@Composable
private fun DeleteUserConfirmationDialogPreview() {
    BricklogTheme {
        DeleteUserConfirmationDialog(
            onDismiss = {},
            onConfirm = {},
        )
    }
}
