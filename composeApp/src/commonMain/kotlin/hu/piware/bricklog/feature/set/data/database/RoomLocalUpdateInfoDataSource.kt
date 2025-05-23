package hu.piware.bricklog.feature.set.data.database

import androidx.sqlite.SQLiteException
import hu.piware.bricklog.feature.core.data.database.BricklogDatabase
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.datasource.LocalUpdateInfoDataSource
import hu.piware.bricklog.feature.set.domain.model.DataType
import hu.piware.bricklog.feature.set.domain.model.UpdateInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class RoomLocalUpdateInfoDataSource(
    database: BricklogDatabase,
) : LocalUpdateInfoDataSource {

    private val dao = database.updateInfoDao

    override suspend fun getUpdateInfo(
        type: DataType,
        setId: Int?,
    ): Result<UpdateInfo?, DataError.Local> {
        return try {
            val updateInfo = dao.getByType(type, setId)?.toDomainModel()
            Result.Success(updateInfo)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override fun watchUpdateInfo(type: DataType, setId: Int?): Flow<UpdateInfo?> {
        return dao.watchByType(type, setId)
            .map { it?.toDomainModel() }
    }

    override suspend fun storeUpdateInfo(updateInfo: UpdateInfo): EmptyResult<DataError.Local> {
        return try {
            dao.upsert(updateInfo.toEntity())
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            Result.Error(DataError.Local.DISK_FULL)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}
