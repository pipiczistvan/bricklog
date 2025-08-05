package hu.piware.bricklog.feature.currency.domain.usecase

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.currency.domain.repository.CurrencyRepository
import hu.piware.bricklog.feature.currency.domain.util.CURRENCY_CODE_EUR
import org.koin.core.annotation.Single

@Single
class HasEurRates(
    private val currencyRepository: CurrencyRepository,
) {
    suspend operator fun invoke(): Result<Boolean, DataError> {
        val count = currencyRepository.getCurrencyRateCount(CURRENCY_CODE_EUR)
            .onError { return@invoke it }
            .data()

        return Result.Success(count > 0)
    }
}
