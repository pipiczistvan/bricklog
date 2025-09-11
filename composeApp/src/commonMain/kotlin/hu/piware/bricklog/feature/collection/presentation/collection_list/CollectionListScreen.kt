@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.collection.presentation.collection_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.DragHandle
import androidx.compose.material.icons.outlined.Reorder
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import bricklog.composeapp.generated.resources.feature_collection_list_title
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.model.CollectionSetDetails
import hu.piware.bricklog.feature.collection.domain.model.containerColor
import hu.piware.bricklog.feature.collection.domain.model.textColor
import hu.piware.bricklog.feature.user.domain.model.isAuthenticated
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import sh.calvin.reorderable.ReorderableCollectionItemScope
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun CollectionListScreenRoot(
    viewModel: CollectionListViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onCollectionEditClick: (CollectionId?) -> Unit,
    onCollectionClick: (CollectionId) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CollectionListScreen(
        modifier = Modifier.testTag("collection_list_screen"),
        state = state,
        onAction = { action ->
            when (action) {
                is CollectionListAction.OnBackClick -> onBackClick()
                is CollectionListAction.OnCollectionEditClick -> onCollectionEditClick(action.id)
                is CollectionListAction.OnCollectionClick -> onCollectionClick(action.id)
                else -> Unit
            }
            viewModel.onAction(action)
        },
    )
}

@Composable
private fun CollectionListScreen(
    state: CollectionListState,
    onAction: (CollectionListAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var editMode by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(Res.string.feature_collection_list_title))
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(CollectionListAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            editMode = !editMode
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Reorder,
                            contentDescription = null,
                        )
                    }
                    IconButton(
                        onClick = {
                            onAction(CollectionListAction.OnCollectionEditClick(null))
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
    ) { padding ->
        val lazyListState = rememberLazyListState()
        val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
            val reorderedItems = state.collections
                .toMutableList()
                .apply {
                    add(to.index, removeAt(from.index))
                }

            onAction(CollectionListAction.OnCollectionOrderChange(reorderedItems))
        }

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = Dimens.MediumPadding.size),
            state = lazyListState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding(),
            ),
            verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
        ) {
            items(state.collections, key = { it.collection.collection.id }) { collectionDetails ->
                ReorderableItem(
                    state = reorderableLazyListState,
                    key = collectionDetails.collection.collection.id,
                ) {
                    CollectionSetDetailsItem(
                        details = collectionDetails,
                        showRoleAndOwner = state.currentUser.isAuthenticated,
                        showDragHandle = editMode,
                        onClick = {
                            onAction(CollectionListAction.OnCollectionClick(collectionDetails.collection.collection.id))
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun ReorderableCollectionItemScope.CollectionSetDetailsItem(
    details: CollectionSetDetails,
    showRoleAndOwner: Boolean,
    showDragHandle: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.MediumPadding.size),
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.padding(end = Dimens.SmallPadding.size),
                    imageVector = details.collection.collection.icon.outlinedIcon,
                    contentDescription = null,
                )
                Column {
                    Text(
                        text = details.collection.collection.name,
                    )
                    if (showRoleAndOwner) {
                        Text(
                            text = details.collection.collection.owner,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
            }
            if (showRoleAndOwner) {
                Badge(
                    containerColor = details.collection.role.containerColor,
                    contentColor = details.collection.role.textColor,
                ) {
                    Text(stringResource(details.collection.role.stringRes))
                }
            }
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(Dimens.SmallPadding.size)
                    .align(Alignment.CenterVertically),
                visible = showDragHandle,
            ) {
                Icon(
                    modifier = Modifier
                        .draggableHandle(),
                    imageVector = Icons.Outlined.DragHandle,
                    contentDescription = null,
                )
            }
        }
    }
}
