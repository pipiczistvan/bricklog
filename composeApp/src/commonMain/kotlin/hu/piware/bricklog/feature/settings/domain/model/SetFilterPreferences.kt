package hu.piware.bricklog.feature.settings.domain.model

import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.set.domain.model.DateFilter
import hu.piware.bricklog.feature.set.domain.model.SetSortOption
import hu.piware.bricklog.feature.set.domain.model.SetStatus
import kotlinx.serialization.Serializable

@Serializable
data class SetFilterPreferences(
    val sortOption: SetSortOption = SetSortOption.APPEARANCE_DATE_DESCENDING,
    val launchDate: DateFilter = DateFilter.AnyTime,
    val themes: Set<String> = emptySet(),
    val packagingTypes: Set<String> = setOf("Box"),
    val statuses: Set<SetStatus> = emptySet(),
    val showIncomplete: Boolean = false,
    val collectionIds: Set<CollectionId> = emptySet(),
)
