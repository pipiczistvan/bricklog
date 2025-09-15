package hu.piware.bricklog.feature.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_dashboard_title_new_items
import hu.piware.bricklog.feature.collection.domain.usecase.InitializeDefaultCollections
import hu.piware.bricklog.feature.core.domain.AccountSyncedRepository
import hu.piware.bricklog.feature.core.domain.AppEvent
import hu.piware.bricklog.feature.onboarding.domain.usecase.InitializeChangelogReadVersion
import hu.piware.bricklog.feature.set.domain.model.DateFilter
import hu.piware.bricklog.feature.set.presentation.SetRoute
import hu.piware.bricklog.feature.set.presentation.dashboard.utils.newItemsFilter
import hu.piware.bricklog.feature.set.presentation.set_list.SetListArguments
import hu.piware.bricklog.feature.user.domain.manager.SessionManager.Companion.USER_ID_GUEST
import hu.piware.bricklog.feature.user.domain.usecase.InitializeSession
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.koin.compose.LocalKoinScope

object AppEventController {
    private val _events = Channel<AppEvent>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: AppEvent) {
        _events.send(event)
    }
}

@Composable
fun AppEventHandler(navController: NavController) {
    val scope = rememberCoroutineScope()
    val koinScope = LocalKoinScope.current
    val syncedRepositories = remember { koinScope.getAll<AccountSyncedRepository>() }
    val initializeSession = remember { koinScope.get<InitializeSession>() }
    val initializeChangelogReadVersion =
        remember { koinScope.get<InitializeChangelogReadVersion>() }
    val initializeDefaultCollections = remember { koinScope.get<InitializeDefaultCollections>() }

    observeAsEvents(AppEventController.events) { event ->
        when (event) {
            AppEvent.Initialize -> scope.launch {
                initializeSession().showSnackbarOnError()
                initializeChangelogReadVersion()
                initializeDefaultCollections(USER_ID_GUEST).showSnackbarOnError()
            }

            AppEvent.UserChanged -> syncedRepositories.forEach { it.startSync(scope) }

            is AppEvent.ShowNewSets -> scope.launch {
                navController.navigate(
                    SetRoute.SetListScreen(
                        SetListArguments.Filtered(
                            title = getString(Res.string.feature_set_dashboard_title_new_items),
                            filterOverrides = newItemsFilter.copy(
                                appearanceDate = DateFilter.Custom(
                                    startDateMs = event.startDateMs,
                                ),
                            ),
                            showFilterBar = false,
                        ),
                    ),
                ) {
                    launchSingleTop = true
                }
            }
        }
    }
}
