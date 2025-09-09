@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_search_collection_filter_sheet_title
import hu.piware.bricklog.feature.collection.domain.model.CollectionDetails
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.model.containerColor
import hu.piware.bricklog.feature.collection.domain.model.textColor
import hu.piware.bricklog.feature.settings.domain.model.DEFAULT_SET_FILTER_PREFERENCES
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource

@Composable
fun CollectionFilterBottomSheet(
    availableOptions: List<CollectionDetails>,
    showRoleAndOwner: Boolean,
    selected: List<CollectionId>,
    onSelectionChange: (List<CollectionId>) -> Unit,
    onDismiss: () -> Unit,
) {
    MultiSelectFilterBottomSheet(
        modifier = Modifier.testTag("search_bar:collection_filter_bottom_sheet"),
        title = stringResource(Res.string.feature_set_search_collection_filter_sheet_title),
        availableOptions = availableOptions,
        defaultSelection = availableOptions.filter { it.collection.id in DEFAULT_SET_FILTER_PREFERENCES.collectionIds },
        selectedItems = availableOptions.filter { it.collection.id in selected },
        onSelectionChange = { selection ->
            onSelectionChange(selection.map { it.collection.id })
        },
        onDismiss = onDismiss,
    ) { collection, isSelected ->
        CollectionFilterBottomSheetItem(
            details = collection,
            showRoleAndOwner = showRoleAndOwner,
            isSelected = isSelected,
        )
    }
}

@Composable
private fun CollectionFilterBottomSheetItem(
    details: CollectionDetails,
    showRoleAndOwner: Boolean,
    isSelected: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier
                    .padding(end = Dimens.SmallPadding.size),
                imageVector = if (isSelected) {
                    details.collection.icon.filledIcon
                } else {
                    details.collection.icon.outlinedIcon
                },
                contentDescription = null,
            )
            Column {
                Text(
                    text = details.collection.name,
                )
                if (showRoleAndOwner) {
                    Text(
                        text = details.collection.owner,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        }
        if (showRoleAndOwner) {
            Badge(
                containerColor = details.role.containerColor,
                contentColor = details.role.textColor,
            ) {
                Text(stringResource(details.role.stringRes))
            }
        }
    }
}
