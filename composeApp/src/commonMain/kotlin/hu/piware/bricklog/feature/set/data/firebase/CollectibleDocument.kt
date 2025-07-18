package hu.piware.bricklog.feature.set.data.firebase

import hu.piware.bricklog.feature.set.domain.model.SetId
import kotlinx.serialization.Serializable

@Serializable
data class CollectibleDocument(
    val codes: Map<SetId, CodeListDocument>,
    val setId: SetId,
    val name: String,
)
