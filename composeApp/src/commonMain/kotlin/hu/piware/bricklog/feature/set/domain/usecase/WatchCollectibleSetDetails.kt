@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetQueryOptions
import hu.piware.bricklog.feature.set.domain.model.SetSortOption
import hu.piware.bricklog.feature.set.domain.repository.DataServiceRepository
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import org.koin.core.annotation.Single

@Single
class WatchCollectibleSetDetails(
    private val dataServiceRepository: DataServiceRepository,
    private val setRepository: SetRepository,
) {
    operator fun invoke(): Flow<List<SetDetails>> {
        return dataServiceRepository.watchCollectibles().flatMapLatest { collectibles ->
            setRepository.watchSetDetails(
                SetQueryOptions(
                    setIds = collectibles.map { it.setId },
                    sortOption = SetSortOption.APPEARANCE_DATE_ASCENDING,
                ),
            )
        }
    }
}
