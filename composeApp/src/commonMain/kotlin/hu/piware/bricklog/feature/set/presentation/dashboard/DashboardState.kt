package hu.piware.bricklog.feature.set.presentation.dashboard

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.set.domain.model.SetUI
import hu.piware.bricklog.feature.settings.domain.model.Changelog
import kotlinx.datetime.Instant

data class DashboardState(
    val latestSets: List<SetUI> = emptyList(),
    val arrivingSets: List<SetUI> = emptyList(),
    val retiringSets: List<SetUI> = emptyList(),
    val collections: List<Collection> = emptyList(),
    val areSetsRefreshing: Boolean = false,
    val lastUpdated: Instant? = null,
    val changelog: Changelog? = null,
)
