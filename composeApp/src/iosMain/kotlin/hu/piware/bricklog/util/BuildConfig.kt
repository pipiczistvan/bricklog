package hu.piware.bricklog.util

import platform.Foundation.NSBundle

actual object BuildConfig {
    actual val VERSION_NAME: String
        get() = NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString") as String
    actual val VERSION_CODE: String
        get() = NSBundle.mainBundle.infoDictionary?.get("CFBundleVersion") as String
}