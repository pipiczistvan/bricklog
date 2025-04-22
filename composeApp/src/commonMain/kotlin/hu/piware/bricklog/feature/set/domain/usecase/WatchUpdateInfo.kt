package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.set.domain.model.DataType
import hu.piware.bricklog.feature.set.domain.model.UpdateInfo
import hu.piware.bricklog.feature.set.domain.repository.UpdateInfoRepository
import kotlinx.coroutines.flow.Flow

class WatchUpdateInfo(
    private val updateInfoRepository: UpdateInfoRepository,
) {
    operator fun invoke(type: DataType): Flow<UpdateInfo?> {
        return updateInfoRepository.watchUpdateInfo(type)
    }
}
