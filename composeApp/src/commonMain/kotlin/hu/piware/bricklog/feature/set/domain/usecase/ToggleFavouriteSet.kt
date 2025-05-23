package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.set.domain.repository.SetPreferencesRepository
import kotlinx.coroutines.flow.firstOrNull
import org.koin.core.annotation.Single

@Single
class ToggleFavouriteSet(
    private val setPreferencesRepository: SetPreferencesRepository
) {
    suspend operator fun invoke(setId: Int): EmptyResult<DataError> {
        val isFavourite = setPreferencesRepository.watchFavouriteSet(setId)
            .firstOrNull() ?: false

        return setPreferencesRepository.setFavouriteSet(setId, !isFavourite)
    }
}
