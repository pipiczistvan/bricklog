package hu.piware.bricklog.feature.set.presentation.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.set_search_bar_chip_packaging_types
import bricklog.composeapp.generated.resources.set_search_bar_chip_release_date
import bricklog.composeapp.generated.resources.set_search_bar_chip_show_incomplete
import bricklog.composeapp.generated.resources.set_search_bar_chip_status
import bricklog.composeapp.generated.resources.set_search_bar_chip_themes
import hu.piware.bricklog.feature.core.presentation.toLocalDateTime
import hu.piware.bricklog.feature.core.presentation.util.formatDate
import hu.piware.bricklog.feature.set.domain.model.DateFilter
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.model.StatusFilterOption
import hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.components.DateFilterBottomSheet
import hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.components.SearchBarChip
import hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.components.StatusFilterBottomSheet
import hu.piware.bricklog.feature.settings.domain.model.SetFilterPreferences
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource

@Composable
fun SetFilterRow(
    filterPreferences: SetFilterPreferences,
    filterOverrides: SetFilter? = null,
    onFilterChange: (SetFilterPreferences) -> Unit,
    onThemeMultiselectClick: () -> Unit,
    onPackagingTypeMultiselectClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showReleaseDateFilterSheet by remember { mutableStateOf(false) }
    var showStatusFilterSheet by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)
    ) {
        Spacer(Modifier.size(Dimens.SmallPadding.size))
        StatusChip(
            enabled = filterOverrides?.status == null,
            status = filterOverrides?.status ?: filterPreferences.status,
            onClick = { showStatusFilterSheet = true }
        )
        ReleaseDateChip(
            enabled = filterOverrides?.launchDate == null,
            dateFilter = filterOverrides?.launchDate ?: filterPreferences.launchDate,
            onClick = { showReleaseDateFilterSheet = true }
        )
        ThemeChip(
            enabled = filterOverrides?.themes == null,
            selectedThemes = filterOverrides?.themes ?: filterPreferences.themes,
            onClick = onThemeMultiselectClick
        )
        PackagingTypeChip(
            enabled = filterOverrides?.packagingTypes == null,
            selectedPackagingTypes = filterOverrides?.packagingTypes
                ?: filterPreferences.packagingTypes,
            onClick = onPackagingTypeMultiselectClick
        )
        ShowIncompleteChip(
            enabled = filterOverrides?.showIncomplete == null,
            show = filterOverrides?.showIncomplete ?: filterPreferences.showIncomplete,
            onClick = { onFilterChange(filterPreferences.copy(showIncomplete = it)) }
        )
        Spacer(Modifier.size(Dimens.SmallPadding.size))
    }

    DateFilterBottomSheet(
        showBottomSheet = showReleaseDateFilterSheet,
        onShowBottomSheetChanged = { showReleaseDateFilterSheet = it },
        selectedFilter = filterPreferences.launchDate,
        onSelectFilter = { onFilterChange(filterPreferences.copy(launchDate = it)) }
    )

    StatusFilterBottomSheet(
        showBottomSheet = showStatusFilterSheet,
        onShowBottomSheetChanged = { showStatusFilterSheet = it },
        selectedOption = filterPreferences.status,
        onSelectOption = { onFilterChange(filterPreferences.copy(status = it)) }
    )
}

@Composable
private fun ReleaseDateChip(
    enabled: Boolean,
    dateFilter: DateFilter,
    onClick: () -> Unit,
) {
    SearchBarChip(
        modifier = Modifier.testTag("search_bar:release_date_chip"),
        title = when (dateFilter) {
            is DateFilter.AnyTime -> stringResource(Res.string.set_search_bar_chip_release_date)
            is DateFilter.Custom -> dateFilter.format()

            is DateFilter.OneWeek, DateFilter.OneMonth, DateFilter.OneYear -> stringResource(
                dateFilter.option.titleRes
            )
        },
        isDefaultSelected = dateFilter is DateFilter.AnyTime,
        showTrailingIcon = true,
        enabled = enabled,
        onClick = onClick
    )
}

private fun DateFilter.Custom.format(): String {
    return "${startDate?.let { formatDate(it.toLocalDateTime()) }} - ${endDate?.let { formatDate(it.toLocalDateTime()) }}"
}

@Composable
private fun ThemeChip(
    enabled: Boolean,
    selectedThemes: Set<String>,
    onClick: () -> Unit,
) {
    SearchBarChip(
        modifier = Modifier.testTag("search_bar:theme_chip"),
        title = when (selectedThemes.size) {
            0 -> stringResource(Res.string.set_search_bar_chip_themes)
            1 -> selectedThemes.first()
            else -> "${selectedThemes.first()} + ${selectedThemes.size - 1}"
        },
        isDefaultSelected = selectedThemes.isEmpty(),
        showTrailingIcon = true,
        enabled = enabled,
        onClick = onClick
    )
}

@Composable
private fun PackagingTypeChip(
    enabled: Boolean,
    selectedPackagingTypes: Set<String>,
    onClick: () -> Unit,
) {
    SearchBarChip(
        modifier = Modifier.testTag("search_bar:packaging_type_chip"),
        title = when (selectedPackagingTypes.size) {
            0 -> stringResource(Res.string.set_search_bar_chip_packaging_types)
            1 -> selectedPackagingTypes.first()
            else -> "${selectedPackagingTypes.first()} + ${selectedPackagingTypes.size - 1}"
        },
        isDefaultSelected = selectedPackagingTypes.isEmpty(),
        showTrailingIcon = true,
        enabled = enabled,
        onClick = onClick
    )
}

@Composable
private fun StatusChip(
    enabled: Boolean,
    status: StatusFilterOption,
    onClick: () -> Unit,
) {
    SearchBarChip(
        modifier = Modifier.testTag("search_bar:status_chip"),
        title = when (status) {
            StatusFilterOption.ANY_STATUS -> stringResource(Res.string.set_search_bar_chip_status)
            else -> stringResource(status.titleRes)
        },
        isDefaultSelected = status == StatusFilterOption.ANY_STATUS,
        showTrailingIcon = true,
        enabled = enabled,
        onClick = onClick
    )
}

@Composable
private fun ShowIncompleteChip(
    enabled: Boolean,
    show: Boolean,
    onClick: (Boolean) -> Unit,
) {
    SearchBarChip(
        modifier = Modifier.testTag("search_bar:show_incomplete_chip"),
        title = stringResource(Res.string.set_search_bar_chip_show_incomplete),
        isDefaultSelected = !show,
        enabled = enabled,
        onClick = { onClick(!show) }
    )
}
