package hu.piware.bricklog.feature.user.domain.manager

import hu.piware.bricklog.feature.user.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Single

@Single
class SessionManager {

    private var authenticatedUser = MutableStateFlow<User?>(null)

    val currentUser = authenticatedUser.asStateFlow()

    fun setCurrentUser(user: User?) {
        authenticatedUser.update { user }
    }
}
