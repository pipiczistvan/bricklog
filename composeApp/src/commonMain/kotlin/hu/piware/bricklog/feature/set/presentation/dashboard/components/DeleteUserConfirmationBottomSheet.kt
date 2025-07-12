@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.delete_user_data_confirm_description
import bricklog.composeapp.generated.resources.delete_user_data_confirm_no
import bricklog.composeapp.generated.resources.delete_user_data_confirm_title
import bricklog.composeapp.generated.resources.delete_user_data_confirm_yes
import hu.piware.bricklog.feature.core.presentation.components.BottomSheetButton
import hu.piware.bricklog.feature.core.presentation.components.BottomSheetHeader
import hu.piware.bricklog.ui.theme.BricklogTheme
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DeleteUserConfirmationBottomSheet(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        modifier = Modifier.testTag("dashboard:delete_user_confirmation_bottom_sheet"),
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        DeleteUserConfirmationContent(
            onDismiss = onDismiss,
            onConfirm = onConfirm
        )
    }
}

@Composable
private fun DeleteUserConfirmationContent(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        BottomSheetHeader(
            title = stringResource(Res.string.delete_user_data_confirm_title)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.MediumPadding.size)
        ) {
            Text(text = stringResource(Res.string.delete_user_data_confirm_description))

            BottomSheetButton(
                onClick = onDismiss,
                title = stringResource(Res.string.delete_user_data_confirm_no),
                icon = Icons.Outlined.Close
            )

            BottomSheetButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                },
                title = stringResource(Res.string.delete_user_data_confirm_yes),
                icon = Icons.Outlined.DeleteOutline,
                color = MaterialTheme.colorScheme.error.copy(0.6f)
            )
        }
    }
}

@Preview
@Composable
private fun DeleteUserConfirmationContentPreview() {
    BricklogTheme {
        DeleteUserConfirmationContent(
            onDismiss = {},
            onConfirm = {}
        )
    }
}
