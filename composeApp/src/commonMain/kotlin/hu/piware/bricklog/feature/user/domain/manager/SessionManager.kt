package hu.piware.bricklog.feature.user.domain.manager

import hu.piware.bricklog.feature.settings.data.firebase.UserPreferencesDocument
import hu.piware.bricklog.feature.settings.data.firebase.toDomainModel
import hu.piware.bricklog.feature.user.domain.model.User
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Single

@Single
class SessionManager {

    private var authenticatedUser = MutableStateFlow(GUEST_USER)

    val user = authenticatedUser.asStateFlow()
    val userId = user.distinctUntilChanged { old, new ->
        old.uid == new.uid
    }.map { it.uid }
    val currentUserId: UserId
        get() = user.value.uid
    val isAuthenticated: Boolean
        get() = currentUserId != USER_ID_GUEST

    fun setCurrentUser(user: User?) {
        authenticatedUser.update { user ?: GUEST_USER }
    }

    companion object {
        const val USER_ID_GUEST = "guest"
        val GUEST_USER = User(USER_ID_GUEST, null)
        val GUEST_PREFERENCES = UserPreferencesDocument().toDomainModel()
    }
}

fun Flow<UserId>.filterAuthenticated() = filter { it != SessionManager.USER_ID_GUEST }
fun Flow<UserId>.filterGuest() = filter { it == SessionManager.USER_ID_GUEST }
