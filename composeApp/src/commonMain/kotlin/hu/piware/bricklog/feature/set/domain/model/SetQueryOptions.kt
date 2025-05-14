package hu.piware.bricklog.feature.set.domain.model

import hu.piware.bricklog.feature.set.domain.util.parseQueries
import hu.piware.bricklog.feature.settings.domain.model.SetFilterPreferences
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

fun buildSetQueryOptions(
    filter: SetFilter?,
    preferences: SetFilterPreferences,
    queries: List<String>,
): SetQueryOptions {
    return SetQueryOptions(
        queries = queries,
        sortOption = filter?.sortOption ?: preferences.sortOption,
        launchDate = filter?.launchDate ?: preferences.launchDate,
        appearanceDate = filter?.appearanceDate ?: DateFilter.AnyTime,
        themes = filter?.themes ?: preferences.themes,
        packagingTypes = filter?.packagingTypes ?: preferences.packagingTypes,
        status = filter?.status ?: preferences.status,
        showIncomplete = filter?.showIncomplete ?: preferences.showIncomplete,
        limit = filter?.limit,
        barcode = filter?.barcode,
        isFavourite = filter?.isFavourite ?: false
    )
}
