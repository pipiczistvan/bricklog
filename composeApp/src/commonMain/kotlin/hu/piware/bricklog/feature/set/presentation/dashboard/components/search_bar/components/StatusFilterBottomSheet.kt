@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextOverflow
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_search_status_filter_sheet_title
import hu.piware.bricklog.feature.core.presentation.components.BottomSheetHeader
import hu.piware.bricklog.feature.core.presentation.components.BottomSheetOption
import hu.piware.bricklog.feature.set.domain.model.SetStatus
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun StatusFilterBottomSheet(
    availableOptions: List<SetStatus>,
    selected: Set<SetStatus>,
    onSelectionChange: (Set<SetStatus>) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        modifier = Modifier.testTag("search_bar:status_filter_bottom_sheet"),
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        StatusFilterSheetContent(
            availableOptions = availableOptions,
            selected = selected,
            onSelectionChange = onSelectionChange,
            sheetState = sheetState,
            onDismiss = onDismiss
        )
    }
}

@Composable
private fun StatusFilterSheetContent(
    availableOptions: List<SetStatus>,
    selected: Set<SetStatus>,
    onSelectionChange: (Set<SetStatus>) -> Unit,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        BottomSheetHeader(
            title = stringResource(Res.string.feature_set_search_status_filter_sheet_title),
            sheetState = sheetState,
            onDismiss = onDismiss
        )

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.MediumPadding.size)
        ) {
            items(availableOptions) { option ->
                StatusFilterSheetOption(
                    option = option,
                    selected = selected.contains(option),
                    onClick = {
                        if (selected.contains(option)) {
                            onSelectionChange(selected - option)
                        } else {
                            onSelectionChange(selected + option)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun StatusFilterSheetOption(
    option: SetStatus,
    selected: Boolean,
    onClick: () -> Unit,
) {
    BottomSheetOption(
        selected = selected,
        onClick = onClick
    ) {
        Spacer(modifier = Modifier.width(Dimens.MediumPadding.size))
        Text(
            text = stringResource(option.statusRes),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun StatusFilterSheetContentPreview() {
    StatusFilterSheetContent(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        availableOptions = SetStatus.entries,
        selected = emptySet(),
        onSelectionChange = {},
        sheetState = rememberModalBottomSheetState(),
        onDismiss = {}
    )
}
