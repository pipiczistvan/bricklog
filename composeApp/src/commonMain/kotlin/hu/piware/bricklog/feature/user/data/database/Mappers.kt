package hu.piware.bricklog.feature.user.data.database

import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.feature.user.domain.model.UserPreferences

fun UserPreferencesEntity.toDomainModel(): UserPreferences {
    return UserPreferences(
        showGreetings = showGreetings,
        displayName = displayName,
        collectionOrder = collectionOrder,
    )
}

fun UserPreferences.toEntity(userId: UserId): UserPreferencesEntity {
    return UserPreferencesEntity(
        userId = userId,
        showGreetings = showGreetings,
        displayName = displayName,
        collectionOrder = collectionOrder,
    )
}
