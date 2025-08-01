@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.user.presentation.password_reset

import androidx.compose.foundation.layout.Arrangement
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
import bricklog.composeapp.generated.resources.feature_user_password_reset_btn_submit
import bricklog.composeapp.generated.resources.feature_user_password_reset_label_instructions
import bricklog.composeapp.generated.resources.feature_user_password_reset_title
import hu.piware.bricklog.feature.core.presentation.components.ContentColumn
import hu.piware.bricklog.feature.core.presentation.components.LoadingOverlay
import hu.piware.bricklog.feature.core.presentation.observeAsEvents
import hu.piware.bricklog.feature.user.presentation.components.EmailField
import hu.piware.bricklog.ui.theme.BricklogTheme
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PasswordResetScreenRoot(
    viewModel: PasswordResetViewModel = koinViewModel(),
    onEmailSent: () -> Unit,
    onBackClick: () -> Unit,
) {
    observeAsEvents(viewModel.eventChannel) { event ->
        when (event) {
            PasswordResetEvent.EmailSent -> onEmailSent()
        }
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    PasswordResetScreen(
        modifier = Modifier.testTag("password_reset_screen"),
        state = state,
        onAction = { action ->
            when (action) {
                PasswordResetAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        },
    )
}

@Composable
private fun PasswordResetScreen(
    state: PasswordResetState,
    onAction: (PasswordResetAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    LoadingOverlay(
        isLoading = state.isLoading,
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(Res.string.feature_user_password_reset_title)) },
                    navigationIcon = {
                        IconButton(onClick = { onAction(PasswordResetAction.OnBackClick) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null,
                            )
                        }
                    },
                )
            },
        ) { padding ->
            ContentColumn(
                modifier = Modifier
                    .padding(horizontal = Dimens.MediumPadding.size)
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding(),
                ),
                verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
            ) {
                Text(text = stringResource(Res.string.feature_user_password_reset_label_instructions))
                PasswordResetForm(
                    onSubmit = { onAction(PasswordResetAction.OnResetClick(it)) },
                )
            }
        }
    }
}

@Composable
private fun PasswordResetForm(
    onSubmit: (email: String) -> Unit,
) {
    // Email field
    var email by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }

    EmailField(
        value = email,
        onValueChange = { email = it },
        onValidate = { isEmailValid = it },
    )

    // Submit button
    val focusManager = LocalFocusManager.current
    val isFormValid by derivedStateOf { isEmailValid }

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            focusManager.clearFocus()
            if (isFormValid) {
                onSubmit(email)
            }
        },
        enabled = isFormValid,
    ) {
        Text(text = stringResource(Res.string.feature_user_password_reset_btn_submit))
    }
}

@Preview
@Composable
private fun PasswordResetScreenPreview() {
    BricklogTheme {
        PasswordResetScreen(
            state = PasswordResetState(),
            onAction = {},
        )
    }
}
