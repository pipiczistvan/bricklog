package hu.piware.bricklog.feature.set.presentation.dashboard

import hu.piware.bricklog.feature.set.domain.model.SetUI
import kotlinx.datetime.Instant

data class DashboardState(
    val latestSets: List<SetUI> = emptyList(),
    val retiringSets: List<SetUI> = emptyList(),
    val areSetsRefreshing: Boolean = false,
    val lastUpdated: Instant? = null,
)
