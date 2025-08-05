package hu.piware.bricklog.feature.currency.domain.datasource

import hu.piware.bricklog.feature.core.domain.FlowForResult
import hu.piware.bricklog.feature.currency.domain.model.UpdateRatesProgress

interface RemoteCurrencyDataSource {
    fun downloadCompressedCsv(url: String): FlowForResult<ByteArray, UpdateRatesProgress>
}
