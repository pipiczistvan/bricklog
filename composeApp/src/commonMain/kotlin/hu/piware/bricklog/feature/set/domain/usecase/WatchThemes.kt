package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import kotlinx.coroutines.flow.Flow

class WatchThemes(
    private val setRepository: SetRepository,
) {
    operator fun invoke(): Flow<List<String>> {
        return setRepository.watchThemes()
    }
}
