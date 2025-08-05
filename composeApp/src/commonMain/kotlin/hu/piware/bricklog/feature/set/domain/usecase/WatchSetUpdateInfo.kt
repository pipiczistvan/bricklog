package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.set.domain.model.DataType
import hu.piware.bricklog.feature.set.domain.model.UpdateInfo
import hu.piware.bricklog.feature.set.domain.repository.UpdateInfoRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class WatchSetUpdateInfo(
    private val updateInfoRepository: UpdateInfoRepository,
) {
    operator fun invoke(): Flow<UpdateInfo?> {
        return updateInfoRepository.watchUpdateInfo(DataType.SET_DATA)
    }
}
