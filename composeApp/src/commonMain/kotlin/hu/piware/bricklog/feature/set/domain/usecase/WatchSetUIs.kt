package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.model.SetUI
import hu.piware.bricklog.feature.set.domain.model.calculateStatus
import hu.piware.bricklog.feature.set.domain.repository.SetPreferencesRepository
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import hu.piware.bricklog.feature.set.domain.util.removeUnnecessaryWhitespace
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class WatchSetUIs(
    private val setRepository: SetRepository,
    setPreferencesRepository: SetPreferencesRepository,
) {
    private val favouriteSetIds = setPreferencesRepository.watchFavouriteSetIds()

    operator fun invoke(filter: SetFilter, query: String = ""): Flow<List<SetUI>> {
        val parsedQueries = query.split(",")
            .map { it.removeUnnecessaryWhitespace() }
            .filterNot { it.isBlank() }
            .ifEmpty { listOf("") }

        val setsFlow = setRepository.watchSets(parsedQueries, filter)

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
