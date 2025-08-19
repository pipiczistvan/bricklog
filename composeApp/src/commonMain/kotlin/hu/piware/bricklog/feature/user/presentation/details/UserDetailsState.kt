package hu.piware.bricklog.feature.user.presentation.details

import hu.piware.bricklog.feature.user.domain.model.User

data class UserDetailsState(
    val currentUser: User? = null,
)
