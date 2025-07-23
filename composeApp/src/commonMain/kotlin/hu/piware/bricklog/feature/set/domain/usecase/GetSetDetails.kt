package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.model.buildSetQueryOptions
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import hu.piware.bricklog.feature.set.domain.util.parseQueries
import hu.piware.bricklog.feature.settings.domain.model.SetFilterPreferences
import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository
import hu.piware.bricklog.util.asResultOrDefault
import hu.piware.bricklog.util.firstOrDefault
import org.koin.core.annotation.Single

@Single
class GetSetDetails(
    private val setRepository: SetRepository,
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(
        filterOverrides: SetFilter? = null,
        query: String = "",
    ): Result<List<SetDetails>, DataError> {
        val parsedQueries = query.parseQueries()
        val filterPreferences = settingsRepository.watchSetFilterPreferences()
            .firstOrDefault { SetFilterPreferences() }

        return setRepository.watchSetDetails(
            buildSetQueryOptions(
                filter = filterOverrides,
                preferences = filterPreferences,
                queries = parsedQueries
            )
        ).asResultOrDefault { emptyList() }
    }
}
