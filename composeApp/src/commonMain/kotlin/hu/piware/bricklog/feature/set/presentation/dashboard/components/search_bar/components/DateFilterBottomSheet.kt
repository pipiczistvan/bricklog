@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.date_filter_sheet_title
import hu.piware.bricklog.feature.core.presentation.components.BottomSheetHeader
import hu.piware.bricklog.feature.core.presentation.components.BottomSheetOption
import hu.piware.bricklog.feature.core.presentation.components.DateRangePickerModal
import hu.piware.bricklog.feature.set.domain.model.DateFilter
import hu.piware.bricklog.feature.set.domain.model.DateFilterOption
import hu.piware.bricklog.ui.theme.Dimens
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun DateFilterBottomSheet(
    onShowBottomSheetChanged: (Boolean) -> Unit,
    selected: DateFilter,
    onSelectionChange: (DateFilter) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var customDateRangePickerVisible by remember { mutableStateOf(false) }

    ModalBottomSheet(
        modifier = Modifier.testTag("search_bar:date_filter_bottom_sheet"),
        onDismissRequest = {
            onShowBottomSheetChanged(false)
        },
        sheetState = sheetState
    ) {
        BottomSheetHeader(
            title = stringResource(Res.string.date_filter_sheet_title),
            onCloseClick = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        onShowBottomSheetChanged(false)
                    }
                }
            }
        )

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.MediumPadding.size)
        ) {
            items(DateFilterOption.entries.toTypedArray()) { option ->
                DateFilterSheetOption(
                    option = option,
                    selected = option == selected.option,
                    onClick = {
                        when (option) {
                            DateFilterOption.CUSTOM -> {
                                customDateRangePickerVisible = true
                            }

                            else -> {
                                val filter = when (option) {
                                    DateFilterOption.ANY_TIME -> DateFilter.AnyTime
                                    DateFilterOption.ONE_WEEK -> DateFilter.OneWeek
                                    DateFilterOption.ONE_MONTH -> DateFilter.OneMonth
                                    DateFilterOption.ONE_YEAR -> DateFilter.OneYear
                                    DateFilterOption.CUSTOM -> throw IllegalStateException("Custom filter should be handled separately.")
                                }
                                onSelectionChange(filter)
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        onShowBottomSheetChanged(false)
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    }

    if (customDateRangePickerVisible) {
        val customDateRangeFilter = when (selected) {
            is DateFilter.Custom -> selected
            else -> null
        }
        val dateRangePickerState = rememberDateRangePickerState(
            customDateRangeFilter?.startDate,
            customDateRangeFilter?.endDate
        )

        DateRangePickerModal(
            state = dateRangePickerState,
            onDateRangeSelected = {
                onSelectionChange(DateFilter.Custom(it.first, it.second))
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        onShowBottomSheetChanged(false)
                    }
                }
                customDateRangePickerVisible = false
            },
            onDismiss = {
                customDateRangePickerVisible = false
            }
        )
    }
}

@Composable
private fun DateFilterSheetOption(
    option: DateFilterOption,
    selected: Boolean,
    onClick: () -> Unit,
) {
    BottomSheetOption(
        selected = selected,
        onClick = onClick
    ) {
        Spacer(modifier = Modifier.width(Dimens.MediumPadding.size))
        Text(
            text = stringResource(option.titleRes),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
