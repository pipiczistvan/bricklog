package hu.piware.bricklog.util

import hu.piware.bricklog.BuildConfig

actual object BuildConfig {
    actual val VERSION_NAME: String
        get() = BuildConfig.VERSION_NAME
    actual val VERSION_CODE: String
        get() = BuildConfig.VERSION_CODE.toString()
}
