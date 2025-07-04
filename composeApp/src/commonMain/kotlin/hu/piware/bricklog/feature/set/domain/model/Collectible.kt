package hu.piware.bricklog.feature.set.domain.model

data class Collectible(
    val setNumber: String,
    val name: String,
    val codes: Map<SetId, CodeList>,
)

data class CodeList(
    val r: List<String>,
    val s: List<String>,
)
