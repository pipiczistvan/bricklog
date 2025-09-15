package hu.piware.bricklog.util

expect object AppState {
    fun isAppInForeground(): Boolean
}
