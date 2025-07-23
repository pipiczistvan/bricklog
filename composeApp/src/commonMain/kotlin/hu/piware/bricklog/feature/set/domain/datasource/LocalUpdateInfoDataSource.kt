package hu.piware.bricklog.feature.set.domain.datasource

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.set.domain.model.DataType
import hu.piware.bricklog.feature.set.domain.model.UpdateInfo
import kotlinx.coroutines.flow.Flow

interface LocalUpdateInfoDataSource {

    fun watchUpdateInfo(type: DataType, setId: Int?): Flow<UpdateInfo?>

    suspend fun upsertUpdateInfo(updateInfo: UpdateInfo): EmptyResult<DataError.Local>
}
