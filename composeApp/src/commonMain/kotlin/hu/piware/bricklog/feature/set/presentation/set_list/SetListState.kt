package hu.piware.bricklog.feature.set.presentation.set_list

import hu.piware.bricklog.feature.collection.domain.model.CollectionDetails
import hu.piware.bricklog.feature.currency.domain.model.CurrencyPreferenceDetails
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.model.SetFilterDomain
import hu.piware.bricklog.feature.set.domain.model.SetListDisplayMode
import hu.piware.bricklog.feature.set.presentation.set_list.components.SetListTitle
import hu.piware.bricklog.feature.settings.domain.model.SetFilterPreferences
import hu.piware.bricklog.feature.user.domain.manager.SessionManager.Companion.GUEST_USER
import hu.piware.bricklog.feature.user.domain.model.User

data class SetListState(
    val title: SetListTitle? = null,
    val filterOverrides: SetFilter? = null,
    val filterPreferences: SetFilterPreferences = SetFilterPreferences(),
    val displayMode: SetListDisplayMode = SetListDisplayMode.COLUMN,
    val showFilterBar: Boolean = true,
    val filterDomain: SetFilterDomain = SetFilterDomain(),
    val currencyPreferenceDetails: CurrencyPreferenceDetails? = null,
    val availableCollections: List<CollectionDetails> = emptyList(),
    val baseCollection: CollectionDetails? = null,
    val user: User = GUEST_USER,
)
