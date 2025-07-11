@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.authentication.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.authentication_google_button
import bricklog.composeapp.generated.resources.authentication_password_supporting_text
import bricklog.composeapp.generated.resources.authentication_separator_title
import bricklog.composeapp.generated.resources.login_button_register
import bricklog.composeapp.generated.resources.login_email_password_button
import bricklog.composeapp.generated.resources.login_forgot_password
import bricklog.composeapp.generated.resources.login_title
import com.mmk.kmpauth.firebase.google.GoogleButtonUiContainerFirebase
import com.mmk.kmpauth.uihelper.google.GoogleSignInButton
import hu.piware.bricklog.feature.authentication.domain.model.AuthenticationMethod
import hu.piware.bricklog.feature.authentication.presentation.components.EmailField
import hu.piware.bricklog.feature.authentication.presentation.components.PasswordField
import hu.piware.bricklog.feature.authentication.presentation.util.isValidEmail
import hu.piware.bricklog.feature.core.presentation.components.ContentColumn
import hu.piware.bricklog.feature.core.presentation.components.LoadingOverlay
import hu.piware.bricklog.feature.core.presentation.observeAsEvents
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel = koinViewModel(),
    onRegisterClick: () -> Unit,
    onPasswordResetClick: () -> Unit,
    onUserLoggedIn: () -> Unit,
    onBackClick: () -> Unit,
) {
    observeAsEvents(viewModel.eventChannel) { event ->
        when (event) {
            LoginEvent.UserLoggedIn -> onUserLoggedIn()
        }
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LoginScreen(
        modifier = Modifier.testTag("login_screen"),
        state = state,
        onAction = { action ->
            when (action) {
                LoginAction.OnRegisterClick -> onRegisterClick()
                LoginAction.OnPasswordResetClick -> onPasswordResetClick()
                LoginAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    LoadingOverlay(
        isLoading = state.isLoading
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
                EmailPasswordForm(onAction = onAction)
                LoginOptionSeparator()
                GoogleButtonUiContainerFirebase(
                    linkAccount = true,
                    onResult = {
                        onAction(
                            LoginAction.OnAuthenticate(
                                AuthenticationMethod.GoogleSignIn(it)
                            )
                        )
                    }
                ) {
                    GoogleSignInButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.authentication_google_button),
                        onClick = ::onClick
                    )
                }
            }
        }
    }
}

@Composable
private fun LoginOptionSeparator() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f)
        )
        Text(
            modifier = Modifier.padding(horizontal = Dimens.MediumPadding.size),
            text = stringResource(Res.string.authentication_separator_title)
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun EmailPasswordForm(
    onAction: (LoginAction) -> Unit,
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
        Column {
            PasswordField(
                value = password,
                supportText = stringResource(Res.string.authentication_password_supporting_text),
                passwordValidation = false,
                onValueChange = { password = it },
                onValidate = { isPasswordValid = it }
            )

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                TextButton(onClick = { onAction(LoginAction.OnPasswordResetClick) }) {
                    Text(text = stringResource(Res.string.login_forgot_password))
                }
            }
        }

        // Submit button
        val focusManager = LocalFocusManager.current
        val isFormValid by derivedStateOf { isEmailValid && isPasswordValid }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                focusManager.clearFocus()
                if (isValidEmail(email) && password.isNotEmpty()) {
                    onAction(
                        LoginAction.OnAuthenticate(
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
            Text(text = stringResource(Res.string.login_email_password_button))
        }
    }
}
