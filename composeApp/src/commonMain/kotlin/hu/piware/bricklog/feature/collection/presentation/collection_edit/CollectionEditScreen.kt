@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.collection.presentation.collection_edit

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.collection_edit_create_title
import bricklog.composeapp.generated.resources.collection_edit_modify_title
import hu.piware.bricklog.feature.collection.domain.usecase.ValidateCollectionName
import hu.piware.bricklog.feature.collection.domain.util.defaultCollections
import hu.piware.bricklog.feature.collection.presentation.collection_edit.components.CollectionDeleteConfirmDialog
import hu.piware.bricklog.feature.collection.presentation.collection_edit.components.CollectionIconBottomSheet
import hu.piware.bricklog.feature.core.presentation.components.ContentColumn
import hu.piware.bricklog.feature.core.presentation.observeAsEvents
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CollectionEditScreenRoot(
    viewModel: CollectionEditViewModel = koinViewModel(),
    onBackClick: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    observeAsEvents(viewModel.eventChannel) { event ->
        when (event) {
            CollectionEditEvent.Back -> onBackClick()
        }
    }

    CollectionEditScreen(
        modifier = Modifier.testTag("collection_edit_screen"),
        state = state,
        onAction = { action ->
            when (action) {
                is CollectionEditAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun CollectionEditScreen(
    state: CollectionEditState,
    onAction: (CollectionEditAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showIconBottomSheet by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier
            .imePadding(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (state.collectionId == 0)
                            stringResource(Res.string.collection_edit_create_title)
                        else
                            stringResource(Res.string.collection_edit_modify_title)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(CollectionEditAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    if (state.collectionId != 0 && defaultCollections.none { it.id == state.collectionId }) {
                        IconButton(onClick = { showDeleteConfirmDialog = true }) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = null
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    onAction(CollectionEditAction.OnSubmit)
                },
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Save,
                        contentDescription = null
                    )
                },
                text = {
                    Text("Save")
                }
            )
        }
    ) { padding ->
        ContentColumn(
            modifier = Modifier
                .padding(horizontal = Dimens.MediumPadding.size)
                .fillMaxSize(),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding()
            )
        ) {
            val focusManager = LocalFocusManager.current

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.name,
                onValueChange = { onAction(CollectionEditAction.OnNameChanged(it)) },
                isError = state.nameError != null,
                label = { Text("Name") },
                placeholder = { Text("Enter a name") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                leadingIcon = {
                    IconButton(
                        onClick = { showIconBottomSheet = true }
                    ) {
                        Icon(
                            imageVector = state.icon.outlinedIcon,
                            contentDescription = null
                        )
                    }
                },
                trailingIcon = {
                    Text("${state.name.length}/${ValidateCollectionName.MAX_LENGTH}")
                }
            )
            if (state.nameError != null) {
                Text(
                    text = state.nameError.asString(),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }

    CollectionIconBottomSheet(
        showBottomSheet = showIconBottomSheet,
        onShowBottomSheetChanged = { showIconBottomSheet = it },
        selectedIcon = state.icon,
        onIconClick = {
            onAction(CollectionEditAction.OnIconChanged(it))
        }
    )

    if (showDeleteConfirmDialog) {
        CollectionDeleteConfirmDialog(
            onConfirmation = {
                showDeleteConfirmDialog = false
                onAction(CollectionEditAction.OnDeleteClick)
            },
            onDismiss = {
                showDeleteConfirmDialog = false
            }
        )
    }
}
