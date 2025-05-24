package hu.piware.bricklog.util

import hu.piware.bricklog.BuildKonfig

expect object BuildConfig {
    val VERSION_NAME: String
    val VERSION_CODE: String
}

val BuildConfig.RELEASE_VERSION: Int
    get() = BuildKonfig.RELEASE_VERSION
