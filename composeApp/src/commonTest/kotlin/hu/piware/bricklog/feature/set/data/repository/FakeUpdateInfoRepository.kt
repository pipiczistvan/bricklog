package hu.piware.bricklog.feature.set.data.repository

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.DataType
import hu.piware.bricklog.feature.set.domain.model.UpdateInfo
import hu.piware.bricklog.feature.set.domain.repository.UpdateInfoRepository
import kotlinx.coroutines.flow.Flow

class FakeUpdateInfoRepository : UpdateInfoRepository {

    private var updateInfo: UpdateInfo? = null

    override fun watchUpdateInfo(
        type: DataType,
        setId: Int?,
    ): Flow<UpdateInfo?> {
        TODO("Not yet implemented")
    }

    override suspend fun saveUpdateInfo(updateInfo: UpdateInfo): EmptyResult<DataError> {
        this.updateInfo = updateInfo
        return Result.Success(Unit)
    }
}
