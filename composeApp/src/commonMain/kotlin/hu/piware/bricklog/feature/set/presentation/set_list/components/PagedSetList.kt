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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import hu.piware.bricklog.feature.currency.domain.model.CurrencyPreferenceDetails
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.set.domain.model.SetListDisplayMode
import hu.piware.bricklog.feature.set.domain.model.setID
import hu.piware.bricklog.feature.set.domain.util.combineSetWithCurrencyPreference
import hu.piware.bricklog.feature.set.presentation.components.ImageSize
import hu.piware.bricklog.mock.PreviewData
import hu.piware.bricklog.ui.theme.BricklogTheme
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PagedSetList(
    sets: LazyPagingItems<SetDetails>,
    currencyDetails: CurrencyPreferenceDetails?,
    baseCollection: CollectionDetails?,
    onSetClick: (SetDetails) -> Unit,
    onCollectionToggle: (SetId, CollectionId) -> Unit,
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
                            baseCollection = baseCollection,
                            modifier = Modifier
                                .testTag("set_list:item")
                                .widthIn(max = 700.dp)
                                .fillMaxWidth(),
                            onClick = onSetClick,
                            onCollectionToggle = onCollectionToggle,
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
                            baseCollection = baseCollection,
                            modifier = Modifier
                                .testTag("set_list:item")
                                .fillMaxWidth(),
                            onClick = onSetClick,
                            onCollectionToggle = onCollectionToggle,
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
                            baseCollection = baseCollection,
                            modifier = Modifier
                                .testTag("set_list:item")
                                .fillMaxWidth(),
                            onClick = onSetClick,
                            onCollectionToggle = onCollectionToggle,
                        )
                    }
                }
            }
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
            baseCollection = null,
            onSetClick = {},
            onCollectionToggle = { _, _ -> },
        )
    }
}
