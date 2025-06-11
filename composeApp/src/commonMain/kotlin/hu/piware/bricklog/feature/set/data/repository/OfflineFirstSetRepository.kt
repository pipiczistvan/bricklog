package hu.piware.bricklog.feature.set.data.repository

import androidx.paging.PagingData
import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.data.csv.SetListCsvParser
import hu.piware.bricklog.feature.set.domain.datasource.LocalSetDataSource
import hu.piware.bricklog.feature.set.domain.datasource.RemoteSetDataSource
import hu.piware.bricklog.feature.set.domain.model.FileUploadResult
import hu.piware.bricklog.feature.set.domain.model.Set
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetQueryOptions
import hu.piware.bricklog.feature.set.domain.model.SetThemeGroup
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import org.koin.core.annotation.Single
import kotlin.time.measureTimedValue

@Single
class OfflineFirstSetRepository(
    private val remoteDataSource: RemoteSetDataSource,
    private val localDataSource: LocalSetDataSource,
    private val csvParser: SetListCsvParser,
) : SetRepository {

    private val logger = Logger.withTag("OfflineFirstSetRepository")

    override fun watchSetDetails(queryOptions: SetQueryOptions): Flow<List<SetDetails>> {
        return localDataSource.watchSetDetails(queryOptions)
    }

    override suspend fun getSetDetails(queryOptions: SetQueryOptions): Result<List<SetDetails>, DataError> {
        return localDataSource.getSetDetails(queryOptions)
    }

    override fun watchSetDetailsPaged(queryOptions: SetQueryOptions): Flow<PagingData<SetDetails>> {
        return localDataSource.watchSetDetailsPaged(queryOptions)
    }

    override fun watchSetDetailsById(id: Int): Flow<SetDetails> {
        return localDataSource.watchSetDetailsById(id)
    }

    override suspend fun updateSets(fileUploads: List<FileUploadResult>): EmptyResult<DataError> {
        // Downloading
        logger.i { "Downloading sets" }
        val (downloadResult, downloadTimeTaken) = measureTimedValue {
            downloadSets(fileUploads)
        }
        logger.i { "Downloading sets took $downloadTimeTaken" }
        if (downloadResult is Result.Error) {
            return downloadResult
        }

        val setsData = (downloadResult as Result.Success).data

        // Parsing
        logger.i { "Parsing sets" }
        val (parseResult, parseTimeTaken) = measureTimedValue {
            val parsedSets = mutableListOf<Set>()
            csvParser.parseInChunksAsync(setsData, SET_CSV_CHUNK_SIZE) { sets ->
                parsedSets.addAll(sets)
            }
            parsedSets
        }
        logger.i { "Parsing sets took $parseTimeTaken" }

        // Storing
        logger.i { "Storing ${parseResult.size} sets" }
        val (storeResult, storeTimeTaken) = measureTimedValue {
            localDataSource.updateSets(parseResult)
        }
        logger.i { "Storing sets took $storeTimeTaken" }

        return storeResult
    }

    override suspend fun getSetCount(): Result<Int, DataError> {
        return localDataSource.getSetCount()
    }

    override fun watchThemes(): Flow<List<String>> {
        return localDataSource.watchThemes()
    }

    override fun watchThemeGroups(): Flow<List<SetThemeGroup>> {
        return localDataSource.watchThemeGroups()
    }

    override fun watchPackagingTypes(): Flow<List<String>> {
        return localDataSource.watchPackagingTypes()
    }

    override suspend fun deleteSetsUpdatedAfter(date: Instant): EmptyResult<DataError.Local> {
        return localDataSource.deleteSetsUpdatedAfter(date)
    }

    override suspend fun getLastUpdatedSet(): Result<Set?, DataError> {
        return localDataSource.getLastUpdatedSet()
    }

    private suspend fun downloadSets(fileUploads: List<FileUploadResult>): Result<ByteArray, DataError.Remote> {
        val orderedFileUploads = fileUploads.sortedBy { it.priority }

        for (fileUpload in orderedFileUploads) {
            val downloadResult = remoteDataSource.downloadSetsCsv(fileUpload.url)
            if (downloadResult is Result.Success) {
                return downloadResult
            }
        }

        return Result.Error(DataError.Remote.UNKNOWN)
    }

    companion object {
        private const val SET_CSV_CHUNK_SIZE = 3000
    }
}
