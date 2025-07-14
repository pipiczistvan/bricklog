@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonOff
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.dashboard_navigation_drawer_item_about
import bricklog.composeapp.generated.resources.dashboard_navigation_drawer_item_appearance
import bricklog.composeapp.generated.resources.dashboard_navigation_drawer_item_login
import bricklog.composeapp.generated.resources.dashboard_navigation_drawer_item_logout
import bricklog.composeapp.generated.resources.dashboard_navigation_drawer_item_notification_settings
import bricklog.composeapp.generated.resources.dashboard_navigation_drawer_item_reset_sets
import bricklog.composeapp.generated.resources.dashboard_navigation_drawer_section_collections
import bricklog.composeapp.generated.resources.dashboard_navigation_drawer_section_developer
import bricklog.composeapp.generated.resources.dashboard_navigation_drawer_section_settings
import bricklog.composeapp.generated.resources.date_range_picker_button_confirm
import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.core.presentation.util.formatDate
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.presentation.dashboard.DashboardAction
import hu.piware.bricklog.feature.set.presentation.set_list.SetListArguments
import hu.piware.bricklog.feature.user.domain.model.User
import hu.piware.bricklog.util.BuildConfig
import hu.piware.bricklog.util.isDebugFlavor
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Duration.Companion.days

@Composable
fun DashboardNavigationDrawerContent(
    state: DrawerState,
    collections: List<Collection>,
    currentUser: User?,
    onAction: (DashboardAction) -> Unit,
) {
    ModalDrawerSheet(
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.statusBarsPadding())
        Spacer(modifier = Modifier.height(24.dp))

        CollectionsSection(
            state = state, collections = collections, onAction = onAction
        )

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
        )

        SettingsSection(
            state = state,
            currentUser = currentUser,
            onAction = onAction
        )

        if (BuildConfig.isDebugFlavor) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
            )

            DeveloperSection(
                state = state, onAction = onAction
            )
        }

        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
private fun CollectionsSection(
    state: DrawerState,
    collections: List<Collection>,
    onAction: (DashboardAction) -> Unit,
) {
    NavigationSection(
        title = stringResource(Res.string.dashboard_navigation_drawer_section_collections),
        trailingIcon = {
            IconButton(
                onClick = { onAction(DashboardAction.OnCollectionEditClick(0)) }) {
                Icon(
                    imageVector = Icons.Outlined.Add, contentDescription = null
                )
            }
        }) {
        collections.map {
            NavigationSectionButton(
                state = state,
                title = it.name,
                onClick = {
                    onAction(
                        DashboardAction.OnSearchSets(
                            SetListArguments(
                                filterOverrides = SetFilter(
                                    collectionIds = setOf(it.id)
                                ), title = it.name
                            )
                        )
                    )
                },
                icon = it.icon.outlinedIcon,
                trailingIcon = {
                    IconButton(
                        onClick = { onAction(DashboardAction.OnCollectionEditClick(it.id)) }) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = null
                        )
                    }
                })
        }
    }
}

@Composable
private fun SettingsSection(
    state: DrawerState,
    currentUser: User?,
    onAction: (DashboardAction) -> Unit,
) {
    NavigationSection(
        title = stringResource(Res.string.dashboard_navigation_drawer_section_settings)
    ) {
        val isLoggedIn = currentUser != null
        NavigationSectionButton(
            state = state,
            title = if (isLoggedIn)
                stringResource(Res.string.dashboard_navigation_drawer_item_logout)
            else
                stringResource(Res.string.dashboard_navigation_drawer_item_login),
            onClick = { onAction(if (isLoggedIn) DashboardAction.OnLogoutClick else DashboardAction.OnLoginClick) },
            icon = if (!isLoggedIn) Icons.Outlined.Person else Icons.Outlined.PersonOff,
            trailingIcon = {
                if (isLoggedIn) {
                    IconButton(onClick = { onAction(DashboardAction.OnDeleteUserClick) }) {
                        Icon(
                            imageVector = Icons.Outlined.DeleteOutline,
                            contentDescription = null
                        )
                    }
                } else Unit
            }
        )
        NavigationSectionButton(
            state = state,
            title = stringResource(Res.string.dashboard_navigation_drawer_item_notification_settings),
            onClick = { onAction(DashboardAction.OnNotificationSettingsClick) },
            icon = Icons.Outlined.Notifications,
        )
        NavigationSectionButton(
            state = state,
            title = stringResource(Res.string.dashboard_navigation_drawer_item_appearance),
            onClick = { onAction(DashboardAction.OnAppearanceClick) },
            icon = Icons.Outlined.Palette,
        )
        NavigationSectionButton(
            state = state,
            title = stringResource(Res.string.dashboard_navigation_drawer_item_about),
            onClick = { onAction(DashboardAction.OnAboutClick) },
            icon = Icons.Outlined.Info,
        )
    }
}

@Composable
private fun DeveloperSection(
    state: DrawerState,
    onAction: (DashboardAction) -> Unit,
) {
    NavigationSection(
        title = stringResource(Res.string.dashboard_navigation_drawer_section_developer)
    ) {
        val yesterday = remember { Clock.System.now() - 1.days }
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = yesterday.toEpochMilliseconds()
        )
        val selectedDate by derivedStateOf {
            datePickerState.selectedDateMillis?.let { Instant.fromEpochMilliseconds(it) }
        }
        var showDatePicker by remember { mutableStateOf(false) }

        NavigationSectionButton(
            state = state, title = stringResource(
                Res.string.dashboard_navigation_drawer_item_reset_sets,
                if (selectedDate != null) formatDate(selectedDate!!.toLocalDateTime(TimeZone.currentSystemDefault()))
                else "?"
            ), onClick = {
                if (selectedDate != null) {
                    onAction(DashboardAction.OnResetSets(selectedDate!!))
                }
            }, icon = Icons.Outlined.Restore, trailingIcon = {
                IconButton(
                    onClick = { showDatePicker = true }) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth, contentDescription = null
                    )
                }
            })

        if (showDatePicker) {
            DatePickerDialog(onDismissRequest = {

            }, confirmButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }) {
                    Text(stringResource(Res.string.date_range_picker_button_confirm))
                }
            }) {
                DatePicker(
                    state = datePickerState, showModeToggle = false
                )
            }
        }
    }
}

@Composable
private fun NavigationSection(
    title: String,
    trailingIcon: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    Column {
        NavigationSectionHeader(
            title = title, action = trailingIcon
        )
        content()
    }
}

@Composable
private fun NavigationSectionHeader(
    title: String,
    action: @Composable () -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        action()
    }
}

@Composable
private fun NavigationSectionButton(
    state: DrawerState,
    title: String,
    onClick: () -> Unit,
    icon: ImageVector,
    trailingIcon: @Composable () -> Unit = {},
) {
    val scope = rememberCoroutineScope()

    NavigationDrawerItem(
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
        label = { Text(title) },
        selected = false,
        onClick = {
            scope.launch {
                onClick()
                state.close()
            }
        },
        icon = {
            Icon(
                imageVector = icon, contentDescription = title
            )
        },
        badge = trailingIcon
    )
}

@Preview
@Composable
private fun DashboardNavigationDrawerContent() {
    MaterialTheme {
        DashboardNavigationDrawerContent(
            state = DrawerState(DrawerValue.Open),
            collections = emptyList(),
            currentUser = null,
            onAction = {}
        )
    }
}
