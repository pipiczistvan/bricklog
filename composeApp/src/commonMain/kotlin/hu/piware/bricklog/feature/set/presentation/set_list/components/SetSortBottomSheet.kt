package hu.piware.bricklog.feature.set.presentation.set_list.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_search_sort_title
import hu.piware.bricklog.feature.core.presentation.components.SingleSelectBottomSheet
import hu.piware.bricklog.feature.set.domain.model.SetSortOption
import org.jetbrains.compose.resources.stringResource

@Composable
fun SetSortBottomSheet(
    selectedOption: SetSortOption,
    onOptionClick: (SetSortOption) -> Unit,
    onDismiss: () -> Unit,
) {
    SingleSelectBottomSheet(
        title = stringResource(Res.string.feature_set_search_sort_title),
        availableOptions = SetSortOption.entries,
        selectedItem = selectedOption,
        onSelectionChange = onOptionClick,
        onDismiss = onDismiss,
    ) { option, isSelected ->
        Text(
            text = stringResource(option.titleRes),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
