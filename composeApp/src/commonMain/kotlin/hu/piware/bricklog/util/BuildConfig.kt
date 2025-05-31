package hu.piware.bricklog.util

import hu.piware.bricklog.BuildKonfig

expect object BuildConfig {
    val VERSION_NAME: String
    val VERSION_CODE: String
}

val BuildConfig.RELEASE_VERSION: Int
    get() = BuildKonfig.RELEASE_VERSION

enum class Flavor {
    DEVELOPMENT,
    MOCK,
    PRODUCTION
}

val BuildConfig.flavor: Flavor
    get() = when (BuildKonfig.FLAVOR) {
        "DEV" -> Flavor.DEVELOPMENT
        "MOCK" -> Flavor.MOCK
        else -> Flavor.PRODUCTION
    }

val BuildConfig.isDebugFlavor: Boolean
    get() = BuildConfig.flavor == Flavor.DEVELOPMENT || BuildConfig.flavor == Flavor.MOCK
