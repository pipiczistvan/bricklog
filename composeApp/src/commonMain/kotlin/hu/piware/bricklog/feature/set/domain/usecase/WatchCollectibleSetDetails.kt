@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.currency.domain.usecase.WatchCurrencyPreferenceDetails
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetQueryOptions
import hu.piware.bricklog.feature.set.domain.model.SetSortOption
import hu.piware.bricklog.feature.set.domain.repository.DataServiceRepository
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import org.koin.core.annotation.Single

@Single
class WatchCollectibleSetDetails(
    private val dataServiceRepository: DataServiceRepository,
    private val setRepository: SetRepository,
    private val watchCurrencyPreferenceDetails: WatchCurrencyPreferenceDetails,
    private val sessionManager: SessionManager,
) {
    operator fun invoke(): Flow<List<SetDetails>> {
        val userIdFlow = sessionManager.userId
        val currencyDetailsFlow = watchCurrencyPreferenceDetails()
        val collectiblesFlow = dataServiceRepository.watchCollectibles()

        return combine(
            userIdFlow,
            currencyDetailsFlow,
            collectiblesFlow,
        ) { userId, currencyDetails, collectibles ->
            SetQueryOptions(
                userId = userId,
                currencyDetails = currencyDetails,
                setIds = collectibles.map { it.setId },
                sortOption = SetSortOption.APPEARANCE_DATE_ASCENDING,
            )
        }.flatMapLatest { queryOptions ->
            setRepository.watchSetDetails(queryOptions)
        }
    }
}
