package hu.piware.bricklog.feature.set.data.database

import androidx.sqlite.SQLiteException
import co.touchlab.kermit.Logger
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
class RoomUpdateInfoDataSource(
    database: BricklogDatabase,
) : LocalUpdateInfoDataSource {

    private val logger = Logger.withTag("RoomUpdateInfoDataSource")

    private val dao = database.updateInfoDao

    override fun watchUpdateInfo(type: DataType, setId: Int?): Flow<UpdateInfo?> {
        return dao.watchUpsertInfo(type, setId)
            .map { it?.toDomainModel() }
    }

    override suspend fun upsertUpdateInfo(updateInfo: UpdateInfo): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Storing update info" }
            dao.upsertUpdateInfo(updateInfo.toEntity())
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            logger.e(e) { "Error storing update info" }
            Result.Error(DataError.Local.DISK_FULL)
        } catch (e: Exception) {
            logger.e(e) { "Error storing update info" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}
