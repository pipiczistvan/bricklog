@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_user_logout_confirm_bottom_sheet_btn_cancel
import bricklog.composeapp.generated.resources.feature_user_logout_confirm_bottom_sheet_btn_confirm
import bricklog.composeapp.generated.resources.feature_user_logout_confirm_bottom_sheet_label
import bricklog.composeapp.generated.resources.feature_user_logout_confirm_bottom_sheet_title
import hu.piware.bricklog.feature.core.presentation.components.BottomSheetButton
import hu.piware.bricklog.feature.core.presentation.components.BottomSheetHeader
import hu.piware.bricklog.ui.theme.BricklogTheme
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LogoutConfirmationBottomSheet(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        modifier = Modifier.testTag("dashboard:logout_confirmation_bottom_sheet"),
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        LogoutConfirmationContent(
            sheetState = sheetState,
            onDismiss = onDismiss,
            onConfirm = onConfirm
        )
    }
}

@Composable
private fun LogoutConfirmationContent(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        BottomSheetHeader(
            title = stringResource(Res.string.feature_user_logout_confirm_bottom_sheet_title),
            sheetState = sheetState,
            onDismiss = onDismiss
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.MediumPadding.size)
        ) {
            Text(text = stringResource(Res.string.feature_user_logout_confirm_bottom_sheet_label))

            BottomSheetButton(
                onClick = onDismiss,
                title = stringResource(Res.string.feature_user_logout_confirm_bottom_sheet_btn_cancel),
                icon = Icons.Outlined.Close
            )

            BottomSheetButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                },
                title = stringResource(Res.string.feature_user_logout_confirm_bottom_sheet_btn_confirm),
                icon = Icons.AutoMirrored.Outlined.Logout,
                color = MaterialTheme.colorScheme.error.copy(0.6f)
            )
        }
    }
}

@Preview
@Composable
private fun LogoutConfirmationContentPreview() {
    BricklogTheme {
        LogoutConfirmationContent(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            sheetState = rememberModalBottomSheetState(),
            onDismiss = {},
            onConfirm = {}
        )
    }
}
