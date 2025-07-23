package hu.piware.bricklog.feature.set.domain.repository

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.set.domain.model.DataType
import hu.piware.bricklog.feature.set.domain.model.UpdateInfo
import kotlinx.coroutines.flow.Flow

interface UpdateInfoRepository {

    fun watchUpdateInfo(type: DataType, setId: Int?): Flow<UpdateInfo?>

    suspend fun saveUpdateInfo(updateInfo: UpdateInfo): EmptyResult<DataError>
}
