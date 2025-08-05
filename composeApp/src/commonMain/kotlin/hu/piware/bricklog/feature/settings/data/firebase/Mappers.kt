package hu.piware.bricklog.feature.settings.data.firebase

import hu.piware.bricklog.feature.currency.domain.model.CurrencyRegion
import hu.piware.bricklog.feature.currency.domain.util.CURRENCY_CODE_EUR
import hu.piware.bricklog.feature.user.domain.model.UserPreferences

fun UserPreferencesDocument.toDomainModel(): UserPreferences {
    return UserPreferences(
        hideGreetings = hideGreetings ?: false,
        displayName = displayName,
        collectionOrder = collectionOrder ?: emptyList(),
        hiddenFeaturedSets = hiddenFeaturedSets ?: emptyList(),
        currencyRegion = currencyRegion ?: CurrencyRegion.EU,
        targetCurrencyCode = targetCurrencyCode ?: CURRENCY_CODE_EUR,
    )
}

fun UserPreferences.toDocument(): UserPreferencesDocument {
    return UserPreferencesDocument(
        hideGreetings = hideGreetings,
        displayName = displayName?.ifEmpty { null },
        collectionOrder = collectionOrder,
        hiddenFeaturedSets = hiddenFeaturedSets,
        currencyRegion = currencyRegion,
        targetCurrencyCode = targetCurrencyCode,
    )
}
