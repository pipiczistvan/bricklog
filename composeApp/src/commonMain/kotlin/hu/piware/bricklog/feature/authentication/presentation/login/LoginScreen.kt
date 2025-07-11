@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.authentication.presentation.login

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.login_button_register
import bricklog.composeapp.generated.resources.login_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginScreenRoot(
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    LoginScreen(
        modifier = Modifier.testTag("login_screen"),
        onAction = { action ->
            when (action) {
                LoginAction.OnRegisterClick -> onRegisterClick()
                LoginAction.OnBackClick -> onBackClick()
            }
        }
    )
}

@Composable
fun LoginScreen(
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(Res.string.login_title)) },
                navigationIcon = {
                    IconButton(onClick = { onAction(LoginAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { onAction(LoginAction.OnRegisterClick) }) {
                        Text(text = stringResource(Res.string.login_button_register))
                    }
                }
            )
        }
    ) {

    }
}
