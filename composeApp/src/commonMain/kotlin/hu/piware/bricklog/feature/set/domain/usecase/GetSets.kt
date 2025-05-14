package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.Set
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.model.buildSetQueryOptions
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import hu.piware.bricklog.feature.set.domain.util.parseQueries
import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.first

class GetSets(
    private val setRepository: SetRepository,
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(
        filterOverrides: SetFilter? = null,
        query: String = "",
    ): Result<List<Set>, DataError> {
        val parsedQueries = query.parseQueries()
        val filterPreferences = settingsRepository.setFilterPreferences.first()

        return setRepository.getSets(
            buildSetQueryOptions(
                filter = filterOverrides,
                preferences = filterPreferences,
                queries = parsedQueries
            )
        )
    }
}
