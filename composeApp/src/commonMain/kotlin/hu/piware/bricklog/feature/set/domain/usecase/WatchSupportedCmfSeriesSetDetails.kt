@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.repository.CmfCodeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import org.koin.core.annotation.Single

@Single
class WatchSupportedCmfSeriesSetDetails(
    private val cmfCodeRepository: CmfCodeRepository,
    private val watchSetDetailsByPreferences: WatchSetDetailsByPreferences,
) {
    operator fun invoke(): Flow<List<SetDetails>> {
        return cmfCodeRepository.watchCmfSeriesSetIds()
            .flatMapLatest { seriesSetIds ->
                watchSetDetailsByPreferences(
                    SetFilter(setIds = seriesSetIds),
                )
            }
    }
}
