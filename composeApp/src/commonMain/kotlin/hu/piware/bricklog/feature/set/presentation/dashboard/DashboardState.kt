package hu.piware.bricklog.feature.set.presentation.dashboard

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.settings.domain.model.Changelog

data class DashboardState(
    val latestSets: List<SetDetails> = emptyList(),
    val latestReleases: List<SetDetails> = emptyList(),
    val arrivingSets: List<SetDetails> = emptyList(),
    val retiringSets: List<SetDetails> = emptyList(),
    val collections: List<Collection> = emptyList(),
    val areSetsRefreshing: Boolean = false,
    val changelog: Changelog? = null,
)
