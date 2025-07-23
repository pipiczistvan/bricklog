package hu.piware.bricklog.feature.set.data.database

import androidx.sqlite.SQLiteException
import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.data.database.BricklogDatabase
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.datasource.LocalSetImageDataSource
import hu.piware.bricklog.feature.set.domain.model.Image
import org.koin.core.annotation.Single

@Single
class RoomSetImageDataSource(
    database: BricklogDatabase,
) : LocalSetImageDataSource {

    private val logger = Logger.withTag("RoomSetImageDataSource")

    private val dao = database.setImagesDao

    override suspend fun getImages(setId: Int): Result<List<Image>, DataError.Local> {
        return try {
            logger.d { "Getting images" }
            val images = dao.getImages(setId).map { it.toDomainModel() }
            Result.Success(images)
        } catch (e: Exception) {
            logger.e(e) { "Error getting images" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun upsertImages(
        setId: Int,
        images: List<Image>,
    ): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Storing images" }
            dao.upsertImages(images.map { it.toEntity(setId) })
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            logger.e(e) { "Error storing images" }
            Result.Error(DataError.Local.DISK_FULL)
        } catch (e: Exception) {
            logger.e(e) { "Error storing images" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteImages(setId: Int): EmptyResult<DataError.Local> {
        return try {
            dao.deleteImages(setId)
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Error deleting images" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}
