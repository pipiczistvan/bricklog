package hu.piware.bricklog.feature.set.presentation.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.dashboard_navigation_drawer_item_about
import bricklog.composeapp.generated.resources.dashboard_navigation_drawer_item_appearance
import bricklog.composeapp.generated.resources.dashboard_navigation_drawer_item_notification_settings
import bricklog.composeapp.generated.resources.dashboard_navigation_drawer_item_reset_sets
import bricklog.composeapp.generated.resources.dashboard_navigation_drawer_section_collections
import bricklog.composeapp.generated.resources.dashboard_navigation_drawer_section_developer
import bricklog.composeapp.generated.resources.dashboard_navigation_drawer_section_settings
import hu.piware.bricklog.BuildKonfig
import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.model.StatusFilterOption
import hu.piware.bricklog.feature.set.presentation.dashboard.DashboardAction
import hu.piware.bricklog.feature.set.presentation.set_list.SetListArguments
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun DashboardNavigationDrawerContent(
    state: DrawerState,
    collections: List<Collection>,
    onAction: (DashboardAction) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val navigationSections = buildNavigationSections(collections)

    ModalDrawerSheet {
        Spacer(modifier = Modifier.statusBarsPadding())
        Spacer(modifier = Modifier.height(24.dp))

        navigationSections.forEachIndexed { index, section ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    text = section.title,
                    style = MaterialTheme.typography.titleMedium
                )
                if (section.action != null && section.actionIcon != null) {
                    IconButton(
                        onClick = {
                            onAction(section.action)
                        }
                    ) {
                        Icon(
                            imageVector = section.actionIcon,
                            contentDescription = null
                        )
                    }
                }
            }
            section.items.forEach { item ->
                NavigationDrawerItem(
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    label = { Text(item.title) },
                    selected = false,
                    onClick = {
                        scope.launch {
                            onAction(item.action)
                            state.close()
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = item.unselectedIcon,
                            contentDescription = item.title
                        )
                    },
                    badge = {
                        if (item.trailingIcon != null && item.trailingIconAction != null) {
                            IconButton(
                                onClick = {
                                    onAction(item.trailingIconAction)
                                }
                            ) {
                                Icon(
                                    imageVector = item.trailingIcon,
                                    contentDescription = null
                                )
                            }
                        }
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
    val title: String,
    val items: List<NavigationItem>,
    val action: DashboardAction? = null,
    val actionIcon: ImageVector? = null,
)

private data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null,
    val action: DashboardAction,
    val trailingIcon: ImageVector? = null,
    val trailingIconAction: DashboardAction? = null,
)

@Composable
private fun buildNavigationSections(collections: List<Collection>): List<NavigationSection> {
    return listOf(
        buildCollectionsSection(collections),
        buildSettingsSection(),
    ).let {
        if (BuildKonfig.DEV_MODE) it + buildDeveloperSection() else it
    }
}

@Composable
private fun buildCollectionsSection(collections: List<Collection>): NavigationSection {
    return NavigationSection(
        title = stringResource(Res.string.dashboard_navigation_drawer_section_collections),
        action = DashboardAction.OnCollectionEditClick(0),
        actionIcon = Icons.Outlined.Add,
        items = collections.map { collection ->
            NavigationItem(
                title = collection.name,
                selectedIcon = collection.icon.filledIcon,
                unselectedIcon = collection.icon.outlinedIcon,
                action = DashboardAction.OnSearchSets(
                    SetListArguments(
                        filterOverrides = SetFilter(
                            status = StatusFilterOption.ANY_STATUS,
                            collectionIds = setOf(collection.id)
                        ),
                        title = collection.name
                    )
                ),
                trailingIcon = Icons.Outlined.Edit,
                trailingIconAction = DashboardAction.OnCollectionEditClick(collection.id)
            )
        }
    )
}

@Composable
private fun buildSettingsSection(): NavigationSection {
    return NavigationSection(
        title = stringResource(Res.string.dashboard_navigation_drawer_section_settings),
        items = listOf(
            NavigationItem(
                title = stringResource(Res.string.dashboard_navigation_drawer_item_notification_settings),
                selectedIcon = Icons.Filled.Notifications,
                unselectedIcon = Icons.Outlined.Notifications,
                action = DashboardAction.OnNotificationSettingsClick
            ),
            NavigationItem(
                title = stringResource(Res.string.dashboard_navigation_drawer_item_appearance),
                selectedIcon = Icons.Filled.Palette,
                unselectedIcon = Icons.Outlined.Palette,
                action = DashboardAction.OnAppearanceClick
            ),
            NavigationItem(
                title = stringResource(Res.string.dashboard_navigation_drawer_item_about),
                selectedIcon = Icons.Filled.Info,
                unselectedIcon = Icons.Outlined.Info,
                action = DashboardAction.OnAboutClick
            )
        )
    )
}

@Composable
private fun buildDeveloperSection(): NavigationSection {
    return NavigationSection(
        title = stringResource(Res.string.dashboard_navigation_drawer_section_developer),
        items = listOf(
            NavigationItem(
                title = stringResource(Res.string.dashboard_navigation_drawer_item_reset_sets),
                selectedIcon = Icons.Filled.Restore,
                unselectedIcon = Icons.Outlined.Restore,
                action = DashboardAction.OnResetSets
            )
        )
    )
}
