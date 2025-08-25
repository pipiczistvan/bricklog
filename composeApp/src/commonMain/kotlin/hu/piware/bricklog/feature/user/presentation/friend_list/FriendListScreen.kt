@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.user.presentation.friend_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_user_friend_list_title
import hu.piware.bricklog.feature.core.presentation.components.ContentColumn
import hu.piware.bricklog.feature.user.domain.model.Friend
import hu.piware.bricklog.feature.user.presentation.friend_list.components.FriendEditBottomSheet
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FriendListScreenRoot(
    viewModel: FriendListViewModel = koinViewModel(),
    onBackClick: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    FriendListScreen(
        modifier = Modifier.testTag("friend_list_screen"),
        state = state,
        onAction = { action ->
            when (action) {
                FriendListAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        },
    )
}

@Composable
private fun FriendListScreen(
    state: FriendListState,
    onAction: (FriendListAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showEditFriendBottomSheet by remember { mutableStateOf(false) }
    var friendToEdit: Friend? by remember { mutableStateOf(null) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(Res.string.feature_user_friend_list_title)) },
                navigationIcon = {
                    IconButton(onClick = { onAction(FriendListAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
    ) { padding ->
        ContentColumn(
            modifier = Modifier
                .padding(horizontal = Dimens.MediumPadding.size)
                .fillMaxSize(),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding(),
            ),
            verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
        ) {
            state.friends.forEach { friend ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(friend.name)
                        Text(friend.id)
                    }
                    IconButton(onClick = {
                        friendToEdit = friend
                        showEditFriendBottomSheet = true
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = null,
                        )
                    }
                }
            }

            Button(
                onClick = {
                    friendToEdit = null
                    showEditFriendBottomSheet = true
                },
            ) {
                Text("Add new friend")
            }
        }
    }

    if (showEditFriendBottomSheet) {
        FriendEditBottomSheet(
            friend = friendToEdit,
            onConfirm = {
                onAction(FriendListAction.OnFriendChange(it))
            },
            onDelete = {
                onAction(FriendListAction.OnFriendDelete(it))
            },
            onDismiss = {
                showEditFriendBottomSheet = false
                friendToEdit = null
            },
        )
    }
}
