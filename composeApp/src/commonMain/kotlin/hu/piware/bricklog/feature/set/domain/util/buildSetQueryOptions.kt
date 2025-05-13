package hu.piware.bricklog.feature.set.domain.util

import hu.piware.bricklog.feature.set.domain.model.DateFilter
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.model.SetQueryOptions
import hu.piware.bricklog.feature.settings.domain.model.SetFilterPreferences

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
