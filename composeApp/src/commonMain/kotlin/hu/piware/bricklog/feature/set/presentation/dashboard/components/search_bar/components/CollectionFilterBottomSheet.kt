@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.collection_filter_sheet_title
import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.core.presentation.components.BottomSheetHeader
import hu.piware.bricklog.feature.core.presentation.components.BottomSheetOption
import hu.piware.bricklog.feature.settings.domain.model.SetFilterPreferences
import hu.piware.bricklog.ui.theme.Dimens
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun CollectionFilterBottomSheet(
    onShowBottomSheetChanged: (Boolean) -> Unit,
    availableOptions: List<Collection>,
    selected: Set<CollectionId>,
    onSelectionChange: (Set<CollectionId>) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var showResetFilterDialog by remember { mutableStateOf(false) }

    ModalBottomSheet(
        modifier = Modifier.testTag("search_bar:collection_filter_bottom_sheet"),
        onDismissRequest = {
            onShowBottomSheetChanged(false)
        },
        sheetState = sheetState
    ) {
        BottomSheetHeader(
            title = stringResource(Res.string.collection_filter_sheet_title),
            onCloseClick = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        onShowBottomSheetChanged(false)
                    }
                }
            },
            primaryActionIcon = Icons.Default.Replay,
            onPrimaryActionClick = {
                showResetFilterDialog = true
            }
        )

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.MediumPadding.size)
        ) {
            items(availableOptions) { option ->
                CollectionFilterSheetOption(
                    collection = option,
                    selected = selected.contains(option.id),
                    onClick = {
                        if (selected.contains(option.id)) {
                            onSelectionChange(selected - option.id)
                        } else {
                            onSelectionChange(selected + option.id)
                        }
                    }
                )
            }
        }
    }

    if (showResetFilterDialog) {
        ResetFilterConfirmDialog(
            onConfirmation = {
                onSelectionChange(SetFilterPreferences().collectionIds)
                showResetFilterDialog = false
            },
            onDismiss = {
                showResetFilterDialog = false
            }
        )
    }
}

@Composable
private fun CollectionFilterSheetOption(
    collection: Collection,
    selected: Boolean,
    onClick: () -> Unit,
) {
    BottomSheetOption(
        selected = selected, onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.SmallPadding.size),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)
            ) {
                Icon(
                    imageVector = collection.icon.outlinedIcon,
                    contentDescription = null
                )
                Text(
                    text = collection.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                modifier = Modifier.alpha(if (selected) 1f else 0f),
                imageVector = Icons.Default.Check,
                contentDescription = null
            )
        }
    }
}
