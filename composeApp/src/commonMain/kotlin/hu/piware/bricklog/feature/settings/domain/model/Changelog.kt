package hu.piware.bricklog.feature.settings.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Changelog(
    val releases: List<Release>,
)

@Serializable
data class Release(
    val version: String,
    val build: Int,
    val changes: List<Change>,
)

@Serializable
data class Change(
    val description: String,
    val type: ChangeType,
)

@Serializable
enum class ChangeType {
    @SerialName("feature")
    FEATURE,

    @SerialName("bugfix")
    BUGFIX
}
