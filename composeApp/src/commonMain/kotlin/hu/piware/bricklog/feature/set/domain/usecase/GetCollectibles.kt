package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.Collectible
import hu.piware.bricklog.feature.set.domain.repository.DataServiceRepository
import hu.piware.bricklog.util.asResultOrDefault
import org.koin.core.annotation.Single

@Single
class GetCollectibles(
    private val dataServiceRepository: DataServiceRepository,
) {
    suspend operator fun invoke(): Result<List<Collectible>, DataError> {
        return dataServiceRepository.watchCollectibles()
            .asResultOrDefault { emptyList() }
    }
}
