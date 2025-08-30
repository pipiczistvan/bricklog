@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.user.presentation.friend_edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_user_friend_edit_form_field_name
import bricklog.composeapp.generated.resources.feature_user_friend_edit_form_field_user_identifier
import bricklog.composeapp.generated.resources.feature_user_friend_edit_title_create
import bricklog.composeapp.generated.resources.feature_user_friend_edit_title_edit
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.presentation.components.ContentColumn
import hu.piware.bricklog.feature.core.presentation.components.LoadingOverlay
import hu.piware.bricklog.feature.core.presentation.observeAsEvents
import hu.piware.bricklog.feature.user.domain.model.Friend
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.feature.user.domain.usecase.ValidateUserIdentifier
import hu.piware.bricklog.feature.user.presentation.friend_edit.components.FriendDeleteConfirmDialog
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FriendEditScreenRoot(
    viewModel: FriendEditViewModel = koinViewModel(),
    onBackClick: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    observeAsEvents(viewModel.eventChannel) { event ->
        when (event) {
            FriendEditEvent.Back -> onBackClick()
        }
    }
    val validateUserIdentifier: ValidateUserIdentifier = koinInject()

    FriendEditScreen(
        modifier = Modifier.testTag("friend_edit_screen"),
        state = state,
        onAction = { action ->
            when (action) {
                FriendEditAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        },
        validateFields = { userId, name ->
            validateUserIdentifier(userId) is Result.Success && name.isNotBlank()
        },
    )
}

@Composable
private fun FriendEditScreen(
    state: FriendEditState,
    onAction: (FriendEditAction) -> Unit,
    validateFields: (UserId, String) -> Boolean,
    modifier: Modifier = Modifier,
) {
    LoadingOverlay(
        isLoading = state.isLoading,
    ) {
        var showDeleteConfirmDialog by remember { mutableStateOf(false) }

        var friendIdentifier by rememberSaveable(state.friendIdentifierArg) {
            mutableStateOf(state.friendIdentifierArg)
        }
        var friendName by rememberSaveable(state.friend) {
            mutableStateOf(
                state.friend?.name ?: "",
            )
        }

        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            if (state.isNew) {
                                stringResource(Res.string.feature_user_friend_edit_title_create)
                            } else {
                                stringResource(Res.string.feature_user_friend_edit_title_edit)
                            },
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { onAction(FriendEditAction.OnBackClick) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null,
                            )
                        }
                    },
                    actions = {
                        if (!state.isNew) {
                            IconButton(
                                onClick = { showDeleteConfirmDialog = true },
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = null,
                                )
                            }
                        }
                        IconButton(
                            onClick = {
                                val friend = state.friend ?: emptyFriend()

                                onAction(
                                    FriendEditAction.OnFriendChange(
                                        friend.copy(
                                            name = friendName,
                                            id = friendIdentifier,
                                        ),
                                    ),
                                )
                            },
                            enabled = validateFields(friendIdentifier, friendName),
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Save,
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
                UserIdentifierField(
                    value = friendIdentifier,
                    onValueChange = { friendIdentifier = it },
                    enabled = state.isNew,
                )

                NameField(
                    value = friendName,
                    onValueChange = { friendName = it },
                )
            }
        }

        if (showDeleteConfirmDialog) {
            FriendDeleteConfirmDialog(
                onConfirmation = {
                    showDeleteConfirmDialog = false
                    onAction(FriendEditAction.OnFriendDelete(state.friend!!))
                },
                onDismiss = {
                    showDeleteConfirmDialog = false
                },
            )
        }
    }
}

@Composable
private fun UserIdentifierField(
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier.padding(vertical = Dimens.SmallPadding.size),
            text = stringResource(Res.string.feature_user_friend_edit_form_field_user_identifier),
            style = MaterialTheme.typography.titleMedium,
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
        )
    }
}

@Composable
private fun NameField(
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier.padding(vertical = Dimens.SmallPadding.size),
            text = stringResource(Res.string.feature_user_friend_edit_form_field_name),
            style = MaterialTheme.typography.titleMedium,
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
        )
    }
}

private fun emptyFriend() =
    Friend(
        name = "",
        id = "",
    )
