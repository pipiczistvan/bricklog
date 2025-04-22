package hu.piware.bricklog.feature.set.presentation.set_detail

import hu.piware.bricklog.feature.set.domain.model.Instruction
import hu.piware.bricklog.feature.set.domain.model.SetUI

data class SetDetailState(
    val setUI: SetUI? = null,
    val instructions: List<Instruction>? = null,
    val sharedElementPrefix: String = "",
)
