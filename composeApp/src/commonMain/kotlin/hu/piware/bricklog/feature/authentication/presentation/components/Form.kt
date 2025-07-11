package hu.piware.bricklog.feature.authentication.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import bricklog.composeapp.generated.resources.authentication_password_title
import hu.piware.bricklog.feature.authentication.presentation.util.isValidEmail
import hu.piware.bricklog.feature.authentication.presentation.util.isValidPassword
import org.jetbrains.compose.resources.stringResource

@Composable
fun EmailField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onValidate: (Boolean) -> Unit,
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
                    isValid = value.isNotEmpty() && isValidEmail(value)
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
    passwordValidation: Boolean,
    onValueChange: (String) -> Unit,
    onValidate: (Boolean) -> Unit,
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
                    isValid = value.isNotEmpty() && (!passwordValidation || isValidPassword(value))
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
