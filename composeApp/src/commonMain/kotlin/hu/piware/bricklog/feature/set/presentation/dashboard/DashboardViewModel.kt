@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.set.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.notifications.REMOTE_NOTIFICATION
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.debounceAfterFirst
import hu.piware.bricklog.feature.core.presentation.showSnackbarOnError
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.usecase.ResetSets
import hu.piware.bricklog.feature.set.domain.usecase.UpdateSets
import hu.piware.bricklog.feature.set.domain.usecase.WatchBricksetUpdateInfo
import hu.piware.bricklog.feature.set.domain.usecase.WatchSetFilterDomain
import hu.piware.bricklog.feature.set.domain.usecase.WatchSetUIs
import hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.SetSearchBarAction
import hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.SetSearchBarState
import hu.piware.bricklog.feature.set.presentation.dashboard.utils.arrivingSetsFilter
import hu.piware.bricklog.feature.set.presentation.dashboard.utils.latestSetsFilter
import hu.piware.bricklog.feature.set.presentation.dashboard.utils.retiringSetsFilter
import hu.piware.bricklog.feature.settings.domain.model.NotificationPreferences
import hu.piware.bricklog.feature.settings.domain.model.SetFilterPreferences
import hu.piware.bricklog.feature.settings.domain.usecase.SaveNotificationPreferences
import hu.piware.bricklog.feature.settings.domain.usecase.SaveSetFilterPreferences
import hu.piware.bricklog.feature.settings.domain.usecase.WatchSetFilterPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant

class DashboardViewModel(
    private val watchSetUIs: WatchSetUIs,
    private val updateSets: UpdateSets,
    private val watchBricksetUpdateInfo: WatchBricksetUpdateInfo,
    private val saveSetFilterPreferences: SaveSetFilterPreferences,
    private val saveNotificationPreferences: SaveNotificationPreferences,
    private val permissionsController: PermissionsController,
    private val watchSetFilterPreferences: WatchSetFilterPreferences,
    private val resetSets: ResetSets,
    private val watchSetFilterDomain: WatchSetFilterDomain,
) : ViewModel() {

    private val logger = Logger.withTag("DashboardViewModel")

    private val _uiState = MutableStateFlow(DashboardState())
    private val _searchBarState = MutableStateFlow(SetSearchBarState())

    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            observeUpdateInfo()
            observeLatestSets()
            observeArrivingSets()
            observeRetiringSets()
            observeSetFilterDomain()
            askNotificationPermission()
        }

    val searchBarState = _searchBarState
        .asStateFlowIn(viewModelScope) {
            observeFilterPreferences()
            observeTypedQuery()
            observeSets()
        }

    fun onAction(action: DashboardAction) {
        when (action) {
            is DashboardAction.OnRefreshSets -> refreshSets()
            is DashboardAction.OnResetSets -> resetSetsClick()
            else -> Unit
        }
    }

    fun onAction(action: SetSearchBarAction) {
        when (action) {
            is SetSearchBarAction.OnQueryChange -> _searchBarState.update { it.copy(typedQuery = action.query) }
            is SetSearchBarAction.OnFilterChange -> saveFilter(action.filterPreferences)
            SetSearchBarAction.OnClearClick -> {
                _searchBarState.update {
                    it.copy(
                        typedQuery = "",
                        searchQuery = ""
                    )
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
                watchSetUIs(
                    filterOverrides = SetFilter(limit = 10),
                    query = query
                )
            }
            .onEach { sets -> _searchBarState.update { it.copy(searchResults = sets) } }
            .launchIn(viewModelScope)
    }

    private fun observeUpdateInfo() {
        watchBricksetUpdateInfo()
            .onEach { updateInfo ->
                _uiState.update { it.copy(lastUpdated = updateInfo?.lastUpdated) }
            }
            .launchIn(viewModelScope)
    }

    private fun observeLatestSets() {
        watchSetUIs(filterOverrides = latestSetsFilter.copy(limit = 12))
            .onEach { sets -> _uiState.update { it.copy(latestSets = sets) } }
            .launchIn(viewModelScope)
    }

    private fun observeArrivingSets() {
        watchSetUIs(filterOverrides = arrivingSetsFilter.copy(limit = 12))
            .onEach { sets -> _uiState.update { it.copy(arrivingSets = sets) } }
            .launchIn(viewModelScope)
    }

    private fun observeRetiringSets() {
        watchSetUIs(filterOverrides = retiringSetsFilter.copy(limit = 12))
            .onEach { sets -> _uiState.update { it.copy(retiringSets = sets) } }
            .launchIn(viewModelScope)
    }

    private fun observeSetFilterDomain() {
        watchSetFilterDomain()
            .onEach { domain -> _searchBarState.update { it.copy(filterDomain = domain) } }
            .launchIn(viewModelScope)
    }

    private fun refreshSets() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(areSetsRefreshing = true) }
            updateSets().showSnackbarOnError()
            _uiState.update { it.copy(areSetsRefreshing = false) }
        }
    }

    private fun saveFilter(filterPreferences: SetFilterPreferences) {
        viewModelScope.launch(Dispatchers.IO) {
            saveSetFilterPreferences(filterPreferences)
        }
    }

    private fun askNotificationPermission() {
        viewModelScope.launch {
            try {
                val permissionGranted =
                    permissionsController.isPermissionGranted(Permission.REMOTE_NOTIFICATION)
                if (!permissionGranted) {
                    permissionsController.providePermission(Permission.REMOTE_NOTIFICATION)
                    saveNotificationPreferences(
                        NotificationPreferences(
                            general = true,
                            newSets = true
                        )
                    )
                }
                logger.i { "Notification permission granted." }
            } catch (e: DeniedAlwaysException) {
                logger.w { "Notification permission denied always." }
            } catch (e: DeniedException) {
                logger.w { "Notification permission denied." }
            } catch (e: RequestCanceledException) {
                logger.w { "Notification permission canceled." }
            }
        }
    }

    private fun resetSetsClick() {
        viewModelScope.launch {
            resetSets(Instant.parse("2025-05-12T00:00:00Z"))
                .showSnackbarOnError()
        }
    }
}
