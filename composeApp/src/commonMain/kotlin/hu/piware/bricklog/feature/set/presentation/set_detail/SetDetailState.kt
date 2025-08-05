package hu.piware.bricklog.feature.set.presentation.set_detail

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.set.domain.model.Instruction
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetPriceDetails

data class SetDetailState(
    val setDetails: SetDetails? = null,
    val instructions: List<Instruction>? = null,
    val sharedElementPrefix: String = "",
    val availableCollections: List<Collection> = emptyList(),
    val setPriceDetails: SetPriceDetails? = null,
)
