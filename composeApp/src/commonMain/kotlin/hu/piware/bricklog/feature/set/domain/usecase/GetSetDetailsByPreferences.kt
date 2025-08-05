package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.util.asResultOrDefault
import org.koin.core.annotation.Single

@Single
class GetSetDetailsByPreferences(
    private val watchSetDetailsByPreferences: WatchSetDetailsByPreferences,
) {
    suspend operator fun invoke(filterOverrides: SetFilter? = null, query: String = "") =
        watchSetDetailsByPreferences(filterOverrides, query)
            .asResultOrDefault { emptyList() }
}
