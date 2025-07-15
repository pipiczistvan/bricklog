@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.user.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_user_login_btn_google_sign_in
import bricklog.composeapp.generated.resources.feature_user_login_btn_register
import bricklog.composeapp.generated.resources.feature_user_login_label_or
import bricklog.composeapp.generated.resources.feature_user_login_title
import com.mmk.kmpauth.firebase.google.GoogleButtonUiContainerFirebase
import com.mmk.kmpauth.uihelper.google.GoogleSignInButton
import hu.piware.bricklog.feature.core.presentation.components.ContentColumn
import hu.piware.bricklog.feature.core.presentation.components.LoadingOverlay
import hu.piware.bricklog.feature.core.presentation.observeAsEvents
import hu.piware.bricklog.feature.user.domain.model.AuthenticationMethod
import hu.piware.bricklog.feature.user.presentation.components.EmailPasswordForm
import hu.piware.bricklog.ui.theme.BricklogTheme
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
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
        },
        googleSignInButton = { onAction ->
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
                    text = stringResource(Res.string.feature_user_login_btn_google_sign_in),
                    onClick = ::onClick
                )
            }
        }
    )
}

@Composable
private fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    googleSignInButton: @Composable (onAction: (LoginAction) -> Unit) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    LoadingOverlay(
        isLoading = state.isLoading
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(Res.string.feature_user_login_title)) },
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
                            Text(text = stringResource(Res.string.feature_user_login_btn_register))
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
                    onSubmit = { email, password ->
                        onAction(
                            LoginAction.OnAuthenticate(
                                AuthenticationMethod.EmailPassword(email, password)
                            )
                        )
                    },
                    onPasswordResetClick = { onAction(LoginAction.OnPasswordResetClick) },
                    validatePassword = { it.isNotBlank() }
                )
                LoginOptionSeparator()
                googleSignInButton(onAction)
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
            text = stringResource(Res.string.feature_user_login_label_or)
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    BricklogTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {},
            googleSignInButton = {
                GoogleSignInButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.feature_user_login_btn_google_sign_in),
                    onClick = {}
                )
            }
        )
    }
}
