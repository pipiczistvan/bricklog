package hu.piware.bricklog.feature.collection.domain.model

import hu.piware.bricklog.feature.set.domain.model.SetDetails

data class CollectionSetDetails(
    val collection: CollectionDetails,
    val sets: List<SetDetails>,
)
