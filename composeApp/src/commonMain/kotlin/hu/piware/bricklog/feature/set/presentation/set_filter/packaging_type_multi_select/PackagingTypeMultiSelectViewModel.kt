package hu.piware.bricklog.feature.set.presentation.set_filter.packaging_type_multi_select

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import hu.piware.bricklog.feature.core.presentation.asStateFlowIn
import hu.piware.bricklog.feature.core.presentation.navigation.CustomNavType
import hu.piware.bricklog.feature.set.domain.usecase.WatchPackagingTypes
import hu.piware.bricklog.feature.set.presentation.SetRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlin.reflect.typeOf

class PackagingTypeMultiSelectViewModel(
    private val watchPackagingTypes: WatchPackagingTypes,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _arguments = savedStateHandle.toRoute<SetRoute.PackagingTypeMultiSelect>(
        typeMap = mapOf(typeOf<PackagingTypeMultiSelectArguments>() to CustomNavType.PackagingTypeMultiSelectArgumentsType)
    ).arguments

    private val _uiState = MutableStateFlow(
        PackagingTypeMultiSelectState(
            selectedPackagingTypes = _arguments.packagingTypes
        )
    )

    val uiState = _uiState
        .asStateFlowIn(viewModelScope) {
            observeAvailablePackagingTypes()
        }

    private val _query = _uiState
        .map { it.searchQuery }
        .distinctUntilChanged()

    fun onAction(action: PackagingTypeMultiSelectAction) {
        when (action) {
            is PackagingTypeMultiSelectAction.OnQueryChange -> _uiState.update { it.copy(searchQuery = action.query) }
            is PackagingTypeMultiSelectAction.OnSelectionChange -> _uiState.update {
                it.copy(
                    selectedPackagingTypes = action.packagingTypes
                )
            }

            else -> Unit
        }
    }

    private fun observeAvailablePackagingTypes() {
        combine(_query, watchPackagingTypes()) { query, packagingTypes ->
            packagingTypes.filter {
                it.contains(query, ignoreCase = true)
            }
        }
            .onEach { packagingTypes ->
                _uiState.update { it.copy(availablePackagingTypes = packagingTypes) }
            }
            .launchIn(viewModelScope)
    }
}
