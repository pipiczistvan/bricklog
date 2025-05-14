package hu.piware.bricklog.feature.set.presentation.dashboard.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.dashboard_navigation_drawer_item_about
import bricklog.composeapp.generated.resources.dashboard_navigation_drawer_item_appearance
import bricklog.composeapp.generated.resources.dashboard_navigation_drawer_item_favourite_sets
import bricklog.composeapp.generated.resources.dashboard_navigation_drawer_item_notification_settings
import bricklog.composeapp.generated.resources.dashboard_navigation_drawer_item_reset_sets
import bricklog.composeapp.generated.resources.dashboard_navigation_drawer_section_collections
import bricklog.composeapp.generated.resources.dashboard_navigation_drawer_section_developer
import bricklog.composeapp.generated.resources.dashboard_navigation_drawer_section_settings
import hu.piware.bricklog.BuildKonfig
import hu.piware.bricklog.feature.set.presentation.dashboard.DashboardAction
import hu.piware.bricklog.feature.set.presentation.dashboard.utils.favouriteSetsFilter
import hu.piware.bricklog.feature.set.presentation.set_list.SetListArguments
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

@Composable
fun DashboardNavigationDrawerContent(
    state: DrawerState,
    onAction: (DashboardAction) -> Unit,
) {
    val scope = rememberCoroutineScope()

    ModalDrawerSheet {
        Spacer(modifier = Modifier.statusBarsPadding())
        Spacer(modifier = Modifier.height(24.dp))

        navigationSections.forEachIndexed { index, section ->
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                text = stringResource(section.titleRes),
                style = MaterialTheme.typography.titleMedium
            )
            section.items.forEach { item ->
                NavigationDrawerItem(
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    label = { Text(stringResource(item.titleRes)) },
                    selected = false,
                    onClick = {
                        scope.launch {
                            onAction(item.action())
                            state.close()
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = item.unselectedIcon,
                            contentDescription = stringResource(item.titleRes)
                        )
                    }
                )
            }
            if (index != navigationSections.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

private data class NavigationSection(
    val titleRes: StringResource,
    val items: List<NavigationItem>,
)

private data class NavigationItem(
    val titleRes: StringResource,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null,
    val action: suspend () -> DashboardAction,
)

private val collectionsSection = NavigationSection(
    titleRes = Res.string.dashboard_navigation_drawer_section_collections,
    items = listOf(
        NavigationItem(
            titleRes = Res.string.dashboard_navigation_drawer_item_favourite_sets,
            selectedIcon = Icons.Filled.Star,
            unselectedIcon = Icons.Outlined.StarOutline,
            action = {
                DashboardAction.OnSearchSets(
                    SetListArguments(
                        filterOverrides = favouriteSetsFilter,
                        title = getString(Res.string.dashboard_navigation_drawer_item_favourite_sets)
                    )
                )
            }
        )
    )
)

private val settingsSection = NavigationSection(
    titleRes = Res.string.dashboard_navigation_drawer_section_settings,
    items = listOf(
        NavigationItem(
            titleRes = Res.string.dashboard_navigation_drawer_item_notification_settings,
            selectedIcon = Icons.Filled.Notifications,
            unselectedIcon = Icons.Outlined.Notifications,
            action = { DashboardAction.OnNotificationSettingsClick }
        ),
        NavigationItem(
            titleRes = Res.string.dashboard_navigation_drawer_item_appearance,
            selectedIcon = Icons.Filled.Palette,
            unselectedIcon = Icons.Outlined.Palette,
            action = { DashboardAction.OnAppearanceClick }
        ),
        NavigationItem(
            titleRes = Res.string.dashboard_navigation_drawer_item_about,
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Info,
            action = { DashboardAction.OnAboutClick }
        )
    )
)

private val developerSection = NavigationSection(
    titleRes = Res.string.dashboard_navigation_drawer_section_developer,
    items = listOf(
        NavigationItem(
            titleRes = Res.string.dashboard_navigation_drawer_item_reset_sets,
            selectedIcon = Icons.Filled.Restore,
            unselectedIcon = Icons.Outlined.Restore,
            action = { DashboardAction.OnResetSets }
        )
    )
)

private val navigationSections = listOf(
    collectionsSection,
    settingsSection,
).let {
    if (BuildKonfig.DEV_MODE) it + developerSection else it
}
