@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.collection.presentation.collection_edit.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.collection_icon_sheet_title
import hu.piware.bricklog.feature.collection.domain.model.CollectionIcon
import hu.piware.bricklog.feature.core.presentation.components.BottomSheetHeader
import hu.piware.bricklog.feature.core.presentation.components.BottomSheetOption
import hu.piware.bricklog.ui.theme.Dimens
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun CollectionIconBottomSheet(
    showBottomSheet: Boolean,
    onShowBottomSheetChanged: (Boolean) -> Unit,
    selectedIcon: CollectionIcon,
    onIconClick: (CollectionIcon) -> Unit,
) {

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                onShowBottomSheetChanged(false)
            },
            sheetState = sheetState
        ) {
            BottomSheetHeader(
                title = stringResource(Res.string.collection_icon_sheet_title),
                onCloseClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onShowBottomSheetChanged(false)
                        }
                    }
                }
            )

            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.MediumPadding.size),
                columns = GridCells.Adaptive(50.dp),
                contentPadding = PaddingValues(Dimens.MediumPadding.size)
            ) {
                items(CollectionIcon.entries.toTypedArray()) { icon ->
                    CollectionIconSheetOption(
                        option = icon,
                        selected = icon == selectedIcon,
                        onClick = {
                            onIconClick(icon)
                        }
                    )
                }
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
        selected = selected,
        onClick = onClick
    ) {
        Icon(
            imageVector = if (selected) option.filledIcon else option.outlinedIcon,
            contentDescription = null
        )
    }
}
