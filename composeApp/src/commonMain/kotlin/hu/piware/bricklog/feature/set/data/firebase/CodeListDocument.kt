package hu.piware.bricklog.feature.set.data.firebase

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CodeListDocument(
    @SerialName("R") val r: List<String> = emptyList(),
    @SerialName("S") val s: List<String> = emptyList(),
)
