package hu.piware.bricklog.feature.user.data.firebase

import hu.piware.bricklog.feature.currency.domain.model.CurrencyRegion
import hu.piware.bricklog.feature.set.presentation.dashboard.utils.FeaturedSetType
import kotlinx.serialization.Serializable

@Serializable
data class UserPreferencesDocument(
    val hideGreetings: Boolean? = null,
    val displayName: String? = null,
    val collectionOrder: List<String>? = null,
    val hiddenFeaturedSets: List<FeaturedSetType>? = null,
    val currencyRegion: CurrencyRegion? = null,
    val targetCurrencyCode: String? = null,
)
