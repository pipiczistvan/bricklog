@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.authentication.presentation.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.register_button_login
import bricklog.composeapp.generated.resources.register_email_password_button
import bricklog.composeapp.generated.resources.register_password_supporting_text
import bricklog.composeapp.generated.resources.register_title
import hu.piware.bricklog.feature.authentication.domain.model.AuthenticationMethod
import hu.piware.bricklog.feature.authentication.presentation.components.EmailField
import hu.piware.bricklog.feature.authentication.presentation.components.PasswordField
import hu.piware.bricklog.feature.authentication.presentation.util.isValidEmail
import hu.piware.bricklog.feature.authentication.presentation.util.isValidPassword
import hu.piware.bricklog.feature.core.presentation.components.ContentColumn
import hu.piware.bricklog.feature.core.presentation.components.LoadingOverlay
import hu.piware.bricklog.feature.core.presentation.observeAsEvents
import hu.piware.bricklog.ui.theme.BricklogTheme
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreenRoot(
    viewModel: RegisterViewModel = koinViewModel(),
    onLoginClick: () -> Unit,
    onUserRegistered: () -> Unit,
    onBackClick: () -> Unit,
) {
    observeAsEvents(viewModel.eventChannel) { event ->
        when (event) {
            RegisterEvent.UserRegistered -> onUserRegistered()
        }
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    RegisterScreen(
        modifier = Modifier.testTag("register_screen"),
        state = state,
        onAction = { action ->
            when (action) {
                RegisterAction.OnBackClick -> onBackClick()
                RegisterAction.OnLoginClick -> onLoginClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    LoadingOverlay(
        isLoading = state.isLoading
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
                    },
                    actions = {
                        TextButton(onClick = { onAction(RegisterAction.OnLoginClick) }) {
                            Text(text = stringResource(Res.string.register_button_login))
                        }
                    }
                )
            }
        ) { padding ->
            ContentColumn(
                modifier = Modifier
                    .padding(horizontal = Dimens.MediumPadding.size)
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                ),
                verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size)
            ) {
                EmailPasswordForm(
                    onAction = onAction
                )
            }
        }
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    BricklogTheme {
        RegisterScreen(
            state = RegisterState(),
            onAction = {}
        )
    }
}

@Composable
private fun EmailPasswordForm(
    onAction: (RegisterAction) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size)
    ) {
        // Email field
        var email by remember { mutableStateOf("") }
        var isEmailValid by remember { mutableStateOf(true) }

        EmailField(
            value = email,
            onValueChange = { email = it },
            onValidate = { isEmailValid = it }
        )

        // Password field
        var password by remember { mutableStateOf("") }
        var isPasswordValid by remember { mutableStateOf(true) }

        PasswordField(
            value = password,
            supportText = stringResource(Res.string.register_password_supporting_text),
            passwordValidation = true,
            onValueChange = { password = it },
            onValidate = { isPasswordValid = it }
        )

        // Submit button
        val focusManager = LocalFocusManager.current
        val isFormValid by derivedStateOf { isEmailValid && isPasswordValid }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                focusManager.clearFocus()
                if (isValidEmail(email) && isValidPassword(password)) {
                    onAction(
                        RegisterAction.OnAuthenticate(
                            AuthenticationMethod.EmailPassword(
                                email,
                                password
                            )
                        )
                    )
                }
            },
            enabled = isFormValid
        ) {
            Text(text = stringResource(Res.string.register_email_password_button))
        }
    }
}
