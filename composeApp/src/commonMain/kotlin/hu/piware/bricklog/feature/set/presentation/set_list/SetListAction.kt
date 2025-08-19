package hu.piware.bricklog.feature.set.presentation.set_list

import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.set.domain.model.SetListDisplayMode
import hu.piware.bricklog.feature.set.presentation.set_detail.SetDetailArguments
import hu.piware.bricklog.feature.settings.domain.model.SetFilterPreferences

sealed interface SetListAction {
    data object OnBackClick : SetListAction
    data class OnSetClick(val arguments: SetDetailArguments) : SetListAction
    data class OnCollectionToggle(val setId: SetId, val collectionId: CollectionId) : SetListAction
    data class OnFilterChange(val filterPreferences: SetFilterPreferences) : SetListAction
    data class OnDisplayModeChange(val mode: SetListDisplayMode) : SetListAction
}
