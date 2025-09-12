package hu.piware.bricklog.feature.set.presentation.set_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import hu.piware.bricklog.feature.collection.domain.model.CollectionDetails
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.model.isEditable
import hu.piware.bricklog.feature.collection.presentation.components.CollectionToggleBottomSheet
import hu.piware.bricklog.feature.currency.domain.model.CurrencyPreferenceDetails
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetListDisplayMode
import hu.piware.bricklog.feature.set.domain.model.setID
import hu.piware.bricklog.feature.set.domain.util.combineSetWithCurrencyPreference
import hu.piware.bricklog.feature.set.presentation.components.ImageSize
import hu.piware.bricklog.feature.user.domain.model.User
import hu.piware.bricklog.feature.user.domain.model.isAuthenticated
import hu.piware.bricklog.mock.PreviewData
import hu.piware.bricklog.ui.theme.BricklogTheme
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PagedSetList(
    sets: LazyPagingItems<SetDetails>,
    currencyDetails: CurrencyPreferenceDetails?,
    currentUser: User,
    baseCollection: CollectionDetails?,
    availableCollections: List<CollectionDetails>,
    onSetClick: (SetDetails) -> Unit,
    onCollectionToggle: (SetDetails, CollectionId) -> Unit,
    scrollState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(
        top = 12.dp,
        start = 12.dp,
        end = 12.dp,
        bottom = 12.dp + WindowInsets.navigationBars.asPaddingValues()
            .calculateBottomPadding(),
    ),
    displayMode: SetListDisplayMode = SetListDisplayMode.COLUMN,
    modifier: Modifier = Modifier,
) {
    when (displayMode) {
        SetListDisplayMode.COLUMN -> {
            LazyColumn(
                modifier = modifier,
                state = scrollState,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = contentPadding,
            ) {
                items(
                    count = sets.itemCount,
                    key = sets.itemKey { it.setID },
                ) { index ->
                    sets[index]?.let { set ->
                        SetListItem(
                            setDetails = set,
                            setPriceDetails = combineSetWithCurrencyPreference(
                                set,
                                currencyDetails,
                            ),
                            modifier = Modifier
                                .testTag("set_list:item")
                                .widthIn(max = 700.dp)
                                .fillMaxWidth(),
                            onClick = onSetClick,
                            collectionActionItem = {
                                CollectionActionButton(
                                    setDetails = set,
                                    baseCollection = baseCollection,
                                    availableCollections = availableCollections,
                                    onCollectionToggle = onCollectionToggle,
                                    showRoleAndOwner = currentUser.isAuthenticated,
                                )
                            },
                        )
                    }
                }
            }
        }

        SetListDisplayMode.GRID -> {
            LazyVerticalStaggeredGrid(
                modifier = modifier,
                columns = StaggeredGridCells.Adaptive(minSize = 150.dp),
                verticalItemSpacing = 12.dp,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = contentPadding,
            ) {
                items(
                    count = sets.itemCount,
                    key = sets.itemKey { it.setID },
                ) { index ->
                    sets[index]?.let { set ->
                        SetGridItem(
                            setDetails = set,
                            setPriceDetails = combineSetWithCurrencyPreference(
                                set,
                                currencyDetails,
                            ),
                            modifier = Modifier
                                .testTag("set_list:item")
                                .fillMaxWidth(),
                            onClick = onSetClick,
                            collectionActionItem = {
                                CollectionActionButton(
                                    setDetails = set,
                                    baseCollection = baseCollection,
                                    availableCollections = availableCollections,
                                    onCollectionToggle = onCollectionToggle,
                                    showRoleAndOwner = currentUser.isAuthenticated,
                                    iconColor = Color.Black,
                                )
                            },
                            imageSize = ImageSize.SMALL,
                        )
                    }
                }
            }
        }

        SetListDisplayMode.GRID_LARGE -> {
            LazyVerticalStaggeredGrid(
                modifier = modifier,
                columns = StaggeredGridCells.Adaptive(minSize = 300.dp),
                verticalItemSpacing = 12.dp,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = contentPadding,
            ) {
                items(
                    count = sets.itemCount,
                    key = sets.itemKey { it.setID },
                ) { index ->
                    sets[index]?.let { set ->
                        SetGridItem(
                            setDetails = set,
                            setPriceDetails = combineSetWithCurrencyPreference(
                                set,
                                currencyDetails,
                            ),
                            modifier = Modifier
                                .testTag("set_list:item")
                                .fillMaxWidth(),
                            onClick = onSetClick,
                            collectionActionItem = {
                                CollectionActionButton(
                                    setDetails = set,
                                    baseCollection = baseCollection,
                                    availableCollections = availableCollections,
                                    onCollectionToggle = onCollectionToggle,
                                    showRoleAndOwner = currentUser.isAuthenticated,
                                    iconColor = Color.Black,
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CollectionActionButton(
    setDetails: SetDetails,
    baseCollection: CollectionDetails?,
    availableCollections: List<CollectionDetails>,
    onCollectionToggle: (SetDetails, CollectionId) -> Unit,
    showRoleAndOwner: Boolean,
    iconColor: Color = LocalContentColor.current,
) {
    if (baseCollection != null) {
        IconButton(
            onClick = { onCollectionToggle(setDetails, baseCollection.collection.id) },
            enabled = baseCollection.isEditable,
        ) {
            Icon(
                imageVector = Icons.Outlined.Remove,
                contentDescription = null,
                tint = iconColor,
            )
        }
    } else {
        var showCollectionBottomSheet by remember { mutableStateOf(false) }

        IconButton(
            onClick = { showCollectionBottomSheet = true },
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = null,
                tint = iconColor,
            )
        }

        if (showCollectionBottomSheet) {
            CollectionToggleBottomSheet(
                availableOptions = availableCollections,
                selectedItems = setDetails.collections.map { it.id },
                onToggleCollection = { onCollectionToggle(setDetails, it) },
                onDismiss = { showCollectionBottomSheet = false },
                showRoleAndOwner = showRoleAndOwner,
            )
        }
    }
}

@Preview
@Composable
private fun PagedSetListPreview() {
    BricklogTheme {
        PagedSetList(
            sets = flowOf(
                PagingData.from(
                    PreviewData.sets,
                    sourceLoadStates =
                        LoadStates(
                            refresh = LoadState.NotLoading(false),
                            append = LoadState.NotLoading(false),
                            prepend = LoadState.NotLoading(false),
                        ),
                ),
            ).collectAsLazyPagingItems(),
            currencyDetails = null,
            currentUser = PreviewData.user,
            baseCollection = null,
            availableCollections = PreviewData.collectionDetails,
            onSetClick = {},
            onCollectionToggle = { _, _ -> },
        )
    }
}
