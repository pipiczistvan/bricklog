@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.user.presentation.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_user_register_btn_login
import bricklog.composeapp.generated.resources.feature_user_register_title
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
                    title = { Text(text = stringResource(Res.string.feature_user_register_title)) },
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
                            Text(text = stringResource(Res.string.feature_user_register_btn_login))
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
                            RegisterAction.OnAuthenticate(
                                AuthenticationMethod.EmailPassword(email, password)
                            )
                        )
                    }
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
