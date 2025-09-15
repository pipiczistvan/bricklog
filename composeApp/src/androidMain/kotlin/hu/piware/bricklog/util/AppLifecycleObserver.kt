package hu.piware.bricklog.util

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class AppLifecycleObserver : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        AppLifecycleState.isForeground = true
    }

    override fun onStop(owner: LifecycleOwner) {
        AppLifecycleState.isForeground = false
    }
}
