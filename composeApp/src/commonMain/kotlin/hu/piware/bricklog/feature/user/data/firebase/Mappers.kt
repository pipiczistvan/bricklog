package hu.piware.bricklog.feature.user.data.firebase

import dev.gitlive.firebase.auth.FirebaseUser
import hu.piware.bricklog.feature.user.domain.model.User

fun FirebaseUser.toUser(): User {
    return User(
        uid = uid,
        displayName = displayName,
    )
}
