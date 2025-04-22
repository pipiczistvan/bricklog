package hu.piware.bricklog.feature.set.domain.usecase

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.set.domain.model.DataType
import hu.piware.bricklog.feature.set.domain.model.UpdateInfo
import hu.piware.bricklog.feature.set.domain.repository.DataServiceRepository
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import hu.piware.bricklog.feature.set.domain.repository.UpdateInfoRepository
import kotlinx.datetime.Clock

class UpdateSets(
    private val dataServiceRepository: DataServiceRepository,
    private val setRepository: SetRepository,
    private val updateInfoRepository: UpdateInfoRepository,
) {
    private val logger = Logger.withTag("UpdateSets")

    suspend operator fun invoke(force: Boolean = false): EmptyResult<DataError> {
        val exportInfo = dataServiceRepository.getExportInfo()
            .onError { return@invoke it }
            .data()

        val updateInfo = updateInfoRepository.getUpdateInfo(DataType.BRICKSET_SETS)
            .onError { return@invoke it }
            .data()

        val isUpdateNecessary =
            force || updateInfo == null || updateInfo.lastUpdated < exportInfo.lastUpdated

        if (!isUpdateNecessary) {
            logger.i { "No need to update" }
            updateInfoRepository.storeUpdateInfo(
                updateInfo = UpdateInfo(
                    dataType = DataType.BRICKSET_SETS,
                    lastUpdated = Clock.System.now()
                )
            )
                .onError { return@invoke it }
        } else {
            setRepository.updateSets(exportInfo.fileUploads)
                .onError { return@invoke it }
        }

        return updateInfoRepository.storeUpdateInfo(
            updateInfo = UpdateInfo(
                dataType = DataType.BRICKSET_SETS,
                lastUpdated = Clock.System.now()
            )
        )
    }
}
