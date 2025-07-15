@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.dashboard.components.navigation_drawer

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
import bricklog.composeapp.generated.resources.feature_set_dashboard_navigation_drawer_btn_about
import bricklog.composeapp.generated.resources.feature_set_dashboard_navigation_drawer_btn_appearance
import bricklog.composeapp.generated.resources.feature_set_dashboard_navigation_drawer_btn_login
import bricklog.composeapp.generated.resources.feature_set_dashboard_navigation_drawer_btn_logout
import bricklog.composeapp.generated.resources.feature_set_dashboard_navigation_drawer_btn_notification_settings
import bricklog.composeapp.generated.resources.feature_set_dashboard_navigation_drawer_btn_reset_sets
import bricklog.composeapp.generated.resources.feature_set_dashboard_navigation_drawer_label_set_update_info
import bricklog.composeapp.generated.resources.feature_set_dashboard_navigation_drawer_label_set_update_never
import bricklog.composeapp.generated.resources.feature_set_dashboard_navigation_drawer_title_collections
import bricklog.composeapp.generated.resources.feature_set_dashboard_navigation_drawer_title_developer_tools
import bricklog.composeapp.generated.resources.feature_set_dashboard_navigation_drawer_title_settings
import bricklog.composeapp.generated.resources.feature_set_search_date_filter_sheet_btn_confirm
import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.core.presentation.util.formatDate
import hu.piware.bricklog.feature.core.presentation.util.formatDateTime
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.model.UpdateInfo
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
fun DashboardNavigationDrawerSheet(
    drawerState: DrawerState,
    state: DashboardNavigationDrawerState,
    onAction: (DashboardNavigationDrawerAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalDrawerSheet(
        modifier = modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.statusBarsPadding())
        Spacer(modifier = Modifier.height(24.dp))

        CollectionsSection(
            drawerState = drawerState, collections = state.collections, onAction = onAction
        )

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
        )

        SettingsSection(
            drawerState = drawerState,
            currentUser = state.currentUser,
            onAction = onAction
        )

        if (BuildConfig.isDebugFlavor) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
            )

            DeveloperSection(
                drawerState = drawerState, onAction = onAction
            )
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
        )

        SetUpdateInfoSection(
            updateInfo = state.setUpdateInfo
        )

        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
private fun CollectionsSection(
    drawerState: DrawerState,
    collections: List<Collection>,
    onAction: (DashboardNavigationDrawerAction) -> Unit,
) {
    NavigationSection(
        title = stringResource(Res.string.feature_set_dashboard_navigation_drawer_title_collections),
        trailingIcon = {
            IconButton(
                onClick = { onAction(DashboardNavigationDrawerAction.OnCollectionEditClick(0)) }) {
                Icon(
                    imageVector = Icons.Outlined.Add, contentDescription = null
                )
            }
        }) {
        collections.map {
            NavigationSectionButton(
                state = drawerState,
                title = it.name,
                onClick = {
                    onAction(
                        DashboardNavigationDrawerAction.OnSearchSets(
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
                        onClick = {
                            onAction(
                                DashboardNavigationDrawerAction.OnCollectionEditClick(
                                    it.id
                                )
                            )
                        }) {
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
    drawerState: DrawerState,
    currentUser: User?,
    onAction: (DashboardNavigationDrawerAction) -> Unit,
) {
    NavigationSection(
        title = stringResource(Res.string.feature_set_dashboard_navigation_drawer_title_settings)
    ) {
        val isLoggedIn = currentUser != null
        NavigationSectionButton(
            state = drawerState,
            title = if (isLoggedIn)
                stringResource(Res.string.feature_set_dashboard_navigation_drawer_btn_logout)
            else
                stringResource(Res.string.feature_set_dashboard_navigation_drawer_btn_login),
            onClick = { onAction(if (isLoggedIn) DashboardNavigationDrawerAction.OnLogoutClick else DashboardNavigationDrawerAction.OnLoginClick) },
            icon = if (!isLoggedIn) Icons.Outlined.Person else Icons.Outlined.PersonOff,
            trailingIcon = {
                if (isLoggedIn) {
                    IconButton(onClick = { onAction(DashboardNavigationDrawerAction.OnDeleteUserClick) }) {
                        Icon(
                            imageVector = Icons.Outlined.DeleteOutline,
                            contentDescription = null
                        )
                    }
                } else Unit
            }
        )
        NavigationSectionButton(
            state = drawerState,
            title = stringResource(Res.string.feature_set_dashboard_navigation_drawer_btn_notification_settings),
            onClick = { onAction(DashboardNavigationDrawerAction.OnNotificationSettingsClick) },
            icon = Icons.Outlined.Notifications,
        )
        NavigationSectionButton(
            state = drawerState,
            title = stringResource(Res.string.feature_set_dashboard_navigation_drawer_btn_appearance),
            onClick = { onAction(DashboardNavigationDrawerAction.OnAppearanceClick) },
            icon = Icons.Outlined.Palette,
        )
        NavigationSectionButton(
            state = drawerState,
            title = stringResource(Res.string.feature_set_dashboard_navigation_drawer_btn_about),
            onClick = { onAction(DashboardNavigationDrawerAction.OnAboutClick) },
            icon = Icons.Outlined.Info,
        )
    }
}

@Composable
private fun DeveloperSection(
    drawerState: DrawerState,
    onAction: (DashboardNavigationDrawerAction) -> Unit,
) {
    NavigationSection(
        title = stringResource(Res.string.feature_set_dashboard_navigation_drawer_title_developer_tools)
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
            state = drawerState, title = stringResource(
                Res.string.feature_set_dashboard_navigation_drawer_btn_reset_sets,
                if (selectedDate != null) formatDate(selectedDate!!.toLocalDateTime(TimeZone.currentSystemDefault()))
                else "?"
            ), onClick = {
                if (selectedDate != null) {
                    onAction(DashboardNavigationDrawerAction.OnResetSets(selectedDate!!))
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
                    Text(stringResource(Res.string.feature_set_search_date_filter_sheet_btn_confirm))
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

@Composable
private fun SetUpdateInfoSection(
    updateInfo: UpdateInfo?,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(Res.string.feature_set_dashboard_navigation_drawer_label_set_update_info))
        if (updateInfo != null) {
            Text(formatDateTime(updateInfo.lastUpdated.toLocalDateTime(TimeZone.currentSystemDefault())))
        } else {
            Text(stringResource(Res.string.feature_set_dashboard_navigation_drawer_label_set_update_never))
        }
    }
}

@Preview
@Composable
private fun DashboardNavigationDrawerSheetPreview() {
    MaterialTheme {
        DashboardNavigationDrawerSheet(
            drawerState = DrawerState(DrawerValue.Open),
            state = DashboardNavigationDrawerState(),
            onAction = {}
        )
    }
}
