package hu.piware.bricklog.feature.set.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_search_chip_collections
import bricklog.composeapp.generated.resources.feature_set_search_chip_packaging_types
import bricklog.composeapp.generated.resources.feature_set_search_chip_release_date
import bricklog.composeapp.generated.resources.feature_set_search_chip_show_incomplete
import bricklog.composeapp.generated.resources.feature_set_search_chip_status
import bricklog.composeapp.generated.resources.feature_set_search_chip_themes
import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.core.presentation.toLocalDateTime
import hu.piware.bricklog.feature.core.presentation.util.formatDate
import hu.piware.bricklog.feature.set.domain.model.DateFilter
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.model.SetFilterDomain
import hu.piware.bricklog.feature.set.domain.model.SetStatus
import hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.components.CollectionFilterBottomSheet
import hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.components.DateFilterBottomSheet
import hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.components.PackagingTypeFilterBottomSheet
import hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.components.SearchBarChip
import hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.components.StatusFilterBottomSheet
import hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.components.ThemeFilterBottomSheet
import hu.piware.bricklog.feature.settings.domain.model.SetFilterPreferences
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SetFilterRow(
    filterPreferences: SetFilterPreferences,
    filterOverrides: SetFilter? = null,
    onFilterPreferencesChange: (SetFilterPreferences) -> Unit,
    filterDomain: SetFilterDomain,
    modifier: Modifier = Modifier,
) {
    val mergedFilter = remember(filterPreferences, filterOverrides) {
        filterPreferences.mergeWithFilter(filterOverrides)
    }

    var showReleaseDateFilterSheet by remember { mutableStateOf(false) }
    var showStatusFilterSheet by remember { mutableStateOf(false) }
    var showThemeFilterSheet by remember { mutableStateOf(false) }
    var showPackagingTypeFilterSheet by remember { mutableStateOf(false) }
    var showCollectionFilterSheet by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
    ) {
        Spacer(Modifier.size(Dimens.SmallPadding.size))
        StatusChip(
            enabled = filterOverrides?.statuses == null,
            selectedStatuses = mergedFilter.statuses,
            onClick = { showStatusFilterSheet = true },
        )
        ReleaseDateChip(
            enabled = filterOverrides?.launchDate == null,
            dateFilter = mergedFilter.launchDate,
            onClick = { showReleaseDateFilterSheet = true },
        )
        ThemeChip(
            enabled = filterOverrides?.themes == null,
            selectedThemes = mergedFilter.themes,
            onClick = { showThemeFilterSheet = true },
        )
        PackagingTypeChip(
            enabled = filterOverrides?.packagingTypes == null,
            selectedPackagingTypes = mergedFilter.packagingTypes,
            onClick = { showPackagingTypeFilterSheet = true },
        )
        CollectionChip(
            enabled = filterOverrides?.collectionIds == null,
            selectedCollections = mergedFilter.collectionIds
                .mapNotNull { collectionId ->
                    filterDomain.collections.firstOrNull { it.id == collectionId }
                },
            onClick = { showCollectionFilterSheet = true },
        )
        ShowIncompleteChip(
            enabled = filterOverrides?.showIncomplete == null,
            show = mergedFilter.showIncomplete,
            onClick = { onFilterPreferencesChange(filterPreferences.copy(showIncomplete = it)) },
        )
        Spacer(Modifier.size(Dimens.SmallPadding.size))
    }

    if (showReleaseDateFilterSheet) {
        DateFilterBottomSheet(
            selected = filterPreferences.launchDate,
            onSelectionChange = { onFilterPreferencesChange(filterPreferences.copy(launchDate = it)) },
            onDismiss = { showReleaseDateFilterSheet = false },
        )
    }

    if (showStatusFilterSheet) {
        StatusFilterBottomSheet(
            availableOptions = SetStatus.entries,
            selected = filterPreferences.statuses,
            onSelectionChange = { onFilterPreferencesChange(filterPreferences.copy(statuses = it)) },
            onDismiss = { showStatusFilterSheet = false },
        )
    }

    if (showThemeFilterSheet) {
        ThemeFilterBottomSheet(
            availableOptions = filterDomain.themes,
            selected = filterPreferences.themes,
            onSelectionChange = { onFilterPreferencesChange(filterPreferences.copy(themes = it)) },
            onDismiss = { showThemeFilterSheet = false },
        )
    }

    if (showPackagingTypeFilterSheet) {
        PackagingTypeFilterBottomSheet(
            availableOptions = filterDomain.packagingTypes,
            selected = filterPreferences.packagingTypes,
            onSelectionChange = { onFilterPreferencesChange(filterPreferences.copy(packagingTypes = it)) },
            onDismiss = { showPackagingTypeFilterSheet = false },
        )
    }

    if (showCollectionFilterSheet) {
        CollectionFilterBottomSheet(
            availableOptions = filterDomain.collections,
            selected = filterPreferences.collectionIds,
            onSelectionChange = { onFilterPreferencesChange(filterPreferences.copy(collectionIds = it)) },
            onDismiss = { showCollectionFilterSheet = false },
        )
    }
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
            is DateFilter.AnyTime -> stringResource(Res.string.feature_set_search_chip_release_date)
            is DateFilter.Custom -> dateFilter.format()

            is DateFilter.OneWeek, DateFilter.OneMonth, DateFilter.OneYear -> stringResource(
                dateFilter.option.titleRes,
            )
        },
        isDefaultSelected = dateFilter is DateFilter.AnyTime,
        showTrailingIcon = true,
        enabled = enabled,
        onClick = onClick,
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
            0 -> stringResource(Res.string.feature_set_search_chip_themes)
            1 -> selectedThemes.first()
            else -> "${selectedThemes.first()} + ${selectedThemes.size - 1}"
        },
        isDefaultSelected = selectedThemes.isEmpty(),
        showTrailingIcon = true,
        enabled = enabled,
        onClick = onClick,
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
            0 -> stringResource(Res.string.feature_set_search_chip_packaging_types)
            1 -> selectedPackagingTypes.first()
            else -> "${selectedPackagingTypes.first()} + ${selectedPackagingTypes.size - 1}"
        },
        isDefaultSelected = selectedPackagingTypes.isEmpty(),
        showTrailingIcon = true,
        enabled = enabled,
        onClick = onClick,
    )
}

@Composable
private fun StatusChip(
    enabled: Boolean,
    selectedStatuses: Set<SetStatus>,
    onClick: () -> Unit,
) {
    SearchBarChip(
        modifier = Modifier.testTag("search_bar:status_chip"),
        title = when (selectedStatuses.size) {
            0 -> stringResource(Res.string.feature_set_search_chip_status)
            1 -> stringResource(selectedStatuses.first().statusRes)
            else -> "${stringResource(selectedStatuses.first().statusRes)} + ${selectedStatuses.size - 1}"
        },
        isDefaultSelected = selectedStatuses.isEmpty(),
        showTrailingIcon = true,
        enabled = enabled,
        onClick = onClick,
    )
}

@Composable
private fun CollectionChip(
    enabled: Boolean,
    selectedCollections: List<Collection>,
    onClick: () -> Unit,
) {
    SearchBarChip(
        modifier = Modifier.testTag("search_bar:collection_chip"),
        title = when (selectedCollections.size) {
            0 -> stringResource(Res.string.feature_set_search_chip_collections)
            1 -> selectedCollections.first().name
            else -> "${selectedCollections.first().name} + ${selectedCollections.size - 1}"
        },
        isDefaultSelected = selectedCollections.isEmpty(),
        showTrailingIcon = true,
        enabled = enabled,
        onClick = onClick,
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
        title = stringResource(Res.string.feature_set_search_chip_show_incomplete),
        isDefaultSelected = !show,
        enabled = enabled,
        onClick = { onClick(!show) },
    )
}

private fun SetFilterPreferences.mergeWithFilter(filter: SetFilter?): SetFilterPreferences {
    return this.copy(
        sortOption = filter?.sortOption ?: sortOption,
        launchDate = filter?.launchDate ?: launchDate,
        themes = filter?.themes ?: themes,
        packagingTypes = filter?.packagingTypes ?: packagingTypes,
        statuses = filter?.statuses ?: statuses,
        showIncomplete = filter?.showIncomplete ?: showIncomplete,
        collectionIds = filter?.collectionIds ?: collectionIds,
    )
}

@Preview
@Composable
private fun SetFilterRowPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
        ) {
            SetFilterRow(
                filterPreferences = SetFilterPreferences(),
                filterOverrides = null,
                onFilterPreferencesChange = {},
                filterDomain = SetFilterDomain(),
            )
        }
    }
}
