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
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource

@Composable
fun SetFilterRow(
    filter: SetFilter,
    onFilterChange: (SetFilter) -> Unit,
    onThemeMultiselectClick: (() -> Unit)?,
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
            status = filter.status,
            onClick = { showStatusFilterSheet = true }
        )
        ReleaseDateChip(
            dateFilter = filter.launchDate,
            onClick = { showReleaseDateFilterSheet = true }
        )
        if (onThemeMultiselectClick != null) {
            ThemeChip(
                selectedThemes = filter.themes,
                onClick = onThemeMultiselectClick
            )
        }
        ShowIncompleteChip(
            show = filter.showIncomplete,
            onClick = { onFilterChange(filter.copy(showIncomplete = it)) }
        )
        Spacer(Modifier.size(Dimens.SmallPadding.size))
    }

    DateFilterBottomSheet(
        showBottomSheet = showReleaseDateFilterSheet,
        onShowBottomSheetChanged = { showReleaseDateFilterSheet = it },
        selectedFilter = filter.launchDate,
        onSelectFilter = { onFilterChange(filter.copy(launchDate = it)) }
    )

    StatusFilterBottomSheet(
        showBottomSheet = showStatusFilterSheet,
        onShowBottomSheetChanged = { showStatusFilterSheet = it },
        selectedOption = filter.status,
        onSelectOption = { onFilterChange(filter.copy(status = it)) }
    )
}

@Composable
private fun ReleaseDateChip(
    dateFilter: DateFilter,
    onClick: () -> Unit,
) {
    SearchBarChip(
        modifier = Modifier.testTag("search_bar:release_date_chip"),
        title = when (dateFilter) {
            is DateFilter.AnyTime -> stringResource(Res.string.set_search_bar_chip_release_date)
            is DateFilter.Custom -> "${formatDate(dateFilter.startDate.toLocalDateTime())} - ${
                formatDate(
                    dateFilter.endDate.toLocalDateTime()
                )
            }"

            is DateFilter.OneWeek, DateFilter.OneMonth, DateFilter.OneYear -> stringResource(
                dateFilter.option.titleRes
            )
        },
        isDefaultSelected = dateFilter is DateFilter.AnyTime,
        showTrailingIcon = true,
        onClick = onClick
    )
}

@Composable
private fun ThemeChip(
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
        onClick = onClick
    )
}

@Composable
private fun StatusChip(
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
        onClick = onClick
    )
}

@Composable
private fun ShowIncompleteChip(
    show: Boolean,
    onClick: (Boolean) -> Unit,
) {
    SearchBarChip(
        modifier = Modifier.testTag("search_bar:show_incomplete_chip"),
        title = stringResource(Res.string.set_search_bar_chip_show_incomplete),
        isDefaultSelected = !show,
        onClick = { onClick(!show) }
    )
}
