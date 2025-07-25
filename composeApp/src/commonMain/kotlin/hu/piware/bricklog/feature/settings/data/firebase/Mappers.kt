package hu.piware.bricklog.feature.settings.data.firebase

import hu.piware.bricklog.feature.user.domain.model.UserPreferences

fun UserPreferencesDocument.toDomainModel(): UserPreferences {
    return UserPreferences(
        showGreetings = showGreetings,
        displayName = displayName,
        collectionOrder = collectionOrder,
    )
}

fun UserPreferences.toDocument(): UserPreferencesDocument {
    return UserPreferencesDocument(
        showGreetings = showGreetings,
        displayName = displayName?.ifEmpty { null },
        collectionOrder = collectionOrder,
    )
}
