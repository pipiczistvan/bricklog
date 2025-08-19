@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.set.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_user_delete_user_data_action_reauthenticate
import bricklog.composeapp.generated.resources.feature_user_delete_user_data_message_success
import bricklog.composeapp.generated.resources.feature_user_logout_message_success
import co.touchlab.kermit.Logger
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.notifications.REMOTE_NOTIFICATION
import hu.piware.bricklog.BuildKonfig
import hu.piware.bricklog.feature.collection.domain.usecase.WatchCollectionDetails
import hu.piware.bricklog.feature.core.domain.UserError
import hu.piware.bricklog.feature.core.presentation.SnackbarAction
import hu.piware.bricklog.feature.core.presentation.UiText
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.debounceAfterFirst
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnSuccess
import hu.piware.bricklog.feature.onboarding.domain.usecase.UpdateData
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.usecase.ResetSets
import hu.piware.bricklog.feature.set.domain.usecase.UpdateChangelogReadVersion
import hu.piware.bricklog.feature.set.domain.usecase.WatchNewChangelog
import hu.piware.bricklog.feature.set.domain.usecase.WatchSetDetailsByPreferences
import hu.piware.bricklog.feature.set.domain.usecase.WatchSetFilterDomain
import hu.piware.bricklog.feature.set.domain.usecase.WatchSetUpdateInfo
import hu.piware.bricklog.feature.set.presentation.dashboard.components.navigation_drawer.DashboardNavigationDrawerAction
import hu.piware.bricklog.feature.set.presentation.dashboard.components.navigation_drawer.DashboardNavigationDrawerState
import hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.SetSearchBarAction
import hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.SetSearchBarState
import hu.piware.bricklog.feature.set.presentation.dashboard.utils.arrivingSetsFilter
import hu.piware.bricklog.feature.set.presentation.dashboard.utils.latestReleasesFilter
import hu.piware.bricklog.feature.set.presentation.dashboard.utils.latestSetsFilter
import hu.piware.bricklog.feature.set.presentation.dashboard.utils.newItemsFilter
import hu.piware.bricklog.feature.set.presentation.dashboard.utils.retiringSetsFilter
import hu.piware.bricklog.feature.settings.domain.usecase.SaveSetFilterPreferences
import hu.piware.bricklog.feature.settings.domain.usecase.WatchSetFilterPreferences
import hu.piware.bricklog.feature.user.domain.usecase.DeleteUserData
import hu.piware.bricklog.feature.user.domain.usecase.LogOutUser
import hu.piware.bricklog.feature.user.domain.usecase.WatchCurrentUser
import hu.piware.bricklog.feature.user.domain.usecase.WatchUserPreferences
import hu.piware.bricklog.util.DevLevels
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Provided

@KoinViewModel
class DashboardViewModel(
    private val watchSetDetailsByPreferences: WatchSetDetailsByPreferences,
    private val updateData: UpdateData,
    private val saveSetFilterPreferences: SaveSetFilterPreferences,
    @Provided private val permissionsController: PermissionsController,
    private val watchSetFilterPreferences: WatchSetFilterPreferences,
    private val resetSets: ResetSets,
    private val watchSetFilterDomain: WatchSetFilterDomain,
    private val watchNewChangelog: WatchNewChangelog,
    private val updateChangelogReadVersion: UpdateChangelogReadVersion,
    private val watchCollectionDetails: WatchCollectionDetails,
    private val watchCurrentUser: WatchCurrentUser,
    @Provided private val logOutUser: LogOutUser,
    @Provided private val deleteUserData: DeleteUserData,
    private val watchSetUpdateInfo: WatchSetUpdateInfo,
    private val watchUserPreferences: WatchUserPreferences,
) : ViewModel() {

    private val logger = Logger.withTag("DashboardViewModel")

    private val _uiState = MutableStateFlow(DashboardState())
    private val _searchBarState = MutableStateFlow(SetSearchBarState())
    private val _navigationDrawerState = MutableStateFlow(DashboardNavigationDrawerState())

    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            observeLatestSets()
            observeLatestReleases()
            observeArrivingSets()
            observeRetiringSets()
            observeNewItems()
            observeSetFilterDomain()
            observeNewChangelog()
            observeCurrentUser()
            observeUserPreferences()
            if (BuildKonfig.DEV_LEVEL < DevLevels.BENCHMARK) {
                askNotificationPermission()
            }
        }

    private val _eventChannel = Channel<DashboardEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    val searchBarState = _searchBarState
        .asStateFlowIn(viewModelScope) {
            observeFilterPreferences()
            observeTypedQuery()
            observeSets()
        }

    val navigationDrawerState = _navigationDrawerState
        .asStateFlowIn(viewModelScope) {
            observeCollections()
            observeSetUpdateInfo()
        }

    fun onAction(action: DashboardAction) {
        when (action) {
            is DashboardAction.OnRefreshData -> refreshData()
            is DashboardAction.OnUpdateChangelogReadVersion -> viewModelScope.launch {
                updateChangelogReadVersion()
            }

            else -> Unit
        }
    }

    fun onAction(action: SetSearchBarAction) {
        when (action) {
            is SetSearchBarAction.OnQueryChange -> _searchBarState.update {
                it.copy(typedQuery = action.query)
            }

            is SetSearchBarAction.OnFilterChange -> viewModelScope.launch {
                saveSetFilterPreferences(action.filterPreferences)
            }

            SetSearchBarAction.OnClearClick -> {
                _searchBarState.update {
                    it.copy(
                        typedQuery = "",
                        searchQuery = "",
                    )
                }
            }

            else -> Unit
        }
    }

    fun onAction(action: DashboardNavigationDrawerAction) {
        when (action) {
            is DashboardNavigationDrawerAction.OnResetSets -> resetSetsClick(action.date)

            DashboardNavigationDrawerAction.OnLogoutClick -> _uiState.update {
                it.copy(
                    showLogoutConfirm = true,
                )
            }

            DashboardNavigationDrawerAction.OnLogoutDismiss -> _uiState.update {
                it.copy(
                    showLogoutConfirm = false,
                )
            }

            DashboardNavigationDrawerAction.OnLogoutConfirm -> viewModelScope.launch {
                logOutUser()
                    .showSnackbarOnSuccess(Res.string.feature_user_logout_message_success)
                    .showSnackbarOnError()
            }

            DashboardNavigationDrawerAction.OnDeleteUserClick -> _uiState.update {
                it.copy(
                    showDeleteUserConfirm = true,
                )
            }

            DashboardNavigationDrawerAction.OnDeleteUserDismiss -> _uiState.update {
                it.copy(
                    showDeleteUserConfirm = false,
                )
            }

            DashboardNavigationDrawerAction.OnDeleteUserConfirm -> viewModelScope.launch {
                deleteUserData()
                    .showSnackbarOnSuccess(Res.string.feature_user_delete_user_data_message_success)
                    .showSnackbarOnError { error ->
                        if (error == UserError.General.REAUTHENTICATION_REQUIRED) {
                            SnackbarAction(
                                name = UiText.StringResourceId(Res.string.feature_user_delete_user_data_action_reauthenticate),
                                action = {
                                    viewModelScope.launch {
                                        _eventChannel.send(DashboardEvent.LoginProposed)
                                    }
                                },
                            )
                        } else {
                            null
                        }
                    }
            }

            else -> Unit
        }
    }

    private fun observeFilterPreferences() {
        watchSetFilterPreferences()
            .onEach { filter ->
                _searchBarState.update { it.copy(filterPreferences = filter) }
            }
            .launchIn(viewModelScope)
    }

    private fun observeTypedQuery() {
        searchBarState
            .mapNotNull { it.typedQuery }
            .distinctUntilChanged()
            .debounceAfterFirst(300L)
            .onEach { typedQuery -> _searchBarState.update { it.copy(searchQuery = typedQuery) } }
            .launchIn(viewModelScope)
    }

    private fun observeSets() {
        searchBarState
            .mapNotNull { it.searchQuery }
            .distinctUntilChanged()
            .flatMapLatest { query ->
                watchSetDetailsByPreferences(
                    filterOverrides = SetFilter(
                        query = query,
                        limit = SEARCH_BAR_RESULT_LIMIT,
                    ),
                )
            }
            .onEach { sets -> _searchBarState.update { it.copy(searchResults = sets) } }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    private fun observeLatestSets() {
        watchSetDetailsByPreferences(filterOverrides = latestSetsFilter.copy(limit = FEATURED_SETS_ROW_LIMIT))
            .onEach { sets -> _uiState.update { it.copy(latestSets = sets) } }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    private fun observeLatestReleases() {
        watchSetDetailsByPreferences(filterOverrides = latestReleasesFilter.copy(limit = FEATURED_SETS_ROW_LIMIT))
            .onEach { sets -> _uiState.update { it.copy(latestReleases = sets) } }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    private fun observeArrivingSets() {
        watchSetDetailsByPreferences(filterOverrides = arrivingSetsFilter.copy(limit = FEATURED_SETS_ROW_LIMIT))
            .onEach { sets -> _uiState.update { it.copy(arrivingSets = sets) } }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    private fun observeRetiringSets() {
        watchSetDetailsByPreferences(filterOverrides = retiringSetsFilter.copy(limit = FEATURED_SETS_ROW_LIMIT))
            .onEach { sets -> _uiState.update { it.copy(retiringSets = sets) } }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    private fun observeNewItems() {
        watchSetDetailsByPreferences(filterOverrides = newItemsFilter.copy(limit = FEATURED_SETS_ROW_LIMIT))
            .onEach { sets -> _uiState.update { it.copy(newItems = sets) } }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    private fun observeSetFilterDomain() {
        watchSetFilterDomain()
            .onEach { domain -> _searchBarState.update { it.copy(filterDomain = domain) } }
            .launchIn(viewModelScope)
    }

    private fun observeNewChangelog() {
        watchNewChangelog()
            .onEach { changelog -> _uiState.update { it.copy(changelog = changelog) } }
            .launchIn(viewModelScope)
    }

    private fun observeCollections() {
        watchCollectionDetails()
            .onEach { collections -> _navigationDrawerState.update { it.copy(collections = collections) } }
            .launchIn(viewModelScope)
    }

    private fun observeSetUpdateInfo() {
        watchSetUpdateInfo()
            .onEach { info -> _navigationDrawerState.update { it.copy(setUpdateInfo = info) } }
            .launchIn(viewModelScope)
    }

    private fun observeCurrentUser() {
        watchCurrentUser()
            .onEach { user -> _uiState.update { it.copy(currentUser = user) } }
            .onEach { user -> _navigationDrawerState.update { it.copy(currentUser = user) } }
            .launchIn(viewModelScope)
    }

    private fun observeUserPreferences() {
        watchUserPreferences()
            .onEach { preferences -> _uiState.update { it.copy(userPreferences = preferences) } }
            .launchIn(viewModelScope)
    }

    private fun refreshData() {
        viewModelScope.launch {
            _uiState.update { it.copy(areSetsRefreshing = true) }
            updateData().showSnackbarOnError()
            _uiState.update { it.copy(areSetsRefreshing = false) }
        }
    }

    private fun askNotificationPermission() {
        viewModelScope.launch {
            try {
                val permissionGranted =
                    permissionsController.isPermissionGranted(Permission.REMOTE_NOTIFICATION)
                if (!permissionGranted) {
                    permissionsController.providePermission(Permission.REMOTE_NOTIFICATION)
                }
                logger.i { "Notification permission granted." }
            } catch (e: DeniedAlwaysException) {
                logger.w(e) { "Notification permission denied always." }
            } catch (e: DeniedException) {
                logger.w(e) { "Notification permission denied." }
            } catch (e: RequestCanceledException) {
                logger.w(e) { "Notification permission canceled." }
            } catch (e: Exception) {
                // https://github.com/icerockdev/moko-permissions/issues/141
                logger.w(e) { "Notification permission error." }
            }
        }
    }

    private fun resetSetsClick(date: Instant) {
        viewModelScope.launch {
            resetSets(date)
                .showSnackbarOnError()
        }
    }

    companion object {
        const val SEARCH_BAR_RESULT_LIMIT = 10
        const val FEATURED_SETS_ROW_LIMIT = 9
    }
}
