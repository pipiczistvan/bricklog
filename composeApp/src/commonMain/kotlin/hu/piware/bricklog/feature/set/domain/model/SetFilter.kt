package hu.piware.bricklog.feature.set.domain.model

import kotlinx.serialization.Serializable
import kotlin.collections.Set

@Serializable
data class SetFilter(
    val sortOption: SetSortOption = SetSortOption.LAUNCH_DATE_DESCENDING,
    val themes: Set<String> = emptySet(),
    val launchDate: DateFilter = DateFilter.AnyTime,
    val status: StatusFilterOption = StatusFilterOption.ANY_STATUS,
    val limit: Int? = null,
    val showIncomplete: Boolean = false,
    val barcode: String? = null,
    val isFavourite: Boolean = false,
)
