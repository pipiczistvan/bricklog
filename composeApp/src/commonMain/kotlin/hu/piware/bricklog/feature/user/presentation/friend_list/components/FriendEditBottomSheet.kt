@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.user.presentation.friend_list.components

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import hu.piware.bricklog.feature.core.presentation.components.BottomSheetHeader
import hu.piware.bricklog.feature.user.domain.model.Friend
import kotlinx.coroutines.launch

@Composable
fun FriendEditBottomSheet(
    friend: Friend? = null,
    onConfirm: (Friend) -> Unit,
    onDelete: (Friend) -> Unit,
    onDismiss: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    var friendName by remember { mutableStateOf(friend?.name ?: "") }
    var friendIdentifier by remember { mutableStateOf(friend?.id ?: "") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        BottomSheetHeader(
            title = "Edit friend",
            sheetState = sheetState,
            onDismiss = onDismiss,
        )

        Text(
            text = "Friend name", // TODO: localize
        )
        TextField(
            value = friendName,
            onValueChange = {
                friendName = it
            },
        )
        Text(
            text = "Friend identifier", // TODO: localize
        )
        TextField(
            value = friendIdentifier,
            onValueChange = {
                friendIdentifier = it
            },
            enabled = friend == null,
        )

        Button(
            onClick = {
                val friend = friend ?: emptyFriend()
                onConfirm(
                    friend.copy(
                        name = friendName,
                        id = friendIdentifier,
                    ),
                )

                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        onDismiss()
                    }
                }
            },
        ) {
            Text("OK")
        }

        if (friend != null) {
            Button(
                onClick = {
                    showDeleteConfirmDialog = true
                },
            ) {
                Text("Delete")
            }
        }
    }

    if (showDeleteConfirmDialog && friend != null) {
        FriendDeleteConfirmDialog(
            onConfirmation = {
                showDeleteConfirmDialog = false
                onDelete(friend)

                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        onDismiss()
                    }
                }
            },
            onDismiss = { showDeleteConfirmDialog = false },
        )
    }
}

private fun emptyFriend() = Friend(
    id = "",
    name = "",
)
