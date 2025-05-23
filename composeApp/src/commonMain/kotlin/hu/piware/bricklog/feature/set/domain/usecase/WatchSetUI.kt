package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.set.domain.model.SetUI
import hu.piware.bricklog.feature.set.domain.model.calculateStatus
import hu.piware.bricklog.feature.set.domain.repository.SetPreferencesRepository
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.koin.core.annotation.Single

@Single
class WatchSetUI(
    private val setRepository: SetRepository,
    private val setPreferencesRepository: SetPreferencesRepository,
) {
    operator fun invoke(id: Int): Flow<SetUI> {
        val setFlow = setRepository.watchSet(id)
        val isFavouriteFlow = setPreferencesRepository.watchFavouriteSet(id)

        return combine(setFlow, isFavouriteFlow) { set, isFavourite ->
            SetUI(
                set = set,
                isFavourite = isFavourite,
                status = set.calculateStatus()
            )
        }
    }
}
