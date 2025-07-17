@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.set_scanner

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.icerock.moko.permissions.PermissionsController
import hu.piware.barcode_scanner.BarcodeScannerWithPermission
import hu.piware.bricklog.feature.set.domain.model.setID
import hu.piware.bricklog.feature.set.presentation.set_detail.SetDetailArguments
import hu.piware.bricklog.feature.set.presentation.set_scanner.components.SetDetectionBox
import hu.piware.bricklog.feature.set.presentation.set_scanner.util.supportedBarcodeFormats
import hu.piware.bricklog.ui.theme.BricklogTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SetScannerScreenRoot(
    viewModel: SetScannerViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onSetClick: (SetDetailArguments) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    SetScannerScreen(
        modifier = Modifier
            .testTag("set_scanner_screen"),
        state = state,
        onAction = { action ->
            when (action) {
                SetScannerAction.OnBackClick -> onBackClick()
                is SetScannerAction.OnSetClick -> onSetClick(
                    SetDetailArguments(
                        action.id,
                        "set_scanner"
                    )
                )

                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun SetScannerScreen(
    state: SetScannerState,
    onAction: (SetScannerAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { onAction(SetScannerAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) {
        BarcodeScannerWithPermission(
            permissionsController = koinInject<PermissionsController>(),
            onScanResult = { results ->
                onAction(SetScannerAction.OnBarcodeDetected(results))
            },
            formats = supportedBarcodeFormats,
        )

        Box(Modifier.fillMaxSize()) {
            for (detection in state.detections) {
                SetDetectionBox(
                    detection = detection,
                    onClick = { set ->
                        onAction(SetScannerAction.OnSetClick(set.setID))
                    }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.BottomCenter
        ) {
            if (state.setDetails?.set?.name != null) {
                Button(
                    modifier = Modifier
                        .padding(16.dp),
                    onClick = {
                        onAction(SetScannerAction.OnSetClick(state.setDetails.setID))
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = null
                    )
                    Text(
                        text = state.setDetails.set.name,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SetScannerScreenPreview() {
    BricklogTheme {
        SetScannerScreen(
            state = SetScannerState(),
            onAction = {}
        )
    }
}
