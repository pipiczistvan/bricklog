@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.settings.presentation.notifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.notification_settings_general
import bricklog.composeapp.generated.resources.notification_settings_new_sets
import bricklog.composeapp.generated.resources.notification_settings_permission_description
import bricklog.composeapp.generated.resources.notification_settings_permission_open_settings
import bricklog.composeapp.generated.resources.notification_settings_permission_title
import bricklog.composeapp.generated.resources.notification_settings_title
import hu.piware.bricklog.feature.core.presentation.components.ContentColumn
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NotificationsScreenRoot(
    viewModel: NotificationsViewModel = koinViewModel(),
    onBackClick: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    NotificationsScreen(
        state = state,
        modifier = Modifier.testTag("notifications_screen"),
        onAction = { action ->
            when (action) {
                NotificationsAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun NotificationsScreen(
    state: NotificationsState,
    onAction: (NotificationsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(Res.string.notification_settings_title)) },
                navigationIcon = {
                    IconButton(onClick = { onAction(NotificationsAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
            )
        }
    ) { padding ->
        ContentColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = Dimens.MediumPadding.size)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)
        ) {
            if (!state.notificationPermissionGranted) {
                NotificationSettingsCard(
                    onButtonClick = { onAction(NotificationsAction.OpenSettings) }
                )
            }

            NotificationPreferenceRow(
                title = stringResource(Res.string.notification_settings_general),
                enabled = state.notificationPermissionGranted,
                checked = state.notificationPreferences.general,
                onCheckedChange = {
                    onAction(
                        NotificationsAction.OnNotificationPreferenceChange(
                            state.notificationPreferences.copy(general = it)
                        )
                    )
                }
            )

            NotificationPreferenceRow(
                title = stringResource(Res.string.notification_settings_new_sets),
                enabled = state.notificationPreferences.general && state.notificationPermissionGranted,
                checked = state.notificationPreferences.newSets,
                onCheckedChange = {
                    onAction(
                        NotificationsAction.OnNotificationPreferenceChange(
                            state.notificationPreferences.copy(newSets = it)
                        )
                    )
                }
            )
        }
    }
}

@Composable
private fun NotificationPreferenceRow(
    title: String,
    enabled: Boolean,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            enabled = enabled,
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text(text = title)
    }
}

@Composable
private fun NotificationSettingsCard(
    onButtonClick: () -> Unit,
) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = stringResource(Res.string.notification_settings_permission_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(Res.string.notification_settings_permission_description)
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onButtonClick
            ) {
                Text(stringResource(Res.string.notification_settings_permission_open_settings))
            }
        }
    }
}