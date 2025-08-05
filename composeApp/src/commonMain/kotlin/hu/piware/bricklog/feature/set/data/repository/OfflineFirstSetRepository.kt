@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.set.data.repository

import androidx.paging.PagingData
import co.touchlab.kermit.Logger
import hu.piware.bricklog.di.SetListCsvParser
import hu.piware.bricklog.feature.core.data.csv.CsvParser
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.await
import hu.piware.bricklog.feature.core.domain.awaitInProgressRange
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.flowForResult
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.set.data.csv.SetRow
import hu.piware.bricklog.feature.set.domain.datasource.LocalSetDataSource
import hu.piware.bricklog.feature.set.domain.datasource.RemoteSetDataSource
import hu.piware.bricklog.feature.set.domain.model.ExportBatch
import hu.piware.bricklog.feature.set.domain.model.FileUploadResult
import hu.piware.bricklog.feature.set.domain.model.Set
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetQueryOptions
import hu.piware.bricklog.feature.set.domain.model.SetThemeGroup
import hu.piware.bricklog.feature.set.domain.model.UpdateSetsProgress
import hu.piware.bricklog.feature.set.domain.model.UpdateSetsStep
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import korlibs.io.compression.deflate.GZIP
import korlibs.io.compression.uncompress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import org.koin.core.annotation.Single
import kotlin.time.measureTimedValue

@Single
class OfflineFirstSetRepository(
    private val remoteDataSource: RemoteSetDataSource,
    private val localDataSource: LocalSetDataSource,
    private val sessionManager: SessionManager,
    @param:SetListCsvParser
    private val csvParser: CsvParser<SetRow, Set>,
) : SetRepository {

    private val logger = Logger.withTag("OfflineFirstSetRepository")

    override suspend fun getSetCount(): Result<Int, DataError> {
        return localDataSource.getSetCount()
    }

    override suspend fun getLastUpdatedSet(): Result<Set?, DataError> {
        return localDataSource.getLastUpdatedSet()
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

    override fun watchSetDetails(queryOptions: SetQueryOptions): Flow<List<SetDetails>> {
        return sessionManager.userId.flatMapLatest { userId ->
            localDataSource.watchSetDetails(userId, queryOptions)
        }
    }

    override fun watchSetDetailsPaged(queryOptions: SetQueryOptions): Flow<PagingData<SetDetails>> {
        return sessionManager.userId.flatMapLatest { userId ->
            localDataSource.watchSetDetailsPaged(userId, queryOptions)
        }
    }

    override fun updateSetsWithProgress(exportBatch: ExportBatch) = flowForResult {
        val downloadedFile = awaitInProgressRange(0f..0.25f) {
            downloadFile(exportBatch.fileUploads)
        }.onError { return@flowForResult it }.data()

        val csvFile = awaitInProgressRange(0.25f..0.5f) {
            uncompressFile(downloadedFile)
        }.onError { return@flowForResult it }.data()

        val parsedSets = awaitInProgressRange(0.5f..0.75f) {
            parseSets(csvFile, exportBatch.rowCount)
        }.onError { return@flowForResult it }.data()

        awaitInProgressRange(0.75f..1f) { storeSets(parsedSets) }
    }

    private fun downloadFile(fileUploads: List<FileUploadResult>) = flowForResult {
        val orderedFileUploads = fileUploads.sortedBy { it.priority }

        for (fileUpload in orderedFileUploads) {
            val downloadResult = await {
                remoteDataSource.downloadCompressedCsv(fileUpload.url)
            }
            if (downloadResult is Result.Success) {
                return@flowForResult downloadResult
            }
        }

        Result.Error(DataError.Remote.UNKNOWN)
    }

    private fun storeSets(sets: List<Set>) = flowForResult {
        logger.i { "Storing ${sets.size} sets" }
        emitProgress(UpdateSetsProgress(0f, UpdateSetsStep.STORE_SETS))
        val (storeResult, storeTimeTaken) = measureTimedValue {
            localDataSource.upsertSetsChunked(sets, SET_CHUNK_SIZE) { insertCount ->
                if (sets.isNotEmpty()) {
                    val progress = insertCount.toFloat() / sets.size
                    emitProgress(UpdateSetsProgress(progress, UpdateSetsStep.STORE_SETS))
                }
            }
        }
        logger.i { "Storing sets took $storeTimeTaken" }
        storeResult
    }

    private fun parseSets(csv: ByteArray, linesCount: Int) = flowForResult {
        logger.i { "Parsing sets" }
        emitProgress(UpdateSetsProgress(0f, UpdateSetsStep.PARSE_SETS))
        val (parseResult, parseTimeTaken) = measureTimedValue {
            try {
                val parsedSets = mutableListOf<Set>()
                csvParser.parseInChunksAsync(csv, SET_CHUNK_SIZE) { sets ->
                    parsedSets.addAll(sets)
                    if (linesCount > 0) {
                        val progress = parsedSets.size.toFloat() / linesCount
                        emitProgress(UpdateSetsProgress(progress, UpdateSetsStep.PARSE_SETS))
                    }
                }
                Result.Success(parsedSets)
            } catch (e: Exception) {
                logger.e("Failed to parse sets", e)
                Result.Error(DataError.Local.UNKNOWN)
            }
        }
        logger.i { "Parsing sets took $parseTimeTaken" }
        parseResult
    }

    private fun uncompressFile(gzippedFile: ByteArray) = flowForResult {
        withContext(Dispatchers.Default) {
            logger.d { "Uncompressing gzip file" }
            emitProgress(UpdateSetsProgress(0f, UpdateSetsStep.UNCOMPRESS_FILE))
            val (uncompressedBytes, uncompressTimeTaken) = measureTimedValue {
                gzippedFile.uncompress(method = GZIP)
            }
            logger.d { "Uncompressing gzip file took $uncompressTimeTaken" }
            emitProgress(UpdateSetsProgress(1f, UpdateSetsStep.UNCOMPRESS_FILE))
            Result.Success(uncompressedBytes)
        }
    }

    override suspend fun deleteSetsUpdatedAfter(date: Instant): EmptyResult<DataError.Local> {
        return localDataSource.deleteSetsUpdatedAfter(date)
    }

    companion object {
        private const val SET_CHUNK_SIZE = 3000
    }
}
