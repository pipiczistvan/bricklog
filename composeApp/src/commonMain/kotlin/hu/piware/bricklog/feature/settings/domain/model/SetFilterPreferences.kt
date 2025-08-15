package hu.piware.bricklog.feature.settings.domain.model

import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.set.domain.model.DateFilter
import hu.piware.bricklog.feature.set.domain.model.PriceFilter
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.model.SetSortOption
import hu.piware.bricklog.feature.set.domain.model.SetStatus
import kotlinx.serialization.Serializable

@Serializable
data class SetFilterPreferences(
    val sortOption: SetSortOption = SetSortOption.APPEARANCE_DATE_DESCENDING,
    val launchDate: DateFilter = DateFilter.AnyTime,
    val themes: List<String> = emptyList(),
    val packagingTypes: List<String> = listOf("Box"),
    val statuses: List<SetStatus> = emptyList(),
    val price: PriceFilter = PriceFilter.AnyPrice,
    val showIncomplete: Boolean = false,
    val collectionIds: List<CollectionId> = emptyList(),
)

val DEFAULT_SET_FILTER_PREFERENCES = SetFilterPreferences()

fun SetFilterPreferences.toSetFilter(): SetFilter {
    return SetFilter(
        sortOption = sortOption,
        launchDate = launchDate,
        themes = themes,
        packagingTypes = packagingTypes,
        statuses = statuses,
        price = price,
        showIncomplete = showIncomplete,
        collectionIds = collectionIds,
    )
}
