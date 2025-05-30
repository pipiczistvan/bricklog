@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.set_scanner

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.outlined.FlashlightOff
import androidx.compose.material.icons.outlined.FlashlightOn
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.piware.bricklog.feature.core.presentation.SnackbarController
import hu.piware.bricklog.feature.core.presentation.SnackbarEvent
import hu.piware.bricklog.feature.set.domain.model.setID
import hu.piware.bricklog.feature.set.presentation.set_detail.SetDetailArguments
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import qrscanner.CameraLens
import qrscanner.OverlayShape
import qrscanner.QrScanner

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
    val scope = rememberCoroutineScope()
    var flashlightOn by remember { mutableStateOf(false) }

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
                actions = {
                    IconButton(
                        onClick = {
                            flashlightOn = !flashlightOn
                        }
                    ) {
                        Icon(
                            imageVector = if (flashlightOn) Icons.Outlined.FlashlightOff else Icons.Outlined.FlashlightOn,
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
        QrScanner(
            modifier = Modifier,
            flashlightOn = flashlightOn,
            cameraLens = CameraLens.Back,
            openImagePicker = false,
            onCompletion = { scannedCode ->
                onAction(SetScannerAction.OnCodeScanned(scannedCode))
            },
            imagePickerHandler = {},
            onFailure = { error ->
                scope.launch {
                    SnackbarController.sendEvent(
                        SnackbarEvent(
                            message = error
                        )
                    )
                }
            },
            overlayShape = OverlayShape.Square
        )

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
