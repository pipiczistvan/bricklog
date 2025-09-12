@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.collection.presentation.collection_edit

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_collection_edit_form_field_name_placeholder
import bricklog.composeapp.generated.resources.feature_collection_edit_form_field_name_title
import bricklog.composeapp.generated.resources.feature_collection_edit_shares_empty
import bricklog.composeapp.generated.resources.feature_collection_edit_shares_title
import bricklog.composeapp.generated.resources.feature_collection_edit_title_create
import bricklog.composeapp.generated.resources.feature_collection_edit_title_modify
import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionIcon
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.model.CollectionType
import hu.piware.bricklog.feature.collection.domain.model.SharePermissions
import hu.piware.bricklog.feature.collection.domain.usecase.ValidateCollectionName
import hu.piware.bricklog.feature.collection.domain.util.DefaultCollections
import hu.piware.bricklog.feature.collection.presentation.collection_edit.components.CollectionDeleteConfirmDialog
import hu.piware.bricklog.feature.collection.presentation.collection_edit.components.CollectionIconBottomSheet
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.presentation.components.ActionRow
import hu.piware.bricklog.feature.core.presentation.components.ContentColumn
import hu.piware.bricklog.feature.core.presentation.components.LoadingOverlay
import hu.piware.bricklog.feature.core.presentation.components.SupportingRow
import hu.piware.bricklog.feature.core.presentation.observeAsEvents
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.feature.user.domain.model.isAuthenticated
import hu.piware.bricklog.ui.theme.BricklogTheme
import hu.piware.bricklog.ui.theme.Dimens
import hu.piware.bricklog.ui.theme.Shapes
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CollectionEditScreenRoot(
    viewModel: CollectionEditViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onCollectionDeleted: () -> Unit,
    onCollectionShareEditClick: (CollectionId, UserId?) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    observeAsEvents(viewModel.eventChannel) { event ->
        when (event) {
            CollectionEditEvent.Back -> onBackClick()
            CollectionEditEvent.Deleted -> onCollectionDeleted()
        }
    }
    val validateCollectionName: ValidateCollectionName = koinInject()

    CollectionEditScreen(
        modifier = Modifier.testTag("collection_edit_screen"),
        state = state,
        onAction = { action ->
            when (action) {
                is CollectionEditAction.OnBackClick -> onBackClick()
                is CollectionEditAction.OnShareClick -> onCollectionShareEditClick(
                    action.collectionId,
                    action.userId,
                )

                else -> Unit
            }
            viewModel.onAction(action)
        },
        validateFields = { collectionName ->
            validateCollectionName(collectionName) is Result.Success
        },
    )
}

@Composable
private fun CollectionEditScreen(
    state: CollectionEditState,
    onAction: (CollectionEditAction) -> Unit,
    validateFields: (String) -> Boolean,
    modifier: Modifier = Modifier,
) {
    LoadingOverlay(
        isLoading = state.isLoading,
    ) {
        var showDeleteConfirmDialog by remember { mutableStateOf(false) }

        var collectionName by rememberSaveable(state.collection) {
            mutableStateOf(state.collection?.name ?: "")
        }
        var collectionIcon by rememberSaveable(state.collection) {
            mutableStateOf(state.collection?.icon ?: CollectionIcon.STAR)
        }

        Scaffold(
            modifier = modifier
                .imePadding(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            if (state.collection == null) {
                                stringResource(Res.string.feature_collection_edit_title_create)
                            } else {
                                stringResource(Res.string.feature_collection_edit_title_modify)
                            },
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { onAction(CollectionEditAction.OnBackClick) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null,
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                val collection =
                                    state.collection ?: emptyCollection(state.currentUser.uid)
                                onAction(
                                    CollectionEditAction.OnCollectionChange(
                                        collection.copy(
                                            name = collectionName,
                                            icon = collectionIcon,
                                        ),
                                    ),
                                )
                            },
                            enabled = validateFields(collectionName),
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Save,
                                contentDescription = null,
                            )
                        }

                        if (state.isOwner && DefaultCollections.entries.none { it.type == state.collection?.type }) {
                            IconButton(onClick = { showDeleteConfirmDialog = true }) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = null,
                                )
                            }
                        }
                    },
                )
            },
        ) { padding ->
            ContentColumn(
                modifier = Modifier
                    .padding(horizontal = Dimens.MediumPadding.size)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
                contentPadding = PaddingValues(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding(),
                ),
            ) {
                CollectionNameField(
                    value = collectionName,
                    onValueChange = { collectionName = it },
                    collectionIcon = collectionIcon,
                    onCollectionIconChange = { collectionIcon = it },
                )

                state.collection?.let { collection ->
                    if (state.currentUser.isAuthenticated && collection.owner == state.currentUser.uid) {
                        SharesTable(
                            shares = collection.shares,
                            onShareClick = {
                                onAction(CollectionEditAction.OnShareClick(collection.id, it))
                            },
                            onNewShareClick = {
                                onAction(CollectionEditAction.OnShareClick(collection.id, null))
                            },
                        )
                    }
                }
            }
        }

        if (showDeleteConfirmDialog) {
            CollectionDeleteConfirmDialog(
                onConfirmation = {
                    showDeleteConfirmDialog = false
                    onAction(CollectionEditAction.OnCollectionDelete(state.collection!!))
                },
                onDismiss = {
                    showDeleteConfirmDialog = false
                },
            )
        }
    }
}

@Composable
private fun CollectionNameField(
    value: String,
    onValueChange: (String) -> Unit,
    collectionIcon: CollectionIcon,
    onCollectionIconChange: (CollectionIcon) -> Unit,
) {
    var showIconBottomSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier.padding(vertical = Dimens.SmallPadding.size),
            text = stringResource(Res.string.feature_collection_edit_form_field_name_title),
            style = MaterialTheme.typography.titleMedium,
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(stringResource(Res.string.feature_collection_edit_form_field_name_placeholder)) },
            singleLine = true,
            leadingIcon = {
                IconButton(
                    onClick = { showIconBottomSheet = true },
                ) {
                    Icon(
                        imageVector = collectionIcon.outlinedIcon,
                        contentDescription = null,
                    )
                }
            },
        )
        SupportingRow {
            Text(
                text = "${value.length}/${ValidateCollectionName.MAX_LENGTH}",
            )
        }
    }

    if (showIconBottomSheet) {
        CollectionIconBottomSheet(
            selectedIcon = collectionIcon,
            onSelectedIconChange = onCollectionIconChange,
            onDismiss = {
                showIconBottomSheet = false
            },
        )
    }
}

@Composable
private fun SharesTable(
    shares: Map<UserId, SharePermissions>,
    onShareClick: (UserId) -> Unit,
    onNewShareClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .border(2.dp, DividerDefaults.color, Shapes.medium)
            .clip(Shapes.medium)
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .padding(Dimens.MediumPadding.size),
                text = stringResource(Res.string.feature_collection_edit_shares_title),
                fontWeight = FontWeight.Bold,
            )
            IconButton(
                onClick = onNewShareClick,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = null,
                )
            }
        }

        HorizontalDivider()

        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            if (shares.isNotEmpty()) {
                shares.entries.forEach {
                    ActionRow(
                        title = it.key,
                        onClick = {
                            onShareClick(it.key)
                        },
                    )
                    if (it != shares.entries.last()) {
                        HorizontalDivider()
                    }
                }
            } else {
                Text(
                    modifier = Modifier
                        .padding(Dimens.SmallPadding.size),
                    text = stringResource(Res.string.feature_collection_edit_shares_empty),
                )
            }
        }
    }
}

private fun emptyCollection(owner: UserId) =
    Collection(
        id = "",
        owner = owner,
        name = "",
        icon = CollectionIcon.STAR,
        type = CollectionType.USER_DEFINED,
        shares = emptyMap(),
    )

@Preview
@Composable
private fun CollectionEditScreenPreview() {
    BricklogTheme {
        CollectionEditScreen(
            state = CollectionEditState(),
            onAction = {},
            validateFields = { true },
        )
    }
}
