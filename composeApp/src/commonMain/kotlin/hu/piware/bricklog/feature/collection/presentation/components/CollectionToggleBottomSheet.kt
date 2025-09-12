package hu.piware.bricklog.feature.collection.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_collection_toggle_bottom_sheet_title
import hu.piware.bricklog.feature.collection.domain.model.CollectionDetails
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.model.isEditable
import hu.piware.bricklog.feature.core.presentation.components.MultiSelectBottomSheet
import org.jetbrains.compose.resources.stringResource

@Composable
fun CollectionToggleBottomSheet(
    title: String = stringResource(Res.string.feature_collection_toggle_bottom_sheet_title),
    availableOptions: List<CollectionDetails>,
    selectedItems: List<CollectionId>,
    onToggleCollection: (CollectionId) -> Unit,
    onDismiss: () -> Unit,
    showRoleAndOwner: Boolean,
    modifier: Modifier = Modifier,
) {
    MultiSelectBottomSheet(
        modifier = modifier,
        title = title,
        availableOptions = availableOptions,
        selectedItems = availableOptions.filter { it.collection.id in selectedItems },
        onSelectionChange = { collections ->
            val newSelected = collections.map { it.collection.id }
            val changedSelection =
                ((selectedItems - newSelected) + (newSelected - selectedItems)).distinct()

            changedSelection.forEach { collectionId ->
                onToggleCollection(collectionId)
            }
        },
        isItemEnabled = { it.isEditable },
        onDismiss = onDismiss,
    ) { collection, isSelected ->
        CollectionItem(
            modifier = Modifier.fillMaxWidth(),
            details = collection,
            isSelected = isSelected,
            showRoleAndOwner = showRoleAndOwner,
        )
    }
}
