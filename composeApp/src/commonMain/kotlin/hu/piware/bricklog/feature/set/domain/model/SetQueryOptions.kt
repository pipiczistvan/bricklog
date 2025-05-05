package hu.piware.bricklog.feature.set.domain.model

import kotlin.collections.Set

data class SetQueryOptions(
    val queries: List<String>,
    val sortOption: SetSortOption,
    val launchDate: DateFilter,
    val themes: Set<String>,
    val packagingTypes: Set<String>,
    val status: StatusFilterOption,
    val showIncomplete: Boolean,
    val limit: Int?,
    val barcode: String?,
    val isFavourite: Boolean,
)
