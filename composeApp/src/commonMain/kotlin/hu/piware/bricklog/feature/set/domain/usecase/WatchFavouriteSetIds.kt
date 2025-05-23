package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.set.domain.repository.SetPreferencesRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class WatchFavouriteSetIds(
    private val setPreferencesRepository: SetPreferencesRepository,
) {
    operator fun invoke(): Flow<List<Int>> {
        return setPreferencesRepository.watchFavouriteSetIds()
    }
}
