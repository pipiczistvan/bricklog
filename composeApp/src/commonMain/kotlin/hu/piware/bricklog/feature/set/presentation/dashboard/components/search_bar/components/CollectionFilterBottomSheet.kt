@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.components

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
import bricklog.composeapp.generated.resources.feature_set_search_collection_filter_sheet_title
import hu.piware.bricklog.feature.collection.domain.model.CollectionDetails
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.settings.domain.model.DEFAULT_SET_FILTER_PREFERENCES
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource

@Composable
fun CollectionFilterBottomSheet(
    availableOptions: List<CollectionDetails>,
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
        ) {
            Icon(
                imageVector = collection.collection.icon.outlinedIcon,
                contentDescription = null,
            )
            Text(
                text = collection.collection.name,
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
