package hu.piware.bricklog.feature.settings.presentation.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.notifications.REMOTE_NOTIFICATION
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.settings.domain.model.NotificationPreferences
import hu.piware.bricklog.feature.settings.domain.usecase.SaveNotificationPreferences
import hu.piware.bricklog.feature.settings.domain.usecase.WatchNotificationPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Provided

@KoinViewModel
class NotificationsViewModel(
    @Provided private val permissionsController: PermissionsController,
    private val watchNotificationPreferences: WatchNotificationPreferences,
    private val saveNotificationPreferences: SaveNotificationPreferences,
) : ViewModel() {

    private var _uiState = MutableStateFlow(NotificationsState())
    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            checkNotificationPermission()
            observeNotificationPreferences()
        }

    fun onAction(action: NotificationsAction) {
        when (action) {
            NotificationsAction.OpenSettings -> permissionsController.openAppSettings()
            is NotificationsAction.OnNotificationPreferenceChange -> onNotificationPreferenceChange(
                action.preferences,
            )

            else -> Unit
        }
    }

    private fun onNotificationPreferenceChange(notificationPreferences: NotificationPreferences) {
        viewModelScope.launch {
            saveNotificationPreferences(notificationPreferences)
        }
    }

    private suspend fun checkNotificationPermission() {
        _uiState.update {
            it.copy(
                notificationPermissionGranted = permissionsController.isPermissionGranted(
                    Permission.REMOTE_NOTIFICATION,
                ),
            )
        }
    }

    private fun observeNotificationPreferences() {
        watchNotificationPreferences()
            .onEach { preferences ->
                _uiState.update { it.copy(notificationPreferences = preferences) }
            }
            .launchIn(viewModelScope)
    }
}
