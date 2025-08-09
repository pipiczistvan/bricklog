package hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Replay
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import hu.piware.bricklog.feature.core.presentation.components.MultiSelectBottomSheet

@Composable
fun <T> MultiSelectFilterBottomSheet(
    title: String,
    availableOptions: List<T>,
    defaultSelection: List<T>,
    selectedItems: List<T>,
    onSelectionChange: (List<T>) -> Unit,
    onDismiss: () -> Unit,
    skipPartiallyExpanded: Boolean = false,
    modifier: Modifier = Modifier,
    itemContent: @Composable RowScope.(T, Boolean) -> Unit,
) {
    var showResetFilterDialog by remember { mutableStateOf(false) }

    MultiSelectBottomSheet(
        modifier = modifier,
        title = title,
        availableOptions = availableOptions,
        selectedItems = selectedItems,
        onSelectionChange = onSelectionChange,
        onDismiss = onDismiss,
        skipPartiallyExpanded = skipPartiallyExpanded,
        headerPrimaryActionIcon = Icons.Default.Replay,
        onHeaderPrimaryActionClick = { showResetFilterDialog = true },
        itemContent = itemContent,
    )

    if (showResetFilterDialog) {
        ResetFilterConfirmDialog(
            onConfirmation = {
                onSelectionChange(defaultSelection)
                showResetFilterDialog = false
            },
            onDismiss = {
                showResetFilterDialog = false
            },
        )
    }
}
