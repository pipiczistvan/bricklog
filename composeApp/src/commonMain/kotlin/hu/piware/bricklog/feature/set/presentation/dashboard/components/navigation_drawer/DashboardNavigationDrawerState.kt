package hu.piware.bricklog.feature.set.presentation.dashboard.components.navigation_drawer

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.set.domain.model.UpdateInfo
import hu.piware.bricklog.feature.user.domain.manager.SessionManager.Companion.GUEST_USER
import hu.piware.bricklog.feature.user.domain.model.User

data class DashboardNavigationDrawerState(
    val currentUser: User = GUEST_USER,
    val collections: List<Collection> = emptyList(),
    val setUpdateInfo: UpdateInfo? = null,
)
