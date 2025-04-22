package hu.piware.bricklog.feature.set.domain.usecase

import androidx.paging.PagingData
import hu.piware.bricklog.feature.set.domain.model.Set
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import hu.piware.bricklog.feature.set.domain.util.removeUnnecessaryWhitespace
import kotlinx.coroutines.flow.Flow

class WatchSetsPaged(
    private val setRepository: SetRepository,
) {
    operator fun invoke(filter: SetFilter, query: String = ""): Flow<PagingData<Set>> {
        val parsedQueries = query.split(",")
            .map { it.removeUnnecessaryWhitespace() }
            .filterNot { it.isBlank() }
            .ifEmpty { listOf("") }

        return setRepository.watchSetsPaged(parsedQueries, filter)
    }
}
