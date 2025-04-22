package hu.piware.bricklog.feature.set.domain.util

fun String.removeUnnecessaryWhitespace(): String {
    return this.trim().replace("\\s+".toRegex(), " ")
}
