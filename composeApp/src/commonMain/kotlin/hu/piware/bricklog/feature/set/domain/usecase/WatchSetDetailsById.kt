@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.currency.domain.usecase.WatchCurrencyPreferenceDetails
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.set.domain.model.SetQueryOptions
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class WatchSetDetailsById(
    private val setRepository: SetRepository,
    private val watchCurrencyPreferenceDetails: WatchCurrencyPreferenceDetails,
    private val sessionManager: SessionManager,
) {
    operator fun invoke(setId: SetId): Flow<SetDetails> {
        val userIdFlow = sessionManager.userId
        val currencyDetailsFlow = watchCurrencyPreferenceDetails()

        return combine(userIdFlow, currencyDetailsFlow) { userId, currencyDetails ->
            SetQueryOptions(
                userId = userId,
                setIds = listOf(setId),
                currencyDetails = currencyDetails,
            )
        }
            .flatMapLatest { setRepository.watchSetDetails(it) }
            .map { it.first() }
    }
}
