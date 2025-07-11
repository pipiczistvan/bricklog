package hu.piware.bricklog.feature.authentication.data.firebase

import dev.gitlive.firebase.auth.FirebaseUser
import hu.piware.bricklog.feature.authentication.domain.model.User

fun FirebaseUser.toUser(): User {
    return User(
        uid = uid
    )
}
