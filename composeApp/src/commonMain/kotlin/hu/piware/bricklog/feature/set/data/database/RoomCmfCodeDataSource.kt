package hu.piware.bricklog.feature.set.data.database

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.data.database.BricklogDatabase
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.datasource.LocalCmfCodeDataSource
import hu.piware.bricklog.feature.set.domain.model.CmfCode
import hu.piware.bricklog.feature.set.domain.model.SetId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class RoomCmfCodeDataSource(
    database: BricklogDatabase,
) : LocalCmfCodeDataSource {

    private val logger = Logger.withTag("RoomCmfCodeDataSource")

    private val cmfCodeDao = database.cmfCodeDao

    override fun watchCmfCode(code: String): Flow<CmfCode?> {
        return cmfCodeDao.watchCmfCode(code)
            .map { it?.toDomainModel() }
    }

    override fun watchCmfSeriesSetIds(): Flow<List<SetId>> {
        return cmfCodeDao.watchCmfSeriesSetIds()
    }

    override suspend fun upsertCmfCodes(codes: List<CmfCode>): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Upserting cmf codes" }
            cmfCodeDao.upsertCmfCodes(codes.map { it.toEntity() })
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Error upserting cmf codes" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}
