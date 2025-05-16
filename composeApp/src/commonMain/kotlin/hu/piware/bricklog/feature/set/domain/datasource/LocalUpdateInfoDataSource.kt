package hu.piware.bricklog.feature.set.domain.datasource

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.DataType
import hu.piware.bricklog.feature.set.domain.model.UpdateInfo
import kotlinx.coroutines.flow.Flow

interface LocalUpdateInfoDataSource {

    suspend fun getUpdateInfo(type: DataType, setId: Int?): Result<UpdateInfo?, DataError.Local>

    fun watchUpdateInfo(type: DataType, setId: Int?): Flow<UpdateInfo?>

    suspend fun storeUpdateInfo(updateInfo: UpdateInfo): EmptyResult<DataError.Local>
}
