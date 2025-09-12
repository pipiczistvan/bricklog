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
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_user_friend_list_title
import hu.piware.bricklog.feature.core.presentation.components.ContentColumn
import hu.piware.bricklog.feature.user.domain.model.Friend
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FriendListScreenRoot(
    viewModel: FriendListViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onFriendEditClick: (Friend?) -> Unit,
    onUserScannerClick: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    FriendListScreen(
        modifier = Modifier.testTag("friend_list_screen"),
        state = state,
        onAction = { action ->
            when (action) {
                FriendListAction.OnBackClick -> onBackClick()
                is FriendListAction.OnFriendEditClick -> onFriendEditClick(action.friend)
                FriendListAction.OnUserScannerClick -> onUserScannerClick()
            }
        },
    )
}

@Composable
private fun FriendListScreen(
    state: FriendListState,
    onAction: (FriendListAction) -> Unit,
    modifier: Modifier = Modifier,
) {
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
                actions = {
                    IconButton(onClick = { onAction(FriendListAction.OnUserScannerClick) }) {
                        Icon(
                            imageVector = Icons.Outlined.QrCodeScanner,
                            contentDescription = null,
                        )
                    }
                    IconButton(onClick = { onAction(FriendListAction.OnFriendEditClick(null)) }) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
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
                FriendItem(
                    friend = friend,
                    onEditClick = { onAction(FriendListAction.OnFriendEditClick(friend)) },
                )
            }
        }
    }
}

@Composable
private fun FriendItem(
    friend: Friend,
    onEditClick: (Friend) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.MediumPadding.size),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(friend.name)
                Text(friend.id)
            }
            IconButton(onClick = { onEditClick(friend) }) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = null,
                )
            }
        }
    }
}
