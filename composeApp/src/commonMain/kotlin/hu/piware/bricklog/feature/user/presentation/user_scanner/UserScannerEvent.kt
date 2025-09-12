package hu.piware.bricklog.feature.user.presentation.user_scanner

sealed interface UserScannerEvent {
    data class UserScanned(val userId: String, val userName: String) : UserScannerEvent
}
