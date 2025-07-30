package hu.piware.bricklog.util

expect object BuildConfig {
    val VERSION_NAME: String
    val VERSION_CODE: String
}

object DevLevels {
    const val PRODUCTION = 0
    const val DEVELOPMENT = 1
    const val MOCK = 2
    const val BENCHMARK = 3
}
