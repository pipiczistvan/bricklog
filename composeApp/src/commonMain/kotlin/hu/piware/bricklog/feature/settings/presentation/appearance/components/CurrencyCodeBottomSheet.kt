@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.settings.presentation.appearance.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_settings_appearance_currency_code_bottom_sheet_title
import hu.piware.bricklog.feature.core.presentation.components.BottomSheetHeader
import hu.piware.bricklog.feature.core.presentation.components.BottomSheetOption
import hu.piware.bricklog.feature.currency.domain.util.CURRENCY_OPTIONS
import hu.piware.bricklog.ui.theme.BricklogTheme
import hu.piware.bricklog.ui.theme.Dimens
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CurrencyCodeBottomSheet(
    availableOptions: List<String>,
    selected: String,
    onSelectionChange: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        modifier = Modifier.testTag("appearance:currency_code_bottom_sheet"),
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        CurrencyCodeSheetContent(
            availableOptions = availableOptions,
            selected = selected,
            onSelectionChange = onSelectionChange,
            sheetState = sheetState,
            onDismiss = onDismiss,
        )
    }
}

@Composable
private fun CurrencyCodeSheetContent(
    availableOptions: List<String>,
    selected: String,
    onSelectionChange: (String) -> Unit,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier,
    ) {
        BottomSheetHeader(
            title = stringResource(Res.string.feature_settings_appearance_currency_code_bottom_sheet_title),
            sheetState = sheetState,
            onDismiss = onDismiss,
        )

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.MediumPadding.size),
        ) {
            items(availableOptions) { option ->
                CurrencyCodeSheetOption(
                    option = option,
                    selected = option == selected,
                    onClick = {
                        onSelectionChange(option)
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                onDismiss()
                            }
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun CurrencyCodeSheetOption(
    option: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    BottomSheetOption(
        selected = selected,
        onClick = onClick,
    ) {
        Spacer(modifier = Modifier.width(Dimens.MediumPadding.size))
        Text(
            text = option,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview
@Composable
private fun CurrencyCodeSheetContentPreview() {
    BricklogTheme {
        CurrencyCodeSheetContent(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            availableOptions = CURRENCY_OPTIONS,
            selected = CURRENCY_OPTIONS.first(),
            onSelectionChange = {},
            sheetState = rememberModalBottomSheetState(),
            onDismiss = {},
        )
    }
}
