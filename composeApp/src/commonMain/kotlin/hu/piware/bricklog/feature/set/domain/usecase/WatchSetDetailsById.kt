package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class WatchSetDetailsById(
    private val setRepository: SetRepository,
) {
    operator fun invoke(setId: SetId): Flow<SetDetails> {
        return setRepository.watchSetDetailsById(setId)
    }
}
