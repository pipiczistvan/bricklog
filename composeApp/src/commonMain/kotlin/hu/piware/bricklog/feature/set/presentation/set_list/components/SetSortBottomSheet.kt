@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.set_list.components

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
import androidx.compose.ui.text.style.TextOverflow
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_search_sort_title
import hu.piware.bricklog.feature.core.presentation.components.BottomSheetHeader
import hu.piware.bricklog.feature.core.presentation.components.BottomSheetOption
import hu.piware.bricklog.feature.set.domain.model.SetSortOption
import hu.piware.bricklog.ui.theme.BricklogTheme
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetSortBottomSheet(
    selectedOption: SetSortOption,
    onOptionClick: (SetSortOption) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        SetSortSheetContent(
            selectedOption = selectedOption,
            onOptionClick = onOptionClick,
            sheetState = sheetState,
            onDismiss = onDismiss
        )
    }
}

@Composable
private fun SetSortSheetContent(
    selectedOption: SetSortOption,
    onOptionClick: (SetSortOption) -> Unit,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        BottomSheetHeader(
            title = stringResource(Res.string.feature_set_search_sort_title),
            sheetState = sheetState,
            onDismiss = onDismiss
        )

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.MediumPadding.size)
        ) {
            items(SetSortOption.entries.toTypedArray()) { option ->
                SetSortSheetOption(
                    option = option,
                    selected = option == selectedOption,
                    onClick = {
                        onOptionClick(option)
                    }
                )
            }
        }
    }
}

@Composable
private fun SetSortSheetOption(
    option: SetSortOption,
    selected: Boolean,
    onClick: () -> Unit,
) {
    BottomSheetOption(
        selected = selected,
        onClick = onClick
    ) {
        Spacer(modifier = Modifier.width(Dimens.MediumPadding.size))
        Text(
            text = stringResource(option.titleRes),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun SetSortSheetContentPreview() {
    BricklogTheme {
        SetSortSheetContent(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            selectedOption = SetSortOption.LAUNCH_DATE_ASCENDING,
            onOptionClick = {},
            sheetState = rememberModalBottomSheetState(),
            onDismiss = {}
        )
    }
}
