package hu.piware.bricklog.feature.set.presentation.set_filter.packaging_type_multi_select

sealed interface PackagingTypeMultiSelectAction {
    data class OnQueryChange(val query: String) : PackagingTypeMultiSelectAction
    data object OnBackClick : PackagingTypeMultiSelectAction
    data class OnApplyClick(val arguments: PackagingTypeMultiSelectArguments) :
        PackagingTypeMultiSelectAction

    data class OnSelectionChange(val packagingTypes: Set<String>) : PackagingTypeMultiSelectAction
}
