package hu.piware.bricklog.feature.set.data.repository

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.datasource.LocalUpdateInfoDataSource
import hu.piware.bricklog.feature.set.domain.model.DataType
import hu.piware.bricklog.feature.set.domain.model.UpdateInfo
import hu.piware.bricklog.feature.set.domain.repository.UpdateInfoRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class OfflineFirstUpdateInfoRepository(
    private val localDataSource: LocalUpdateInfoDataSource,
) : UpdateInfoRepository {

    override suspend fun getUpdateInfo(
        type: DataType,
        setId: Int?,
    ): Result<UpdateInfo?, DataError> {
        return localDataSource.getUpdateInfo(type, setId)
    }

    override fun watchUpdateInfo(type: DataType, setId: Int?): Flow<UpdateInfo?> {
        return localDataSource.watchUpdateInfo(type, setId)
    }

    override suspend fun storeUpdateInfo(updateInfo: UpdateInfo): EmptyResult<DataError> {
        return localDataSource.storeUpdateInfo(updateInfo)
    }
}
