@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package hu.piware.bricklog.feature.set.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_dashboard_title_arriving_sets
import bricklog.composeapp.generated.resources.feature_set_dashboard_title_greetings
import bricklog.composeapp.generated.resources.feature_set_dashboard_title_latest_releases
import bricklog.composeapp.generated.resources.feature_set_dashboard_title_latest_sets
import bricklog.composeapp.generated.resources.feature_set_dashboard_title_new_items
import bricklog.composeapp.generated.resources.feature_set_dashboard_title_retiring_sets
import bricklog.composeapp.generated.resources.feature_set_detail_title_by_theme
import hu.piware.bricklog.App
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.core.presentation.components.ContentColumn
import hu.piware.bricklog.feature.core.presentation.observeAsEvents
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.presentation.components.PullToRefreshColumn
import hu.piware.bricklog.feature.set.presentation.dashboard.components.ChangelogBottomSheet
import hu.piware.bricklog.feature.set.presentation.dashboard.components.DeleteUserConfirmationDialog
import hu.piware.bricklog.feature.set.presentation.dashboard.components.FeaturedSetsRow
import hu.piware.bricklog.feature.set.presentation.dashboard.components.FeaturedThemesCarousel
import hu.piware.bricklog.feature.set.presentation.dashboard.components.LogoutConfirmationBottomSheet
import hu.piware.bricklog.feature.set.presentation.dashboard.components.navigation_drawer.DashboardNavigationDrawerAction
import hu.piware.bricklog.feature.set.presentation.dashboard.components.navigation_drawer.DashboardNavigationDrawerSheet
import hu.piware.bricklog.feature.set.presentation.dashboard.components.navigation_drawer.DashboardNavigationDrawerState
import hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.SetSearchBar
import hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.SetSearchBarAction
import hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.SetSearchBarState
import hu.piware.bricklog.feature.set.presentation.dashboard.utils.arrivingSetsFilter
import hu.piware.bricklog.feature.set.presentation.dashboard.utils.latestReleasesFilter
import hu.piware.bricklog.feature.set.presentation.dashboard.utils.latestSetsFilter
import hu.piware.bricklog.feature.set.presentation.dashboard.utils.newItemsFilter
import hu.piware.bricklog.feature.set.presentation.dashboard.utils.retiringSetsFilter
import hu.piware.bricklog.feature.set.presentation.set_detail.SetDetailArguments
import hu.piware.bricklog.feature.set.presentation.set_list.SetListArguments
import hu.piware.bricklog.feature.user.domain.model.User
import hu.piware.bricklog.mock.PreviewData
import hu.piware.bricklog.ui.theme.BricklogTheme
import hu.piware.bricklog.ui.theme.Dimens
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardScreenRoot(
    viewModel: DashboardViewModel = koinViewModel(),
    onSearchSets: (SetListArguments) -> Unit,
    onSetClick: (SetDetailArguments) -> Unit,
    onNotificationSettingsClick: () -> Unit,
    onAboutClick: () -> Unit,
    onAppearanceClick: () -> Unit,
    onCollectionEditClick: (CollectionId?) -> Unit,
    onScanClick: () -> Unit,
    onThemeListClick: () -> Unit,
    onLoginClick: () -> Unit,
    selectedThemes: Set<String>?,
    selectedPackagingTypes: Set<String>?,
) {
    App.firstScreenLoaded = true

    observeAsEvents(viewModel.eventChannel) { event ->
        when (event) {
            DashboardEvent.LoginProposed -> onLoginClick()
        }
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val searchBarState by viewModel.searchBarState.collectAsStateWithLifecycle()
    val navigationDrawerState by viewModel.navigationDrawerState.collectAsStateWithLifecycle()

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
                is DashboardAction.OnThemeListClick -> onThemeListClick()
                else -> Unit
            }
            viewModel.onAction(action)
        },
        searchBarState = searchBarState,
        onSearchBarAction = { action ->
            when (action) {
                SetSearchBarAction.OnScanClick -> onScanClick()
                is SetSearchBarAction.OnSetClick -> onSetClick(action.arguments)
                is SetSearchBarAction.OnShowAllClick -> onSearchSets(action.arguments)
                else -> Unit
            }
            viewModel.onAction(action)
        },
        navigationDrawerState = navigationDrawerState,
        onNavigationDrawerAction = { action ->
            when (action) {
                is DashboardNavigationDrawerAction.OnSearchSets -> onSearchSets(action.arguments)
                is DashboardNavigationDrawerAction.OnNotificationSettingsClick -> onNotificationSettingsClick()
                is DashboardNavigationDrawerAction.OnAboutClick -> onAboutClick()
                is DashboardNavigationDrawerAction.OnAppearanceClick -> onAppearanceClick()
                is DashboardNavigationDrawerAction.OnCollectionEditClick -> onCollectionEditClick(
                    action.id
                )

                is DashboardNavigationDrawerAction.OnLoginClick -> onLoginClick()

                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun DashboardScreen(
    state: DashboardState,
    onAction: (DashboardAction) -> Unit,
    searchBarState: SetSearchBarState,
    onSearchBarAction: (SetSearchBarAction) -> Unit,
    navigationDrawerState: DashboardNavigationDrawerState,
    onNavigationDrawerAction: (DashboardNavigationDrawerAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        modifier = modifier,
        drawerContent = {
            DashboardNavigationDrawerSheet(
                drawerState = drawerState,
                state = navigationDrawerState,
                onAction = onNavigationDrawerAction
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
                ) {
                    ContentColumn(
                        modifier = Modifier
                            .testTag("dashboard:body")
                            .fillMaxSize(),
                        contentPadding = PaddingValues(
                            top = Dimens.MediumPadding.size,
                            bottom = Dimens.MediumPadding.size + padding.calculateBottomPadding()
                        )
                    ) {
                        if (state.currentUser != null) {
                            Greetings(
                                modifier = Modifier.padding(bottom = Dimens.MediumPadding.size),
                                user = state.currentUser
                            )
                        }

                        FeaturedThemesCarousel(
                            modifier = Modifier
                                .testTag("dashboard:featured_themes")
                                .padding(horizontal = Dimens.MediumPadding.size),
                            onItemClick = { item ->
                                scope.launch {
                                    val themeName = getString(item.contentDescriptionResId)
                                    val title =
                                        getString(
                                            Res.string.feature_set_detail_title_by_theme,
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

                        TextButton(
                            modifier = Modifier
                                .padding(
                                    top = Dimens.SmallPadding.size,
                                    end = Dimens.SmallPadding.size
                                )
                                .align(Alignment.End),
                            onClick = { onAction(DashboardAction.OnThemeListClick) }
                        ) {
                            Text("Show all themes")
                        }

                        FeaturedSets(
                            state = state,
                            onAction = onAction
                        )
                    }
                }
            }
        }
    }

    if (state.changelog != null && state.changelog.releases.isNotEmpty()) {
        ChangelogBottomSheet(
            changelog = state.changelog,
            onDismiss = { onAction(DashboardAction.OnUpdateChangelogReadVersion) },
        )
    }

    if (state.showLogoutConfirm) {
        LogoutConfirmationBottomSheet(
            onDismiss = { onNavigationDrawerAction(DashboardNavigationDrawerAction.OnLogoutDismiss) },
            onConfirm = { onNavigationDrawerAction(DashboardNavigationDrawerAction.OnLogoutConfirm) }
        )
    }

    if (state.showDeleteUserConfirm) {
        DeleteUserConfirmationDialog(
            onDismiss = { onNavigationDrawerAction(DashboardNavigationDrawerAction.OnDeleteUserDismiss) },
            onConfirm = { onNavigationDrawerAction(DashboardNavigationDrawerAction.OnDeleteUserConfirm) }
        )
    }
}

@Composable
private fun Greetings(
    user: User,
    modifier: Modifier = Modifier,
) {
    val firstName = user.displayName?.split(" ")?.firstOrNull()

    Row(
        modifier = modifier.padding(Dimens.MediumPadding.size)
    ) {
        Text(
            text = stringResource(Res.string.feature_set_dashboard_title_greetings),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        if (firstName != null) {
            Text(
                text = " $firstName",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun FeaturedSets(
    state: DashboardState,
    onAction: (DashboardAction) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size)
    ) {
        FeaturedSetsRow(
            title = stringResource(Res.string.feature_set_dashboard_title_latest_sets),
            onDashboardAction = onAction,
            sets = state.latestSets,
            filterOverrides = latestSetsFilter,
            sharedElementPrefix = "latest_sets"
        )

        FeaturedSetsRow(
            title = stringResource(Res.string.feature_set_dashboard_title_latest_releases),
            onDashboardAction = onAction,
            sets = state.latestReleases,
            filterOverrides = latestReleasesFilter,
            sharedElementPrefix = "latest_releases"
        )

        FeaturedSetsRow(
            title = stringResource(Res.string.feature_set_dashboard_title_arriving_sets),
            onDashboardAction = onAction,
            sets = state.arrivingSets,
            filterOverrides = arrivingSetsFilter,
            sharedElementPrefix = "arriving_sets"
        )

        FeaturedSetsRow(
            title = stringResource(Res.string.feature_set_dashboard_title_retiring_sets),
            onDashboardAction = onAction,
            sets = state.retiringSets,
            filterOverrides = retiringSetsFilter,
            sharedElementPrefix = "retiring_sets"
        )

        FeaturedSetsRow(
            title = stringResource(Res.string.feature_set_dashboard_title_new_items),
            onDashboardAction = onAction,
            sets = state.newItems,
            filterOverrides = newItemsFilter,
            sharedElementPrefix = "new_items"
        )
    }
}

@Preview
@Composable
private fun DashboardScreenPreview() {
    BricklogTheme {
        DashboardScreen(
            state = DashboardState(
                currentUser = PreviewData.user
            ),
            onAction = {},
            searchBarState = SetSearchBarState(),
            onSearchBarAction = {},
            navigationDrawerState = DashboardNavigationDrawerState(),
            onNavigationDrawerAction = {}
        )
    }
}
