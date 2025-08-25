package hu.piware.bricklog.feature.user.data.firebase

import dev.gitlive.firebase.auth.FirebaseUser
import hu.piware.bricklog.feature.currency.domain.model.CurrencyRegion
import hu.piware.bricklog.feature.currency.domain.util.CURRENCY_CODE_EUR
import hu.piware.bricklog.feature.user.domain.model.Friend
import hu.piware.bricklog.feature.user.domain.model.User
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.feature.user.domain.model.UserPreferences

fun FirebaseUser.toUser(): User {
    return User(
        uid = uid,
        displayName = displayName,
    )
}

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

fun FriendDocument.toDomainModel(friendId: UserId): Friend {
    return Friend(
        id = friendId,
        name = name,
    )
}

fun Friend.toDocument(): FriendDocument {
    return FriendDocument(
        name = name,
    )
}
