@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_search_collection_filter_sheet_title
import hu.piware.bricklog.feature.collection.domain.model.CollectionDetails
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.presentation.components.CollectionBottomSheetItem
import hu.piware.bricklog.feature.settings.domain.model.DEFAULT_SET_FILTER_PREFERENCES
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
        onSelectionChange = { selection -> onSelectionChange(selection.map { it.collection.id }) },
        onDismiss = onDismiss,
    ) { collection, isSelected ->
        CollectionBottomSheetItem(
            details = collection,
            showRoleAndOwner = showRoleAndOwner,
            isSelected = isSelected,
        )
    }
}
