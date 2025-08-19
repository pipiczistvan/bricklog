package hu.piware.bricklog.feature.set.presentation.set_list

import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import kotlinx.serialization.Serializable

@Serializable
sealed interface SetListArguments {

    @Serializable
    data class Filtered(
        val filterOverrides: SetFilter? = null,
        val title: String,
        val showFilterBar: Boolean = true,
    ) : SetListArguments

    @Serializable
    data class Collection(
        val collectionId: CollectionId,
    ) : SetListArguments
}
