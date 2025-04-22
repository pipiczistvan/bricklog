package hu.piware.bricklog.feature.set.domain.repository

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.DataType
import hu.piware.bricklog.feature.set.domain.model.UpdateInfo
import kotlinx.coroutines.flow.Flow

interface UpdateInfoRepository {
    suspend fun getUpdateInfo(type: DataType): Result<UpdateInfo?, DataError>
    fun watchUpdateInfo(type: DataType): Flow<UpdateInfo?>
    suspend fun storeUpdateInfo(updateInfo: UpdateInfo): EmptyResult<DataError>
}
