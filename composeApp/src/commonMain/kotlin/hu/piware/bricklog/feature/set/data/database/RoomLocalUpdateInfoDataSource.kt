package hu.piware.bricklog.feature.set.data.database

import androidx.sqlite.SQLiteException
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.datasource.LocalUpdateInfoDataSource
import hu.piware.bricklog.feature.set.domain.model.DataType
import hu.piware.bricklog.feature.set.domain.model.UpdateInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomLocalUpdateInfoDataSource(
    private val updateInfoDao: UpdateInfoDao,
) : LocalUpdateInfoDataSource {

    override suspend fun getUpdateInfo(type: DataType): Result<UpdateInfo?, DataError.Local> {
        return try {
            val updateInfo = updateInfoDao.getByType(type)?.toDomainModel()
            Result.Success(updateInfo)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override fun watchUpdateInfo(type: DataType): Flow<UpdateInfo?> {
        return updateInfoDao.watchByType(type)
            .map { it?.toDomainModel() }
    }

    override suspend fun storeUpdateInfo(updateInfo: UpdateInfo): EmptyResult<DataError.Local> {
        return try {
            updateInfoDao.upsert(updateInfo.toEntity())
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            Result.Error(DataError.Local.DISK_FULL)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}
