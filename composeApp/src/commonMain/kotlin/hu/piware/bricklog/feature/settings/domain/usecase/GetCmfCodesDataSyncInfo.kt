package hu.piware.bricklog.feature.settings.domain.usecase

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.set.domain.model.DataType
import hu.piware.bricklog.feature.set.domain.repository.DataServiceRepository
import hu.piware.bricklog.feature.set.domain.repository.UpdateInfoRepository
import hu.piware.bricklog.feature.settings.domain.model.DataSyncInfo
import hu.piware.bricklog.util.asResult
import org.koin.core.annotation.Single

@Single
class GetCmfCodesDataSyncInfo(
    private val dataServiceRepository: DataServiceRepository,
    private val updateInfoRepository: UpdateInfoRepository,
) {
    suspend operator fun invoke(): Result<DataSyncInfo, DataError> {
        val exportInfo = dataServiceRepository.getCmfCodesExportInfo()
            .onError { return it }
            .data()
        val updateInfo = updateInfoRepository.watchUpdateInfo(DataType.CMF_CODES)
            .asResult()
            .onError { return it }
            .data()

        return Result.Success(
            DataSyncInfo(
                exportDate = exportInfo.lastUpdated,
                updateDate = updateInfo.lastUpdated,
            ),
        )
    }
}
