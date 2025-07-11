@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.authentication.presentation.register

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.register_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun RegisterScreenRoot(
    onBackClick: () -> Unit,
) {
    RegisterScreen(
        modifier = Modifier.testTag("register_screen"),
        onAction = { action ->
            when (action) {
                RegisterAction.OnBackClick -> onBackClick()
            }
        }
    )
}

@Composable
fun RegisterScreen(
    onAction: (RegisterAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(Res.string.register_title)) },
                navigationIcon = {
                    IconButton(onClick = { onAction(RegisterAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {

    }
}

