package hu.piware.bricklog.feature.user.domain.model

data class Friend(
    val name: String,
    val id: UserId,
)

val Friend.isNew: Boolean
    get() = id == ""
