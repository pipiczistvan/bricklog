package hu.piware.bricklog.util

actual object AppState {
    actual fun isAppInForeground(): Boolean {
        return AppLifecycleState.isForeground
    }
}
