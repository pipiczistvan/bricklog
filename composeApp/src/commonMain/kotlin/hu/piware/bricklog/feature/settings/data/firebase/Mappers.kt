package hu.piware.bricklog.feature.settings.data.firebase

import hu.piware.bricklog.feature.user.domain.model.UserPreferences

fun UserPreferencesDocument.toDomainModel(): UserPreferences {
    return UserPreferences(
        hideGreetings = hideGreetings,
        displayName = displayName,
        collectionOrder = collectionOrder,
        hiddenFeaturedSets = hiddenFeaturedSets,
    )
}

fun UserPreferences.toDocument(): UserPreferencesDocument {
    return UserPreferencesDocument(
        hideGreetings = hideGreetings,
        displayName = displayName?.ifEmpty { null },
        collectionOrder = collectionOrder,
        hiddenFeaturedSets = hiddenFeaturedSets,
    )
}
