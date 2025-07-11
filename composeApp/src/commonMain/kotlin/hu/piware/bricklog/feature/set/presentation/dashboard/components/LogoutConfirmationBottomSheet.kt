@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import bricklog.composeapp.generated.resources.logout_confirm_bottom_sheet_no
import bricklog.composeapp.generated.resources.logout_confirm_bottom_sheet_title
import bricklog.composeapp.generated.resources.logout_confirm_bottom_sheet_yes
import hu.piware.bricklog.feature.core.presentation.components.BottomSheetHeader
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource

@Composable
fun LogoutConfirmationBottomSheet(
    onDismiss: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        modifier = Modifier.testTag("dashboard:logout_confirmation_bottom_sheet"),
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        BottomSheetHeader(
            title = stringResource(Res.string.logout_confirm_bottom_sheet_title)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.MediumPadding.size)
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onDismiss
            ) {
                Text(text = stringResource(Res.string.logout_confirm_bottom_sheet_no))
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onLogoutClick()
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(text = stringResource(Res.string.logout_confirm_bottom_sheet_yes))
            }
        }
    }
}
