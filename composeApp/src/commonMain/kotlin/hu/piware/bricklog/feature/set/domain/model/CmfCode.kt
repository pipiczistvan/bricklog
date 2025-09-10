package hu.piware.bricklog.feature.set.domain.model

data class CmfCode(
    val code: String,
    val setId: SetId,
    val seriesSetId: SetId,
    val manufacturerId: String,
)
