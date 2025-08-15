@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.currency.domain.usecase.WatchCurrencyPreferenceDetails
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.set.domain.model.SetQueryOptions
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class WatchSetDetailsById(
    private val setRepository: SetRepository,
    private val watchCurrencyPreferenceDetails: WatchCurrencyPreferenceDetails,
) {
    operator fun invoke(setId: SetId): Flow<SetDetails> {
        return watchCurrencyPreferenceDetails()
            .flatMapLatest { currencyDetails ->
                setRepository.watchSetDetails(
                    SetQueryOptions(
                        setIds = listOf(setId),
                        currencyDetails = currencyDetails,
                    ),
                )
            }
            .map { it.first() }
    }
}
