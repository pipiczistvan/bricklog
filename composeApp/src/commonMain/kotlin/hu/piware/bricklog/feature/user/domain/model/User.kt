package hu.piware.bricklog.feature.user.domain.model

import hu.piware.bricklog.feature.user.domain.manager.SessionManager.Companion.USER_ID_GUEST

typealias UserId = String

data class User(
    val uid: UserId,
    val displayName: String?,
)

val User.isAuthenticated: Boolean
    get() = uid != USER_ID_GUEST
