package hu.piware.bricklog.feature.user.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.authentication_email_placeholder
import bricklog.composeapp.generated.resources.authentication_email_supporting_text
import bricklog.composeapp.generated.resources.authentication_email_title
import bricklog.composeapp.generated.resources.authentication_password_placeholder
import bricklog.composeapp.generated.resources.authentication_password_supporting_text
import bricklog.composeapp.generated.resources.authentication_password_title
import bricklog.composeapp.generated.resources.login_email_password_button
import bricklog.composeapp.generated.resources.login_forgot_password
import hu.piware.bricklog.feature.user.presentation.util.isValidEmail
import hu.piware.bricklog.feature.user.presentation.util.isValidPassword
import hu.piware.bricklog.ui.theme.BricklogTheme
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EmailPasswordForm(
    onSubmit: (String, String) -> Unit,
    onPasswordResetClick: (() -> Unit)? = null,
    validateEmail: (String) -> Boolean = ::isValidEmail,
    validatePassword: (String) -> Boolean = ::isValidPassword,
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
            validate = validateEmail,
            onValidate = { isEmailValid = it }
        )

        // Password field
        var password by remember { mutableStateOf("") }
        var isPasswordValid by remember { mutableStateOf(true) }

        Column {
            PasswordField(
                value = password,
                supportText = stringResource(Res.string.authentication_password_supporting_text),
                onValueChange = { password = it },
                validate = validatePassword,
                onValidate = { isPasswordValid = it }
            )

            if (onPasswordResetClick != null) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    TextButton(onClick = onPasswordResetClick) {
                        Text(text = stringResource(Res.string.login_forgot_password))
                    }
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
                if (validateEmail(email) && validatePassword(password)) {
                    onSubmit(email, password)
                }
            },
            enabled = isFormValid
        ) {
            Text(text = stringResource(Res.string.login_email_password_button))
        }
    }
}

@Composable
fun EmailField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onValidate: (Boolean) -> Unit,
    validate: (String) -> Boolean = ::isValidEmail,
) {
    val focusManager = LocalFocusManager.current

    var isValid by remember { mutableStateOf(true) }
    var hasTouched by remember { mutableStateOf(false) }

    LaunchedEffect(isValid) {
        onValidate(isValid)
    }

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged {
                if (it.isFocused) {
                    hasTouched = true
                }
                if (!it.isFocused && hasTouched) {
                    isValid = validate(value)
                }
            },
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = stringResource(Res.string.authentication_email_placeholder)) },
        label = { Text(text = stringResource(Res.string.authentication_email_title)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Next)
            }
        ),
        supportingText = {
            if (!isValid) {
                Text(text = stringResource(Res.string.authentication_email_supporting_text))
            }
        },
        isError = !isValid
    )
}

@Composable
fun PasswordField(
    modifier: Modifier = Modifier,
    value: String,
    supportText: String,
    onValueChange: (String) -> Unit,
    onValidate: (Boolean) -> Unit,
    validate: (String) -> Boolean = ::isValidPassword,
) {
    val focusManager = LocalFocusManager.current

    var isValid by remember { mutableStateOf(true) }
    var hasTouched by remember { mutableStateOf(false) }
    var visibility by remember { mutableStateOf(false) }

    LaunchedEffect(isValid) {
        onValidate(isValid)
    }

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged {
                if (it.isFocused) {
                    hasTouched = true
                }
                if (!it.isFocused && hasTouched) {
                    isValid = validate(value)
                }
            },
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = stringResource(Res.string.authentication_password_placeholder)) },
        label = { Text(text = stringResource(Res.string.authentication_password_title)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(
                onClick = {
                    visibility = !visibility
                }
            ) {
                Icon(
                    imageVector = if (visibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = null
                )
            }
        },
        supportingText = {
            if (!isValid) {
                Text(text = supportText)
            }
        },
        isError = !isValid
    )
}

@Preview
@Composable
private fun EmailPasswordFormPreview() {
    BricklogTheme {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            EmailPasswordForm(
                onSubmit = { _, _ -> }
            )
        }
    }
}
