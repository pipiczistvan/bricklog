package hu.piware.bricklog.feature.settings.data.datastore

import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.settings.domain.model.SetFilterPreferences

fun SetFilter.toPreferences(): SetFilterPreferences {
    return SetFilterPreferences(
        sortOption = sortOption,
        launchDate = launchDate,
        themes = themes,
        status = status,
        showIncomplete = showIncomplete
    )
}

fun SetFilterPreferences.toDomainModel(): SetFilter {
    return SetFilter(
        sortOption = sortOption,
        launchDate = launchDate,
        themes = themes,
        status = status,
        showIncomplete = showIncomplete
    )
}
