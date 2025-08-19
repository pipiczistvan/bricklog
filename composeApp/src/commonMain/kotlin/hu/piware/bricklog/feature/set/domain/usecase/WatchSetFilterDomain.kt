package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.collection.domain.usecase.WatchCollectionDetails
import hu.piware.bricklog.feature.currency.domain.usecase.WatchCurrencyPreferenceDetails
import hu.piware.bricklog.feature.set.domain.model.SetFilterDomain
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.koin.core.annotation.Single

@Single
class WatchSetFilterDomain(
    private val setRepository: SetRepository,
    private val watchCollectionDetails: WatchCollectionDetails,
    private val watchCurrencyPreferenceDetails: WatchCurrencyPreferenceDetails,
) {
    operator fun invoke(): Flow<SetFilterDomain> {
        val themesFlow = setRepository.watchThemes()
        val packagingTypesFlow = setRepository.watchPackagingTypes()
        val collectionsFlow = watchCollectionDetails()
        val currencyDetailsFlow = watchCurrencyPreferenceDetails()

        return combine(
            themesFlow,
            packagingTypesFlow,
            collectionsFlow,
            currencyDetailsFlow,
        ) { themes, packagingTypes, collections, currencyDetails ->
            SetFilterDomain(
                themes = themes,
                packagingTypes = packagingTypes,
                collections = collections,
                currencyDetails = currencyDetails,
            )
        }
    }
}
