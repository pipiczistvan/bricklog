@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.dashboard_section_latest_sets
import bricklog.composeapp.generated.resources.dashboard_section_retiring_sets
import bricklog.composeapp.generated.resources.dashboard_theme_carousel_title
import hu.piware.bricklog.App
import hu.piware.bricklog.feature.core.presentation.components.ContentColumn
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.presentation.components.PullToRefreshColumn
import hu.piware.bricklog.feature.set.presentation.dashboard.components.DashboardNavigationDrawerContent
import hu.piware.bricklog.feature.set.presentation.dashboard.components.FeaturedThemesCarousel
import hu.piware.bricklog.feature.set.presentation.dashboard.components.SetCardRow
import hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.SetSearchBar
import hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.SetSearchBarAction
import hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.SetSearchBarState
import hu.piware.bricklog.feature.set.presentation.dashboard.utils.latestSetsFilter
import hu.piware.bricklog.feature.set.presentation.dashboard.utils.retiringSetsFilter
import hu.piware.bricklog.feature.set.presentation.set_detail.SetDetailArguments
import hu.piware.bricklog.feature.set.presentation.set_filter.packaging_type_multi_select.PackagingTypeMultiSelectArguments
import hu.piware.bricklog.feature.set.presentation.set_filter.theme_multi_select.ThemeMultiSelectArguments
import hu.piware.bricklog.feature.set.presentation.set_list.SetListArguments
import hu.piware.bricklog.ui.theme.Dimens
import hu.piware.bricklog.ui.theme.Shapes
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardScreenRoot(
    viewModel: DashboardViewModel = koinViewModel(),
    onSearchSets: (SetListArguments) -> Unit,
    onSetClick: (SetDetailArguments) -> Unit,
    onNotificationSettingsClick: () -> Unit,
    onAboutClick: () -> Unit,
    onAppearanceClick: () -> Unit,
    onScanClick: () -> Unit,
    onThemeMultiselectClick: (ThemeMultiSelectArguments) -> Unit,
    onPackagingTypeMultiselectClick: (PackagingTypeMultiSelectArguments) -> Unit,
    selectedThemes: Set<String>?,
    selectedPackagingTypes: Set<String>?,
) {
    App.firstScreenLoaded = true

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val searchBarState by viewModel.searchBarState.collectAsStateWithLifecycle()

    LaunchedEffect(selectedThemes) {
        if (selectedThemes != null) {
            viewModel.onAction(
                SetSearchBarAction.OnFilterChange(
                    searchBarState.filterPreferences.copy(
                        themes = selectedThemes
                    )
                )
            )
        }
    }

    LaunchedEffect(selectedPackagingTypes) {
        if (selectedPackagingTypes != null) {
            viewModel.onAction(
                SetSearchBarAction.OnFilterChange(
                    searchBarState.filterPreferences.copy(
                        packagingTypes = selectedPackagingTypes
                    )
                )
            )
        }
    }

    DashboardScreen(
        modifier = Modifier.testTag("dashboard_screen"),
        state = state,
        onAction = { action ->
            when (action) {
                is DashboardAction.OnSetClick -> onSetClick(action.arguments)
                is DashboardAction.OnSearchSets -> onSearchSets(action.arguments)
                is DashboardAction.OnNotificationSettingsClick -> onNotificationSettingsClick()
                is DashboardAction.OnAboutClick -> onAboutClick()
                is DashboardAction.OnAppearanceClick -> onAppearanceClick()
                else -> Unit
            }
            viewModel.onAction(action)
        },
        searchBarState = searchBarState,
        onSearchBarAction = { action ->
            when (action) {
                SetSearchBarAction.OnScanClick -> onScanClick()
                is SetSearchBarAction.OnThemeMultiselectClick -> onThemeMultiselectClick(action.arguments)
                is SetSearchBarAction.OnPackagingTypeMultiselectClick -> onPackagingTypeMultiselectClick(
                    action.arguments
                )

                is SetSearchBarAction.OnSetClick -> onSetClick(action.arguments)
                is SetSearchBarAction.OnShowAllClick -> onSearchSets(action.arguments)
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun DashboardScreen(
    state: DashboardState,
    onAction: (DashboardAction) -> Unit,
    searchBarState: SetSearchBarState,
    onSearchBarAction: (SetSearchBarAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        modifier = modifier,
        drawerContent = {
            DashboardNavigationDrawerContent(
                state = drawerState,
                onAction = onAction
            )
        },
        drawerState = drawerState
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    SetSearchBar(
                        modifier = Modifier
                            .testTag("dashboard_screen:search_bar"),
                        state = searchBarState,
                        onAction = { action ->
                            when (action) {
                                SetSearchBarAction.OnDrawerClick -> scope.launch { drawerState.open() }
                                else -> Unit
                            }
                            onSearchBarAction(action)
                        },
                    )
                }
            }
        ) { padding ->
            Surface(
                modifier = Modifier
                    .padding(top = padding.calculateTopPadding())
                    .padding(top = Dimens.MediumPadding.size)
                    .fillMaxSize(),
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(
                    topStart = 32.dp,
                    topEnd = 32.dp
                )
            ) {
                PullToRefreshColumn(
                    isRefreshing = state.areSetsRefreshing,
                    onRefresh = { onAction(DashboardAction.OnRefreshSets) },
                    lastUpdated = state.lastUpdated
                ) {
                    ContentColumn(
                        modifier = Modifier
                            .testTag("dashboard:body")
                            .fillMaxSize(),
                        contentPadding = PaddingValues(
                            top = Dimens.MediumPadding.size,
                            bottom = Dimens.MediumPadding.size + padding.calculateBottomPadding()
                        ),
                        verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)
                    ) {
                        FeaturedThemesCarousel(
                            modifier = Modifier
                                .testTag("dashboard:featured_themes")
                                .padding(horizontal = Dimens.MediumPadding.size),
                            onItemClick = { item ->
                                scope.launch {
                                    val themeName = getString(item.contentDescriptionResId)
                                    val title =
                                        getString(
                                            Res.string.dashboard_theme_carousel_title,
                                            themeName
                                        )

                                    onAction(
                                        DashboardAction.OnSearchSets(
                                            arguments = SetListArguments(
                                                filterOverrides = SetFilter(
                                                    themes = setOf(item.theme)
                                                ),
                                                title = title
                                            )
                                        )
                                    )
                                }
                            }
                        )

                        SectionTitle(
                            modifier = Modifier.padding(start = Dimens.MediumPadding.size),
                            title = stringResource(Res.string.dashboard_section_latest_sets),
                            onClick = {
                                scope.launch {
                                    onAction(
                                        DashboardAction.OnSearchSets(
                                            arguments = SetListArguments(
                                                filterOverrides = latestSetsFilter,
                                                title = getString(Res.string.dashboard_section_latest_sets)
                                            )
                                        )
                                    )
                                }
                            }
                        )
                        SetCardRow(
                            sets = state.latestSets,
                            onSetClick = {
                                onAction(DashboardAction.OnSetClick(it))
                            },
                            sharedElementPrefix = "latest_sets"
                        )

                        SectionTitle(
                            modifier = Modifier.padding(start = Dimens.MediumPadding.size),
                            title = stringResource(Res.string.dashboard_section_retiring_sets),
                            onClick = {
                                scope.launch {
                                    onAction(
                                        DashboardAction.OnSearchSets(
                                            arguments = SetListArguments(
                                                filterOverrides = retiringSetsFilter,
                                                title = getString(Res.string.dashboard_section_retiring_sets)
                                            )
                                        )
                                    )
                                }
                            }
                        )
                        SetCardRow(
                            sets = state.retiringSets,
                            onSetClick = {
                                onAction(DashboardAction.OnSetClick(it))
                            },
                            sharedElementPrefix = "retiring_sets"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(
    title: String,
    onClick: (() -> Unit),
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .clip(Shapes.large)
                .clickable(onClick = onClick)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null
            )
        }
    }
}
