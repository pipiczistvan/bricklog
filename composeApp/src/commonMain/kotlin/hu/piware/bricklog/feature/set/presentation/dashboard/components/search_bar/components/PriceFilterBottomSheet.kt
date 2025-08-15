@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_search_price_filter_custom_dialog_btn_cancel
import bricklog.composeapp.generated.resources.feature_set_search_price_filter_custom_dialog_btn_confirm
import bricklog.composeapp.generated.resources.feature_set_search_price_filter_custom_dialog_field_max_title
import bricklog.composeapp.generated.resources.feature_set_search_price_filter_custom_dialog_field_min_invalid
import bricklog.composeapp.generated.resources.feature_set_search_price_filter_custom_dialog_field_min_title
import bricklog.composeapp.generated.resources.feature_set_search_price_filter_custom_dialog_title
import bricklog.composeapp.generated.resources.feature_set_search_price_filter_sheet_title
import hu.piware.bricklog.feature.core.presentation.components.BottomSheetHeader
import hu.piware.bricklog.feature.core.presentation.components.BottomSheetOption
import hu.piware.bricklog.feature.currency.domain.model.CurrencyPreferenceDetails
import hu.piware.bricklog.feature.set.domain.model.PriceFilter
import hu.piware.bricklog.feature.set.domain.model.PriceFilterOption
import hu.piware.bricklog.feature.set.domain.util.currencyPriceInRegion
import hu.piware.bricklog.feature.set.domain.util.regionPriceInCurrency
import hu.piware.bricklog.feature.set.presentation.components.formatedPriceRange
import hu.piware.bricklog.ui.theme.Dimens
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun PriceFilterBottomSheet(
    currencyDetails: CurrencyPreferenceDetails,
    selected: PriceFilter,
    onSelectionChange: (PriceFilter) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var customPriceRangePickerVisible by remember { mutableStateOf(false) }

    ModalBottomSheet(
        modifier = Modifier.testTag("search_bar:price_filter_bottom_sheet"),
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column {
            BottomSheetHeader(
                title = stringResource(Res.string.feature_set_search_price_filter_sheet_title),
                sheetState = sheetState,
                onDismiss = onDismiss,
            )

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.MediumPadding.size),
            ) {
                items(PriceFilterOption.entries.toTypedArray()) { option ->
                    PriceFilterSheetOption(
                        option = option,
                        isSelected = option == selected.option,
                        currencyDetails = currencyDetails,
                        onClick = {
                            when (option) {
                                PriceFilterOption.CUSTOM -> {
                                    customPriceRangePickerVisible = true
                                }

                                else -> {
                                    val filter = when (option) {
                                        PriceFilterOption.ANY_PRICE -> PriceFilter.AnyPrice
                                        PriceFilterOption.PREMIUM -> PriceFilter.Premium
                                        PriceFilterOption.EXPENSIVE -> PriceFilter.Expensive
                                        PriceFilterOption.AFFORDABLE -> PriceFilter.Affordable
                                        PriceFilterOption.BUDGET -> PriceFilter.Budget
                                        PriceFilterOption.CUSTOM -> throw IllegalStateException(
                                            "Custom filter should be handled separately.",
                                        )
                                    }
                                    onSelectionChange(filter)
                                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            onDismiss()
                                        }
                                    }
                                }
                            }
                        },
                    )
                }
            }

            if (customPriceRangePickerVisible) {
                val customPriceRangeFilter = when (selected) {
                    is PriceFilter.Custom -> selected
                    else -> null
                }
                CustomPriceRangeDialog(
                    initialRegionalMin = customPriceRangeFilter?.min,
                    initialRegionalMax = customPriceRangeFilter?.max,
                    currencyDetails = currencyDetails,
                    onDismiss = {
                        customPriceRangePickerVisible = false
                    },
                    onConfirm = { min, max ->
                        onSelectionChange(PriceFilter.Custom(min, max))
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                onDismiss()
                            }
                        }
                        customPriceRangePickerVisible = false
                    },
                )
            }
        }
    }
}

@Composable
private fun PriceFilterSheetOption(
    option: PriceFilterOption,
    currencyDetails: CurrencyPreferenceDetails,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    BottomSheetOption(
        isSelected = isSelected,
        onClick = onClick,
    ) {
        Column {
            Text(
                text = stringResource(option.titleRes),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = option.format(currencyDetails),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun PriceFilterOption.format(currencyDetails: CurrencyPreferenceDetails): String {
    return when (this) {
        PriceFilterOption.BUDGET,
        PriceFilterOption.AFFORDABLE,
        PriceFilterOption.EXPENSIVE,
        PriceFilterOption.PREMIUM,
            -> currencyDetails.priceRanges.getOrElse(this) {
            throw IllegalStateException("Price filter option not found.")
        }.let { (min, max) ->
            formatedPriceRange(min, max, currencyDetails)
        }

        else -> ""
    }
}

@Composable
fun CustomPriceRangeDialog(
    initialRegionalMin: Double?,
    initialRegionalMax: Double?,
    currencyDetails: CurrencyPreferenceDetails,
    onDismiss: () -> Unit,
    onConfirm: (Double?, Double?) -> Unit,
) {
    val initialCurrencyMin =
        remember { regionPriceInCurrency(initialRegionalMin, currencyDetails)?.roundTo2Decimals() }
    val initialCurrencyMax =
        remember { regionPriceInCurrency(initialRegionalMax, currencyDetails)?.roundTo2Decimals() }

    var minValue by remember { mutableStateOf(initialCurrencyMin?.toString() ?: "") }
    var maxValue by remember { mutableStateOf(initialCurrencyMax?.toString() ?: "") }
    var isValid by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.feature_set_search_price_filter_custom_dialog_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = minValue,
                    onValueChange = { input ->
                        if (input.isEmpty() || input.matches(Regex("^\\d*\\.?\\d*$"))) {
                            minValue = input
                        }
                    },
                    label = { Text(stringResource(Res.string.feature_set_search_price_filter_custom_dialog_field_min_title)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                    prefix = { Text(currencyDetails.preferredCurrencyCode.uppercase()) },
                    isError = !isValid,
                    supportingText = {
                        if (!isValid) {
                            Text(text = stringResource(Res.string.feature_set_search_price_filter_custom_dialog_field_min_invalid))
                        }
                    },
                )
                OutlinedTextField(
                    value = maxValue,
                    onValueChange = { input ->
                        if (input.isEmpty() || input.matches(Regex("^\\d*\\.?\\d*$"))) {
                            maxValue = input
                        }
                    },
                    label = { Text(stringResource(Res.string.feature_set_search_price_filter_custom_dialog_field_max_title)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                    prefix = { Text(currencyDetails.preferredCurrencyCode.uppercase()) },
                    isError = !isValid,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val min = minValue.toDoubleOrNull()
                val max = maxValue.toDoubleOrNull()

                isValid = min == null || max == null || max >= min
                if (!isValid) return@TextButton

                val regionalMin = currencyPriceInRegion(min, currencyDetails)
                val regionalMax = currencyPriceInRegion(max, currencyDetails)
                onConfirm(regionalMin, regionalMax)
            }) {
                Text(stringResource(Res.string.feature_set_search_price_filter_custom_dialog_btn_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.feature_set_search_price_filter_custom_dialog_btn_cancel))
            }
        },
    )
}

fun Double.roundTo2Decimals(): Double {
    return (kotlin.math.round(this * 100) / 100)
}
