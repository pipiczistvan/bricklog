@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_search_packaging_type_filter_sheet_title
import hu.piware.bricklog.feature.settings.domain.model.DEFAULT_SET_FILTER_PREFERENCES
import org.jetbrains.compose.resources.stringResource

@Composable
fun PackagingTypeFilterBottomSheet(
    availableOptions: List<String>,
    selected: List<String>,
    onSelectionChange: (List<String>) -> Unit,
    onDismiss: () -> Unit,
) {
    MultiSelectFilterBottomSheet(
        modifier = Modifier.testTag("search_bar:packaging_type_filter_bottom_sheet"),
        title = stringResource(Res.string.feature_set_search_packaging_type_filter_sheet_title),
        availableOptions = availableOptions,
        defaultSelection = DEFAULT_SET_FILTER_PREFERENCES.packagingTypes,
        selectedItems = selected,
        onSelectionChange = onSelectionChange,
        onDismiss = onDismiss,
        skipPartiallyExpanded = true,
    ) { packagingType, selected ->
        Text(
            text = packagingType,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Icon(
            modifier = Modifier.alpha(if (selected) 1f else 0f),
            imageVector = Icons.Default.Check,
            contentDescription = null,
        )
    }
}
