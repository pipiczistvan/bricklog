package hu.piware.bricklog.feature.set.domain.model

import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.settings.domain.model.SetFilterPreferences

data class SetQueryOptions(
    val queries: List<String> = emptyList(),
    val sortOption: SetSortOption = SetSortOption.APPEARANCE_DATE_DESCENDING,
    val launchDate: DateFilter = DateFilter.AnyTime,
    val appearanceDate: DateFilter = DateFilter.AnyTime,
    val themes: List<String> = emptyList(),
    val packagingTypes: List<String> = emptyList(),
    val statuses: List<SetStatus> = emptyList(),
    val showIncomplete: Boolean = false,
    val limit: Int? = null,
    val barcode: String? = null,
    val collectionIds: List<CollectionId> = emptyList(),
    val setIds: List<SetId> = emptyList(),
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
        statuses = filter?.statuses ?: preferences.statuses,
        showIncomplete = filter?.showIncomplete ?: preferences.showIncomplete,
        limit = filter?.limit,
        barcode = filter?.barcode,
        collectionIds = filter?.collectionIds ?: preferences.collectionIds,
        setIds = filter?.setIds ?: emptyList(),
    )
}
