package hu.piware.bricklog.feature.set.presentation.dashboard.components.navigation_drawer

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.user.domain.model.User

data class DashboardNavigationDrawerState(
    val currentUser: User? = null,
    val collections: List<Collection> = emptyList(),
)