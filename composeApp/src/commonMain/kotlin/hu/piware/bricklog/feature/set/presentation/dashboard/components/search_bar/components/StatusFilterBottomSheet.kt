@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_search_status_filter_sheet_title
import hu.piware.bricklog.feature.set.domain.model.SetStatus
import hu.piware.bricklog.feature.settings.domain.model.DEFAULT_SET_FILTER_PREFERENCES
import org.jetbrains.compose.resources.stringResource

@Composable
fun StatusFilterBottomSheet(
    availableOptions: List<SetStatus>,
    selected: List<SetStatus>,
    onSelectionChange: (List<SetStatus>) -> Unit,
    onDismiss: () -> Unit,
) {
    MultiSelectFilterBottomSheet(
        modifier = Modifier.testTag("search_bar:status_filter_bottom_sheet"),
        title = stringResource(Res.string.feature_set_search_status_filter_sheet_title),
        availableOptions = availableOptions,
        defaultSelection = DEFAULT_SET_FILTER_PREFERENCES.statuses,
        selectedItems = selected,
        onSelectionChange = onSelectionChange,
        onDismiss = onDismiss,
    ) { status, isSelected ->
        Text(
            text = stringResource(status.statusRes),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
