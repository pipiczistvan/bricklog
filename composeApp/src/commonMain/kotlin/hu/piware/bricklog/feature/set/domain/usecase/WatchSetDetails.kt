@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.model.buildSetQueryOptions
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import hu.piware.bricklog.feature.set.domain.util.parseQueries
import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class WatchSetDetails(
    private val setRepository: SetRepository,
    private val settingsRepository: SettingsRepository,
) {
    operator fun invoke(
        filterOverrides: SetFilter? = null,
        query: String = "",
    ): Flow<List<SetDetails>> {
        val parsedQueries = query.parseQueries()

        return settingsRepository.setFilterPreferences
            .map { filterPreferences ->
                buildSetQueryOptions(filterOverrides, filterPreferences, parsedQueries)
            }
            .distinctUntilChanged()
            .flatMapLatest { queryOptions ->
                setRepository.watchSetDetails(queryOptions)
            }
    }
}
