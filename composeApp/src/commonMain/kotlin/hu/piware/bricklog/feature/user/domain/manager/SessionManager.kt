@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.user.domain.manager

import hu.piware.bricklog.feature.core.domain.AppEvent
import hu.piware.bricklog.feature.core.presentation.AppEventController
import hu.piware.bricklog.feature.user.data.firebase.UserPreferencesDocument
import hu.piware.bricklog.feature.user.data.firebase.toDomainModel
import hu.piware.bricklog.feature.user.domain.manager.SessionManager.Companion.USER_ID_GUEST
import hu.piware.bricklog.feature.user.domain.model.User
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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
        get() = currentUserId.isAuthenticated

    suspend fun setCurrentUser(user: User?) {
        authenticatedUser.update { user ?: GUEST_USER }
        AppEventController.sendEvent(AppEvent.UserChanged)
    }

    companion object {
        const val USER_ID_GUEST = "guest"
        val GUEST_USER = User(USER_ID_GUEST, null)
        val GUEST_PREFERENCES = UserPreferencesDocument().toDomainModel()
    }
}

val UserId.isAuthenticated: Boolean
    get() = this != USER_ID_GUEST

fun Flow<UserId>.filterAuthenticated() = filter { it.isAuthenticated }
fun Flow<UserId>.filterGuest() = filterNot { it.isAuthenticated }

fun <T> SessionManager.userBoundFlow(userId: UserId? = null, flow: (UserId) -> Flow<T>): Flow<T> {
    val userIdFlow = userId?.let { flowOf(userId) } ?: this.userId

    return userIdFlow.flatMapLatest { userId ->
        flow(userId)
    }
}
