@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.set_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.automirrored.outlined.ViewList
import androidx.compose.material.icons.outlined.GridOn
import androidx.compose.material.icons.outlined.Window
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.lego_brick_2x3
import bricklog.composeapp.generated.resources.no_search_results
import hu.piware.bricklog.App
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetListDisplayMode
import hu.piware.bricklog.feature.set.domain.model.setID
import hu.piware.bricklog.feature.set.presentation.components.SetFilterRow
import hu.piware.bricklog.feature.set.presentation.set_detail.SetDetailArguments
import hu.piware.bricklog.feature.set.presentation.set_list.components.PagedSetList
import hu.piware.bricklog.feature.set.presentation.set_list.components.SetSortBottomSheet
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SetListScreenRoot(
    viewModel: SetListViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onSetClick: (SetDetailArguments) -> Unit,
) {
    App.firstScreenLoaded = true

    val sets = viewModel.pagingData.collectAsLazyPagingItems()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    SetListScreen(
        modifier = Modifier.testTag("set_list_screen"),
        state = state,
        sets = sets,
        onAction = { action ->
            when (action) {
                is SetListAction.OnBackClick -> onBackClick()
                is SetListAction.OnSetClick -> onSetClick(action.arguments)
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun SetListScreen(
    state: SetListState,
    sets: LazyPagingItems<SetDetails>,
    onAction: (SetListAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showSortingBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(state.title)
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(SetListAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onAction(
                                SetListAction.OnDisplayModeChange(
                                    when (state.displayMode) {
                                        SetListDisplayMode.COLUMN -> SetListDisplayMode.GRID
                                        SetListDisplayMode.GRID -> SetListDisplayMode.GRID_LARGE
                                        SetListDisplayMode.GRID_LARGE -> SetListDisplayMode.COLUMN
                                    }
                                )
                            )
                        }
                    ) {
                        Icon(
                            imageVector = when (state.displayMode) {
                                SetListDisplayMode.COLUMN -> Icons.AutoMirrored.Outlined.ViewList
                                SetListDisplayMode.GRID -> Icons.Outlined.GridOn
                                SetListDisplayMode.GRID_LARGE -> Icons.Outlined.Window
                            },
                            contentDescription = null
                        )
                    }

                    IconButton(
                        enabled = state.filterOverrides?.sortOption == null,
                        onClick = {
                            showSortingBottomSheet = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Sort,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(top = padding.calculateTopPadding())
        ) {
            if (state.showFilterBar) {
                SetFilterRow(
                    filterPreferences = state.filterPreferences,
                    filterOverrides = state.filterOverrides,
                    onFilterPreferencesChange = { onAction(SetListAction.OnFilterChange(it)) },
                    filterDomain = state.filterDomain
                )
            }

            if (sets.loadState.isIdle && sets.itemCount == 0) {
                EmptySetList(
                    modifier = Modifier
                        .fillMaxSize()
                )
            } else {
                PagedSetList(
                    modifier = Modifier
                        .testTag("set_list:sets")
                        .fillMaxSize(),
                    sets = sets,
                    onSetClick = {
                        onAction(SetListAction.OnSetClick(SetDetailArguments(it.setID, "set_list")))
                    },
                    onFavouriteClick = {
                        onAction(SetListAction.OnFavouriteClick(it.setID))
                    },
                    displayMode = state.displayMode
                )
            }
        }
    }

    SetSortBottomSheet(
        showBottomSheet = showSortingBottomSheet,
        onShowBottomSheetChanged = { showSortingBottomSheet = it },
        selectedOption = state.filterPreferences.sortOption,
        onOptionClick = {
            onAction(SetListAction.OnFilterChange(state.filterPreferences.copy(sortOption = it)))
        }
    )
}

@Composable
private fun EmptySetList(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.25f))
        Image(
            modifier = Modifier.size(100.dp),
            painter = painterResource(Res.drawable.lego_brick_2x3),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(Res.string.no_search_results))
    }
}
