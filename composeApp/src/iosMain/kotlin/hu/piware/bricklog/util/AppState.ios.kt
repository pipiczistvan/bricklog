package hu.piware.bricklog.util

import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationState

actual object AppState {
    actual fun isAppInForeground(): Boolean {
        return UIApplication.sharedApplication.applicationState == UIApplicationState.UIApplicationStateActive
    }
}