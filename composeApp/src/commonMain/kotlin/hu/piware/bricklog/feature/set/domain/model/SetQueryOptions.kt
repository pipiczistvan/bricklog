package hu.piware.bricklog.feature.set.domain.model

import hu.piware.bricklog.feature.set.domain.util.parseQueries
import kotlin.collections.Set

data class SetQueryOptions(
    val queries: List<String> = "".parseQueries(),
    val sortOption: SetSortOption = SetSortOption.APPEARANCE_DATE_DESCENDING,
    val launchDate: DateFilter = DateFilter.AnyTime,
    val appearanceDate: DateFilter = DateFilter.AnyTime,
    val themes: Set<String> = emptySet(),
    val packagingTypes: Set<String> = emptySet(),
    val status: StatusFilterOption = StatusFilterOption.ANY_STATUS,
    val showIncomplete: Boolean = false,
    val limit: Int? = null,
    val barcode: String? = null,
    val isFavourite: Boolean = false,
)
