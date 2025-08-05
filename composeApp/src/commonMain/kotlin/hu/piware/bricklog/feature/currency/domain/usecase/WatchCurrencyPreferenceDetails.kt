@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.currency.domain.usecase

import hu.piware.bricklog.feature.currency.domain.model.CurrencyPreferenceDetails
import hu.piware.bricklog.feature.currency.domain.model.CurrencyRate
import hu.piware.bricklog.feature.currency.domain.repository.CurrencyRepository
import hu.piware.bricklog.feature.currency.domain.util.CURRENCY_CODE_EUR
import hu.piware.bricklog.feature.currency.domain.util.CURRENCY_CODE_USD
import hu.piware.bricklog.feature.user.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class WatchCurrencyPreferenceDetails(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val currencyRepository: CurrencyRepository,
) {
    operator fun invoke(): Flow<CurrencyPreferenceDetails> {
        return userPreferencesRepository.watchUserPreferences().flatMapLatest { userPreferences ->
            currencyRepository.watchCurrencyRates(CURRENCY_CODE_EUR).map { currencyRates ->
                CurrencyPreferenceDetails(
                    usdEurRate = currencyRates.findByCode(CURRENCY_CODE_USD)?.rate,
                    preferredRegion = userPreferences.currencyRegion,
                    preferredCurrencyCode = userPreferences.targetCurrencyCode,
                    preferredCurrencyEurRate = currencyRates.findByCode(userPreferences.targetCurrencyCode)?.rate,
                )
            }
        }
    }

    private fun List<CurrencyRate>.findByCode(code: String): CurrencyRate? {
        return find { it.currencyCode == code }
    }
}
