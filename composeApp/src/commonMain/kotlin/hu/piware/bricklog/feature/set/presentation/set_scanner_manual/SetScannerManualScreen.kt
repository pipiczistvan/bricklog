@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.set_scanner_manual

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.example_barcode
import bricklog.composeapp.generated.resources.example_data_matrix_large
import bricklog.composeapp.generated.resources.example_data_matrix_small
import bricklog.composeapp.generated.resources.feature_set_scanner_manual_label
import bricklog.composeapp.generated.resources.feature_set_scanner_manual_label_barcode_scanning_1
import bricklog.composeapp.generated.resources.feature_set_scanner_manual_label_barcode_scanning_2
import bricklog.composeapp.generated.resources.feature_set_scanner_manual_label_barcode_scanning_3
import bricklog.composeapp.generated.resources.feature_set_scanner_manual_label_barcode_scanning_tap
import bricklog.composeapp.generated.resources.feature_set_scanner_manual_label_barcode_scanning_warning
import bricklog.composeapp.generated.resources.feature_set_scanner_manual_label_data_matrix_scanning_1
import bricklog.composeapp.generated.resources.feature_set_scanner_manual_label_data_matrix_scanning_2
import bricklog.composeapp.generated.resources.feature_set_scanner_manual_label_data_matrix_scanning_3
import bricklog.composeapp.generated.resources.feature_set_scanner_manual_label_data_matrix_scanning_info
import bricklog.composeapp.generated.resources.feature_set_scanner_manual_label_data_matrix_scanning_warning_1
import bricklog.composeapp.generated.resources.feature_set_scanner_manual_label_data_matrix_scanning_warning_2
import bricklog.composeapp.generated.resources.feature_set_scanner_manual_label_data_matrix_scanning_warning_3
import bricklog.composeapp.generated.resources.feature_set_scanner_manual_title
import bricklog.composeapp.generated.resources.feature_set_scanner_manual_title_barcode_scanning
import bricklog.composeapp.generated.resources.feature_set_scanner_manual_title_data_matrix_scanning
import bricklog.composeapp.generated.resources.feature_set_scanner_manual_title_data_matrix_scanning_supported_boxes
import hu.piware.bricklog.feature.core.presentation.components.ContentColumn
import hu.piware.bricklog.ui.theme.BricklogTheme
import hu.piware.bricklog.ui.theme.Dimens
import hu.piware.bricklog.ui.theme.Shapes
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SetScannerManualScreenRoot(
    viewModel: SetScannerManualViewModel = koinViewModel(),
    onBackClick: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    SetScannerManualScreen(
        modifier = Modifier.testTag("set_scanner_manual_screen"),
        state = state,
        onAction = { action ->
            when (action) {
                SetScannerManualAction.OnBackClick -> onBackClick()
            }
        }
    )
}

@Composable
private fun SetScannerManualScreen(
    state: SetScannerManualState,
    onAction: (SetScannerManualAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(Res.string.feature_set_scanner_manual_title))
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(SetScannerManualAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->
        ContentColumn(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding(),
                start = Dimens.MediumPadding.size,
                end = Dimens.MediumPadding.size
            )
        ) {
            Text(stringResource(Res.string.feature_set_scanner_manual_label))

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimens.SmallPadding.size)
            )

            Text(
                text = stringResource(Res.string.feature_set_scanner_manual_title_barcode_scanning),
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = buildAnnotatedString {
                    append(stringResource(Res.string.feature_set_scanner_manual_label_barcode_scanning_1))
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(stringResource(Res.string.feature_set_scanner_manual_label_barcode_scanning_2))
                    }
                    append(stringResource(Res.string.feature_set_scanner_manual_label_barcode_scanning_3))
                }
            )

            Image(
                modifier = Modifier
                    .clip(Shapes.large)
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
                painter = painterResource(Res.drawable.example_barcode),
                contentDescription = null,
            )

            Text(stringResource(Res.string.feature_set_scanner_manual_label_barcode_scanning_tap))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .padding(Dimens.SmallPadding.size),
                    text = stringResource(Res.string.feature_set_scanner_manual_label_barcode_scanning_warning)
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimens.SmallPadding.size)
            )

            Text(
                text = stringResource(Res.string.feature_set_scanner_manual_title_data_matrix_scanning),
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = buildAnnotatedString {
                    append(stringResource(Res.string.feature_set_scanner_manual_label_data_matrix_scanning_1))
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(stringResource(Res.string.feature_set_scanner_manual_label_data_matrix_scanning_2))
                    }
                    append(stringResource(Res.string.feature_set_scanner_manual_label_data_matrix_scanning_3))
                }
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .padding(Dimens.SmallPadding.size),
                    text = stringResource(Res.string.feature_set_scanner_manual_label_data_matrix_scanning_info)
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .padding(Dimens.SmallPadding.size),
                    text = buildAnnotatedString {
                        append(stringResource(Res.string.feature_set_scanner_manual_label_data_matrix_scanning_warning_1))
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(stringResource(Res.string.feature_set_scanner_manual_label_data_matrix_scanning_warning_2))
                        }
                        append(stringResource(Res.string.feature_set_scanner_manual_label_data_matrix_scanning_warning_3))
                    }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.SmallPadding.size),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Image(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(Shapes.large),
                            contentScale = ContentScale.FillWidth,
                            painter = painterResource(Res.drawable.example_data_matrix_small),
                            contentDescription = null
                        )
                        Icon(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(Dimens.ExtraLargePadding.size),
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.Red
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Image(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(Shapes.large),
                            contentScale = ContentScale.FillWidth,
                            painter = painterResource(Res.drawable.example_data_matrix_large),
                            contentDescription = null
                        )
                        Icon(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(Dimens.ExtraLargePadding.size),
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.Green
                        )
                    }
                }
            }

            Text(
                text = stringResource(Res.string.feature_set_scanner_manual_title_data_matrix_scanning_supported_boxes),
                style = MaterialTheme.typography.titleMedium
            )

            state.collectibleSetDetails.mapNotNull { it.set.name }.forEach { setName ->
                Text(setName)
            }
        }
    }
}

@Preview
@Composable
private fun SetScannerManualScreenPreview() {
    BricklogTheme {
        SetScannerManualScreen(
            state = SetScannerManualState(),
            onAction = {}
        )
    }
}
