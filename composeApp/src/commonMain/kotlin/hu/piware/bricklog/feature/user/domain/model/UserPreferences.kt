package hu.piware.bricklog.feature.user.domain.model

import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.currency.domain.model.CurrencyRegion
import hu.piware.bricklog.feature.set.presentation.dashboard.utils.FeaturedSetType

data class UserPreferences(
    val hideGreetings: Boolean,
    val displayName: String?,
    val collectionOrder: List<CollectionId>,
    val hiddenFeaturedSets: List<FeaturedSetType>,
    val currencyRegion: CurrencyRegion,
    val targetCurrencyCode: String,
)
