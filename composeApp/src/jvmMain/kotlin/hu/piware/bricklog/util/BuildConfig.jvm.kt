package hu.piware.bricklog.util

import hu.piware.bricklog.BuildKonfig

actual object BuildConfig {
    actual val VERSION_NAME: String
        get() = BuildKonfig.VERSION_NAME
    actual val VERSION_CODE: String
        get() = BuildKonfig.VERSION_CODE.toString()
}
