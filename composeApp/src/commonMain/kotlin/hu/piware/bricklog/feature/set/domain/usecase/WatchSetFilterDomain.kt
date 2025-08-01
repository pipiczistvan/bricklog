package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.collection.domain.usecase.WatchCollections
import hu.piware.bricklog.feature.set.domain.model.SetFilterDomain
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.koin.core.annotation.Single

@Single
class WatchSetFilterDomain(
    private val setRepository: SetRepository,
    private val watchCollections: WatchCollections,
) {
    operator fun invoke(): Flow<SetFilterDomain> {
        val themesFlow = setRepository.watchThemes()
        val packagingTypesFlow = setRepository.watchPackagingTypes()
        val collectionsFlow = watchCollections()

        return combine(
            themesFlow,
            packagingTypesFlow,
            collectionsFlow,
        ) { themes, packagingTypes, collections ->
            SetFilterDomain(
                themes = themes,
                packagingTypes = packagingTypes,
                collections = collections,
            )
        }
    }
}
