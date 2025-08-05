package hu.piware.bricklog.feature.user.data.database

import hu.piware.bricklog.feature.set.presentation.dashboard.utils.FeaturedSetType
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.feature.user.domain.model.UserPreferences

fun UserPreferencesEntity.toDomainModel(): UserPreferences {
    return UserPreferences(
        hideGreetings = hideGreetings,
        displayName = displayName,
        collectionOrder = collectionOrder,
        hiddenFeaturedSets = hiddenFeaturedSets
            .filter { it.isNotBlank() }
            .map { FeaturedSetType.valueOf(it) },
        currencyRegion = currencyRegion,
        targetCurrencyCode = targetCurrencyCode,
    )
}

fun UserPreferences.toEntity(userId: UserId): UserPreferencesEntity {
    return UserPreferencesEntity(
        userId = userId,
        hideGreetings = hideGreetings,
        displayName = displayName,
        collectionOrder = collectionOrder,
        hiddenFeaturedSets = hiddenFeaturedSets.map { it.name },
        currencyRegion = currencyRegion,
        targetCurrencyCode = targetCurrencyCode,
    )
}
