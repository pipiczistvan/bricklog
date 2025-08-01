package hu.piware.bricklog.feature.set.presentation.set_list

import hu.piware.bricklog.feature.set.domain.model.SetFilter
import kotlinx.serialization.Serializable

@Serializable
data class SetListArguments(
    val filterOverrides: SetFilter? = null,
    val title: String,
    val searchQuery: String = "",
    val showFilterBar: Boolean = true,
)
