@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.core.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.date_range_picker_button_confirm
import bricklog.composeapp.generated.resources.date_range_picker_button_dismiss
import bricklog.composeapp.generated.resources.date_range_picker_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun DateRangePickerModal(
    state: DateRangePickerState = rememberDateRangePickerState(),
    onDateRangeSelected: (Pair<Long, Long>) -> Unit,
    onDismiss: () -> Unit,
) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateRangeSelected(
                        Pair(
                            state.selectedStartDateMillis!!,
                            state.selectedEndDateMillis!!
                        )
                    )
                    onDismiss()
                },
                enabled = state.selectedStartDateMillis != null && state.selectedEndDateMillis != null
            ) {
                Text(stringResource(Res.string.date_range_picker_button_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.date_range_picker_button_dismiss))
            }
        }
    ) {
        DateRangePicker(
            state = state,
            title = {
                Text(stringResource(Res.string.date_range_picker_title))
            },
            showModeToggle = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(16.dp)
        )
    }
}
