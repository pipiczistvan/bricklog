@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.set_detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_detail_collections_sheet_title
import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.model.toCollections
import hu.piware.bricklog.feature.core.presentation.components.MultiSelectBottomSheet
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource

@Composable
fun SetCollectionBottomSheet(
    availableOptions: List<Collection>,
    selectedItems: List<CollectionId>,
    onToggleCollection: (CollectionId) -> Unit,
    onDismiss: () -> Unit,
) {
    MultiSelectBottomSheet(
        modifier = Modifier.testTag("set_details:set_collection_bottom_sheet"),
        title = stringResource(Res.string.feature_set_detail_collections_sheet_title),
        availableOptions = availableOptions,
        selectedItems = selectedItems.toCollections(availableOptions),
        onSelectionChange = { collections ->
            val newSelected = collections.map { it.id }
            val changedSelection =
                ((selectedItems - newSelected) + (newSelected - selectedItems)).distinct()

            changedSelection.forEach { collectionId ->
                onToggleCollection(collectionId)
            }
        },
        onDismiss = onDismiss,
    ) { collection, isSelected ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
        ) {
            Icon(
                imageVector = collection.icon.outlinedIcon,
                contentDescription = null,
            )
            Text(
                text = collection.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Icon(
            modifier = Modifier.alpha(if (isSelected) 1f else 0f),
            imageVector = Icons.Default.Check,
            contentDescription = null,
        )
    }
}
