@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.currency.domain.usecase.WatchCurrencyPreferenceDetails
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.model.buildSetQueryOptions
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import hu.piware.bricklog.feature.settings.domain.usecase.WatchSetFilterPreferences
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import org.koin.core.annotation.Single

@Single
class WatchSetDetailsByPreferences(
    private val setRepository: SetRepository,
    private val watchSetFilterPreferences: WatchSetFilterPreferences,
    private val watchCurrencyPreferenceDetails: WatchCurrencyPreferenceDetails,
    private val sessionManager: SessionManager,
) {
    operator fun invoke(
        filterOverrides: SetFilter? = null,
    ): Flow<List<SetDetails>> {
        val userIdFlow = sessionManager.userId
        val currencyDetailsFlow = watchCurrencyPreferenceDetails()
        val setFilterPreferencesFlow = watchSetFilterPreferences()

        return combine(
            userIdFlow,
            currencyDetailsFlow,
            setFilterPreferencesFlow,
        ) { userId, currencyDetails, filterPreferences ->
            buildSetQueryOptions(
                filter = filterOverrides,
                preferences = filterPreferences,
                userId = userId,
                currencyDetails = currencyDetails,
            )
        }
            .distinctUntilChanged()
            .flatMapLatest { queryOptions ->
                setRepository.watchSetDetails(queryOptions)
            }
    }
}
