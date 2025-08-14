package hu.piware.bricklog.feature.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import hu.piware.bricklog.feature.core.domain.AccountSyncedRepository
import hu.piware.bricklog.feature.core.domain.AppEvent
import hu.piware.bricklog.feature.onboarding.domain.usecase.InitializeChangelogReadVersion
import hu.piware.bricklog.feature.user.domain.usecase.InitializeSession
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.koin.compose.LocalKoinScope

object AppEventController {
    private val _events = Channel<AppEvent>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: AppEvent) {
        _events.send(event)
    }
}

@Composable
fun AppEventHandler() {
    val scope = rememberCoroutineScope()
    val koinScope = LocalKoinScope.current
    val syncedRepositories = remember { koinScope.getAll<AccountSyncedRepository>() }
    val initializeSession = remember { koinScope.get<InitializeSession>() }
    val initializeChangelogReadVersion =
        remember { koinScope.get<InitializeChangelogReadVersion>() }

    observeAsEvents(AppEventController.events) { event ->
        when (event) {
            AppEvent.Initialize -> scope.launch {
                initializeSession().showSnackbarOnError()
                initializeChangelogReadVersion()
            }

            AppEvent.StartSync -> syncedRepositories.forEach { it.startSync(scope) }
        }
    }
}
