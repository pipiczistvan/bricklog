@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.collection.presentation.collection_edit.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import hu.piware.bricklog.feature.collection.domain.model.CollectionShare
import hu.piware.bricklog.feature.collection.presentation.collection_edit.model.UserCollectionShare
import hu.piware.bricklog.feature.core.presentation.components.BottomSheetHeader
import kotlinx.coroutines.launch

@Composable
fun EditShareBottomSheet(
    collectionShare: UserCollectionShare?,
    onConfirm: (UserCollectionShare) -> Unit,
    onDelete: (UserCollectionShare) -> Unit,
    onDismiss: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var userIdentifier by remember { mutableStateOf(collectionShare?.userId ?: "") }
    var hasWriteAccess by remember { mutableStateOf(collectionShare?.share?.canWrite ?: false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        BottomSheetHeader(
            title = "Collection share",
            sheetState = sheetState,
            onDismiss = onDismiss,
        )

        Text(
            text = "User identifier", // TODO: localize
        )
        TextField(
            value = userIdentifier,
            onValueChange = {
                userIdentifier = it
            },
            enabled = collectionShare == null,
        )
        Row {
            Text(
                text = "Write access",
            )
            Switch(
                checked = hasWriteAccess,
                onCheckedChange = {
                    hasWriteAccess = it
                },
            )
        }
        Button(
            onClick = {
                val share = collectionShare ?: emptyUserCollectionShare()

                onConfirm(
                    share.copy(
                        userId = userIdentifier,
                        share = share.share.copy(
                            canWrite = hasWriteAccess,
                        ),
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
        if (collectionShare != null) {
            Button(
                onClick = {
                    onDelete(collectionShare)
                },
            ) {
                Text("Delete")
            }
        }
    }
}

private fun emptyUserCollectionShare() =
    UserCollectionShare(
        userId = "",
        share = CollectionShare(canWrite = false),
    )
