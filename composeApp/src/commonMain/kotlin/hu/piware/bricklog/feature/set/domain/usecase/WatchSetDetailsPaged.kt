@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.set.domain.usecase

import androidx.paging.PagingData
import hu.piware.bricklog.feature.currency.domain.usecase.WatchCurrencyPreferenceDetails
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.model.buildSetQueryOptions
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import hu.piware.bricklog.feature.set.domain.util.parseQueries
import hu.piware.bricklog.feature.settings.domain.usecase.WatchSetFilterPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import org.koin.core.annotation.Single

@Single
class WatchSetDetailsPaged(
    private val setRepository: SetRepository,
    private val watchSetFilterPreferences: WatchSetFilterPreferences,
    private val watchCurrencyPreferenceDetails: WatchCurrencyPreferenceDetails,
) {
    operator fun invoke(
        filterOverrides: SetFilter? = null,
        query: String = "",
    ): Flow<PagingData<SetDetails>> {
        val parsedQueries = query.parseQueries()
        val currencyDetailsFlow = watchCurrencyPreferenceDetails()
        val setFilterPreferencesFlow = watchSetFilterPreferences()

        return combine(
            currencyDetailsFlow,
            setFilterPreferencesFlow,
        ) { currencyDetails, filterPreferences ->
            buildSetQueryOptions(
                filterOverrides,
                filterPreferences,
                currencyDetails,
                parsedQueries,
            )
        }
            .distinctUntilChanged()
            .flatMapLatest { queryOptions ->
                setRepository.watchSetDetailsPaged(queryOptions)
            }
    }
}
