package hu.piware.bricklog.feature.collection.presentation.collection_share_edit.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_collection_share_edit_friend_select_bottom_sheet_title
import hu.piware.bricklog.feature.core.presentation.components.SingleSelectBottomSheet
import hu.piware.bricklog.feature.user.domain.model.Friend
import org.jetbrains.compose.resources.stringResource

@Composable
fun FriendSelectBottomSheet(
    availableOptions: List<Friend>,
    selectedItem: Friend? = null,
    onSelectionChange: (Friend) -> Unit,
    onDismiss: () -> Unit,
) {
    SingleSelectBottomSheet(
        title = stringResource(Res.string.feature_collection_share_edit_friend_select_bottom_sheet_title),
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
