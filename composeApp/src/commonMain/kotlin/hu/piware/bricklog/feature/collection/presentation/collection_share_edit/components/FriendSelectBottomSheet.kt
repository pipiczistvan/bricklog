package hu.piware.bricklog.feature.collection.presentation.collection_share_edit.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import hu.piware.bricklog.feature.core.presentation.components.SingleSelectBottomSheet
import hu.piware.bricklog.feature.user.domain.model.Friend

@Composable
fun FriendSelectBottomSheet(
    availableOptions: List<Friend>,
    selectedItem: Friend? = null,
    onSelectionChange: (Friend) -> Unit,
    onDismiss: () -> Unit,
) {
    SingleSelectBottomSheet(
        title = "Select friend",
        availableOptions = availableOptions,
        selectedItem = selectedItem,
        onSelectionChange = onSelectionChange,
        onDismiss = onDismiss,
    ) { friend, isSelected ->
        Column {
            Text(friend.name)
            Text(
                text = friend.id,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
