package hu.piware.bricklog.feature.collection.domain.model

import hu.piware.bricklog.feature.user.domain.model.UserId

data class UserCollectionShare(
    val userId: UserId,
    val permissions: SharePermissions,
)
