package hu.piware.bricklog.feature.set.data.repository

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.FlowForResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.flowForResult
import hu.piware.bricklog.feature.currency.domain.model.CurrencyRate
import hu.piware.bricklog.feature.currency.domain.model.UpdateRatesProgress
import hu.piware.bricklog.feature.currency.domain.repository.CurrencyRepository
import hu.piware.bricklog.feature.set.domain.model.FileUploadResult
import hu.piware.bricklog.mock.PreviewData
import kotlinx.coroutines.flow.Flow

class FakeCurrencyRepository : CurrencyRepository {

    private var currencyRates = listOf<CurrencyRate>()

    override fun updateCurrencyRatesWithProgress(
        baseCurrencyCode: String,
        fileUploads: List<FileUploadResult>,
    ): FlowForResult<Unit, UpdateRatesProgress> {
        return flowForResult {
            currencyRates = PreviewData.currencyRates
            Result.Success(Unit)
        }
    }

    override suspend fun getCurrencyRateCount(baseCurrencyCode: String): Result<Int, DataError> {
        TODO("Not yet implemented")
    }

    override fun watchCurrencyRates(baseCurrencyCode: String): Flow<List<CurrencyRate>> {
        TODO("Not yet implemented")
    }
}
