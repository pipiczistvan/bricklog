package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.set.domain.model.SetQueryOptions
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class WatchSetDetailsById(
    private val setRepository: SetRepository,
) {
    operator fun invoke(setId: SetId): Flow<SetDetails> {
        return setRepository.watchSetDetails(SetQueryOptions(setIds = setOf(setId)))
            .map { it.first() }
    }
}
