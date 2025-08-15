package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.currency.domain.usecase.WatchCurrencyPreferenceDetails
import hu.piware.bricklog.feature.set.domain.model.Collectible
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.set.domain.model.SetQueryOptions
import hu.piware.bricklog.feature.set.domain.repository.DataServiceRepository
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.util.asResultOrDefault
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import org.koin.core.annotation.Single

@Single
class FindCollectibleSet(
    private val dataServiceRepository: DataServiceRepository,
    private val setRepository: SetRepository,
    private val watchCurrencyPreferenceDetails: WatchCurrencyPreferenceDetails,
    private val sessionManager: SessionManager,
) {
    suspend operator fun invoke(code: String): Result<SetDetails?, DataError> {
        val collectibles = dataServiceRepository.watchCollectibles()
            .asResultOrDefault { emptyList() }
            .onError { return it }
            .data()

        val userId = sessionManager.userId.first()
        val setId = collectibles.findSetId(code) ?: return Result.Success(null)
        val currencyDetails = watchCurrencyPreferenceDetails().first()

        val setDetails = setRepository.watchSetDetails(
            SetQueryOptions(
                userId = userId,
                currencyDetails = currencyDetails,
                setIds = listOf(setId),
            ),
        )
            .firstOrNull()?.firstOrNull()

        return Result.Success(setDetails)
    }
}

private fun List<Collectible>.findSetId(code: String): SetId? {
    forEach { collectible ->
        collectible.codes.forEach { (setId, codeList) ->
            if (codeList.r.contains(code) || codeList.s.contains(code)) {
                return setId
            }
        }
    }

    return null
}
