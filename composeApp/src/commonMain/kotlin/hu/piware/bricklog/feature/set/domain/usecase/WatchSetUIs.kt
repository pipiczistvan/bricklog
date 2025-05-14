@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.model.SetUI
import hu.piware.bricklog.feature.set.domain.model.buildSetQueryOptions
import hu.piware.bricklog.feature.set.domain.model.calculateStatus
import hu.piware.bricklog.feature.set.domain.repository.SetPreferencesRepository
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import hu.piware.bricklog.feature.set.domain.util.parseQueries
import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class WatchSetUIs(
    private val setRepository: SetRepository,
    private val settingsRepository: SettingsRepository,
    setPreferencesRepository: SetPreferencesRepository,
) {
    private val favouriteSetIds = setPreferencesRepository.watchFavouriteSetIds()

    operator fun invoke(filterOverrides: SetFilter? = null, query: String = ""): Flow<List<SetUI>> {
        val parsedQueries = query.parseQueries()

        val setsFlow = settingsRepository.setFilterPreferences
            .map { filterPreferences ->
                buildSetQueryOptions(filterOverrides, filterPreferences, parsedQueries)
            }
            .distinctUntilChanged()
            .flatMapLatest { queryOptions ->
                setRepository.watchSets(queryOptions)
            }

        return combine(setsFlow, favouriteSetIds) { sets, favouriteSetIds ->
            sets.map { set ->
                SetUI(
                    set = set,
                    isFavourite = favouriteSetIds.contains(set.setID),
                    status = set.calculateStatus()
                )
            }
        }
    }
}
