@file:OptIn(ExperimentalMaterial3Api::class, InternalAPI::class)

package hu.piware.bricklog.feature.collection.presentation.collection_share_edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_collection_share_edit_form_field_user_identifier_supporting_text_1
import bricklog.composeapp.generated.resources.feature_collection_share_edit_form_field_user_identifier_supporting_text_2
import bricklog.composeapp.generated.resources.feature_collection_share_edit_form_field_user_identifier_supporting_text_3
import bricklog.composeapp.generated.resources.feature_collection_share_edit_form_field_user_identifier_title
import bricklog.composeapp.generated.resources.feature_collection_share_edit_form_field_write_access_supporting_text
import bricklog.composeapp.generated.resources.feature_collection_share_edit_form_field_write_access_title
import bricklog.composeapp.generated.resources.feature_collection_share_edit_title
import hu.piware.bricklog.feature.collection.domain.model.SharePermissions
import hu.piware.bricklog.feature.collection.domain.model.UserCollectionShare
import hu.piware.bricklog.feature.collection.presentation.collection_share_edit.components.CollectionShareDeleteConfirmDialog
import hu.piware.bricklog.feature.collection.presentation.collection_share_edit.components.FriendSelectBottomSheet
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.presentation.components.ContentColumn
import hu.piware.bricklog.feature.core.presentation.components.LoadingOverlay
import hu.piware.bricklog.feature.core.presentation.components.SupportingRow
import hu.piware.bricklog.feature.core.presentation.observeAsEvents
import hu.piware.bricklog.feature.user.domain.model.Friend
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.feature.user.domain.usecase.ValidateUserIdentifier
import hu.piware.bricklog.ui.theme.BricklogTheme
import hu.piware.bricklog.ui.theme.Dimens
import io.ktor.utils.io.InternalAPI
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CollectionShareEditScreenRoot(
    viewModel: CollectionShareEditViewModel = koinViewModel(),
    onBackClick: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    observeAsEvents(viewModel.eventChannel) { event ->
        when (event) {
            CollectionShareEditEvent.Back -> onBackClick()
        }
    }
    val validateUserIdentifier: ValidateUserIdentifier = koinInject()

    CollectionShareEditScreen(
        modifier = Modifier.testTag("collection_share_edit_screen"),
        state = state,
        onAction = { action ->
            when (action) {
                CollectionShareEditAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        },
        validateFields = { userId ->
            validateUserIdentifier(userId) is Result.Success
        },
    )
}

@Composable
private fun CollectionShareEditScreen(
    state: CollectionShareEditState,
    onAction: (CollectionShareEditAction) -> Unit,
    validateFields: (UserId) -> Boolean,
    modifier: Modifier = Modifier,
) {
    LoadingOverlay(
        isLoading = state.isLoading,
    ) {
        var showDeleteConfirmDialog by remember { mutableStateOf(false) }

        var userIdentifier by rememberSaveable(state.shareUserIdArgument) {
            mutableStateOf(state.shareUserIdArgument)
        }
        var hasWriteAccess by rememberSaveable(state.share) {
            mutableStateOf(
                state.share?.permissions?.canWrite ?: false,
            )
        }

        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    title = {
                        Text(stringResource(Res.string.feature_collection_share_edit_title))
                    },
                    navigationIcon = {
                        IconButton(onClick = { onAction(CollectionShareEditAction.OnBackClick) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null,
                            )
                        }
                    },
                    actions = {
                        if (!state.isNew) {
                            IconButton(onClick = { showDeleteConfirmDialog = true }) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = null,
                                )
                            }
                        }
                        IconButton(
                            onClick = {
                                val share = state.share ?: emptyUserCollectionShare()

                                onAction(
                                    CollectionShareEditAction.OnShareChange(
                                        share.copy(
                                            userId = userIdentifier,
                                            permissions = share.permissions.copy(
                                                canWrite = hasWriteAccess,
                                            ),
                                        ),
                                    ),
                                )
                            },
                            enabled = validateFields(userIdentifier),
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
                    value = userIdentifier,
                    onValueChange = { userIdentifier = it },
                    friends = state.friends,
                    enabled = state.isNew,
                )

                WriteAccessCheckbox(
                    checked = hasWriteAccess,
                    onCheckedChange = { hasWriteAccess = it },
                )
            }
        }

        if (showDeleteConfirmDialog) {
            CollectionShareDeleteConfirmDialog(
                onConfirmation = {
                    showDeleteConfirmDialog = false
                    onAction(CollectionShareEditAction.OnShareDelete(state.share!!))
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
    friends: List<Friend>,
    enabled: Boolean,
) {
    var showFriendSelectBottomSheet by remember { mutableStateOf(false) }
    val selectedFriend = remember(value, friends) { friends.find { it.id == value } }

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier.padding(vertical = Dimens.SmallPadding.size),
            text = stringResource(Res.string.feature_collection_share_edit_form_field_user_identifier_title),
            style = MaterialTheme.typography.titleMedium,
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            label = {
                if (selectedFriend != null) {
                    Text(selectedFriend.name)
                }
            },
        )
        SupportingRow(
            enabled = enabled,
        ) {
            Text(
                text = buildAnnotatedString {
                    append(stringResource(Res.string.feature_collection_share_edit_form_field_user_identifier_supporting_text_1))
                    withLink(
                        LinkAnnotation.Clickable(
                            tag = "CLICK",
                            styles = TextLinkStyles(style = SpanStyle(textDecoration = TextDecoration.Underline)),
                            linkInteractionListener = {
                                if (enabled) {
                                    showFriendSelectBottomSheet = true
                                }
                            },
                        ),
                    ) {
                        append(stringResource(Res.string.feature_collection_share_edit_form_field_user_identifier_supporting_text_2))
                    }
                    append(stringResource(Res.string.feature_collection_share_edit_form_field_user_identifier_supporting_text_3))
                },
            )
        }
    }

    if (showFriendSelectBottomSheet) {
        FriendSelectBottomSheet(
            availableOptions = friends,
            selectedItem = selectedFriend,
            onSelectionChange = {
                onValueChange(it.id)
                showFriendSelectBottomSheet = false
            },
            onDismiss = {
                showFriendSelectBottomSheet = false
            },
        )
    }
}

@Composable
private fun WriteAccessCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.SmallPadding.size),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.feature_collection_share_edit_form_field_write_access_title),
                style = MaterialTheme.typography.titleMedium,
            )
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
            )
        }
        SupportingRow {
            Text(stringResource(Res.string.feature_collection_share_edit_form_field_write_access_supporting_text))
        }
    }
}

private fun emptyUserCollectionShare() =
    UserCollectionShare(
        userId = "",
        permissions = SharePermissions(canWrite = false),
    )

@Preview
@Composable
private fun CollectionShareEditScreenPreview() {
    BricklogTheme {
        CollectionShareEditScreen(
            state = CollectionShareEditState(
                share = emptyUserCollectionShare(),
            ),
            onAction = {},
            validateFields = { true },
        )
    }
}
