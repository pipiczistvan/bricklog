package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.collection.domain.repository.CollectionRepository
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.set.domain.model.SetUI
import hu.piware.bricklog.feature.set.domain.model.calculateStatus
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.koin.core.annotation.Single

@Single
class WatchSetUI(
    private val setRepository: SetRepository,
    private val collectionRepository: CollectionRepository,
) {
    operator fun invoke(setId: SetId): Flow<SetUI> {
        val setFlow = setRepository.watchSet(setId)
        val collectionsFlow = collectionRepository.watchCollectionsBySet(setId)

        return combine(setFlow, collectionsFlow) { set, collections ->
            SetUI(
                set = set,
                collections = collections,
                status = set.calculateStatus()
            )
        }
    }
}
