@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.collection.presentation.collection_edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_collection_edit_icon_sheet_title
import hu.piware.bricklog.feature.collection.domain.model.CollectionIcon
import hu.piware.bricklog.feature.core.presentation.components.BottomSheetHeader
import hu.piware.bricklog.feature.core.presentation.components.BottomSheetOption
import hu.piware.bricklog.ui.theme.BricklogTheme
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CollectionIconBottomSheet(
    selectedIcon: CollectionIcon,
    onSelectedIconChange: (CollectionIcon) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        CollectionIconSheetContent(
            selectedIcon = selectedIcon,
            onSelectedIconChange = onSelectedIconChange,
            sheetState = sheetState,
            onDismiss = onDismiss,
        )
    }
}

@Composable
private fun CollectionIconSheetContent(
    selectedIcon: CollectionIcon,
    onSelectedIconChange: (CollectionIcon) -> Unit,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        BottomSheetHeader(
            title = stringResource(Res.string.feature_collection_edit_icon_sheet_title),
            sheetState = sheetState,
            onDismiss = onDismiss,
        )

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.MediumPadding.size),
            columns = GridCells.Adaptive(50.dp),
            contentPadding = PaddingValues(Dimens.MediumPadding.size),
        ) {
            items(CollectionIcon.entries.toTypedArray()) { icon ->
                CollectionIconSheetOption(
                    option = icon,
                    selected = icon == selectedIcon,
                    onClick = {
                        onSelectedIconChange(icon)
                    },
                )
            }
        }
    }
}

@Composable
private fun CollectionIconSheetOption(
    option: CollectionIcon,
    selected: Boolean,
    onClick: () -> Unit,
) {
    BottomSheetOption(
        isSelected = selected,
        onClick = onClick,
    ) {
        Icon(
            imageVector = if (selected) option.filledIcon else option.outlinedIcon,
            contentDescription = null,
        )
    }
}

@Preview
@Composable
private fun CollectionIconSheetContentPreview() {
    BricklogTheme {
        CollectionIconSheetContent(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            selectedIcon = CollectionIcon.PAYMENTS,
            onSelectedIconChange = {},
            sheetState = rememberModalBottomSheetState(),
            onDismiss = {},
        )
    }
}
