package hu.piware.bricklog.feature.set.domain.util

fun String.parseQueries() = split(",")
    .map { it.removeUnnecessaryWhitespace() }
    .filterNot { it.isBlank() }
    .ifEmpty { listOf("") }
