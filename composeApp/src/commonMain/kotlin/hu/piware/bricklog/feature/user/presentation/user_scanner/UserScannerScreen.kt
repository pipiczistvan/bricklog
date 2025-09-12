@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.user.presentation.user_scanner

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import dev.icerock.moko.permissions.PermissionsController
import hu.piware.barcode_scanner.BarcodeFormat
import hu.piware.barcode_scanner.BarcodeScannerWithPermission
import hu.piware.bricklog.feature.core.presentation.observeAsEvents
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UserScannerScreenRoot(
    viewModel: UserScannerViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onUserScanned: (String, String) -> Unit,
) {
    observeAsEvents(viewModel.eventChannel) { event ->
        when (event) {
            is UserScannerEvent.UserScanned -> onUserScanned(event.userId, event.userName)
        }
    }

    UserScannerScreen(
        modifier = Modifier
            .testTag("user_scanner_screen"),
        onAction = { action ->
            when (action) {
                UserScannerAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        },
    )
}

@Composable
private fun UserScannerScreen(
    onAction: (UserScannerAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { onAction(UserScannerAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White,
                ),
            )
        },
    ) {
        BarcodeScannerWithPermission(
            permissionsController = koinInject<PermissionsController>(),
            onScanResult = { onAction(UserScannerAction.OnBarcodeDetected(it)) },
            formats = listOf(BarcodeFormat.QR),
        )
    }
}
