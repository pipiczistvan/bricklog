@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.user.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.buildAnnotatedString
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_user_details_label_uid
import bricklog.composeapp.generated.resources.feature_user_details_title
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.presentation.components.ContentColumn
import hu.piware.bricklog.feature.core.presentation.components.LoadingOverlay
import hu.piware.bricklog.feature.core.presentation.components.SupportingRow
import hu.piware.bricklog.feature.core.presentation.observeAsEvents
import hu.piware.bricklog.feature.set.domain.usecase.ValidateUserName
import hu.piware.bricklog.feature.user.domain.model.User
import hu.piware.bricklog.feature.user.domain.model.isAuthenticated
import hu.piware.bricklog.feature.user.presentation.details.components.UserDeleteConfirmDialog
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UserDetailsScreenRoot(
    viewModel: UserDetailsViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit,
    onUserDeleted: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    observeAsEvents(viewModel.eventChannel) { event ->
        when (event) {
            UserDetailsEvent.Back -> onBackClick()
            UserDetailsEvent.LoginProposed -> onLoginClick()
            UserDetailsEvent.UserDeleted -> onUserDeleted()
        }
    }
    val validateUserName: ValidateUserName = koinInject()

    UserDetailsScreen(
        modifier = Modifier.testTag("user_details_screen"),
        state = state,
        onAction = { action ->
            when (action) {
                UserDetailsAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        },
        validateFields = { userName ->
            validateUserName(userName) is Result.Success
        },
    )
}

@Composable
private fun UserDetailsScreen(
    state: UserDetailsState,
    onAction: (UserDetailsAction) -> Unit,
    validateFields: (String) -> Boolean,
    modifier: Modifier = Modifier,
) {
    LoadingOverlay(
        isLoading = state.isLoading,
    ) {
        var showDeleteConfirmDialog by remember { mutableStateOf(false) }

        var userName by rememberSaveable(state.userPreferences.displayName) {
            mutableStateOf(state.userPreferences.displayName ?: "")
        }

        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(Res.string.feature_user_details_title)) },
                    navigationIcon = {
                        IconButton(onClick = { onAction(UserDetailsAction.OnBackClick) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null,
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                onAction(
                                    UserDetailsAction.OnUserPreferencesChange(
                                        state.userPreferences.copy(
                                            displayName = userName,
                                        ),
                                    ),
                                )
                            },
                            enabled = validateFields(userName),
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Save,
                                contentDescription = null,
                            )
                        }

                        if (state.currentUser.isAuthenticated) {
                            IconButton(onClick = { showDeleteConfirmDialog = true }) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = null,
                                )
                            }
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
                UserDetailsContent(
                    user = state.currentUser,
                )

                UserNameField(
                    value = userName,
                    onValueChange = { userName = it },
                )
            }
        }

        if (showDeleteConfirmDialog) {
            UserDeleteConfirmDialog(
                onConfirmation = {
                    showDeleteConfirmDialog = false
                    onAction(UserDetailsAction.OnUserDelete)
                },
                onDismiss = {
                    showDeleteConfirmDialog = false
                },
            )
        }
    }
}

@Composable
private fun UserDetailsContent(
    user: User,
) {
    // Using deprecated manager because new clipboard API seems unfinished for KMP
    val clipboardManager = LocalClipboardManager.current

    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.MediumPadding.size),
        ) {
            Text(
                text = stringResource(Res.string.feature_user_details_label_uid),
                style = MaterialTheme.typography.titleMedium,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = user.uid,
                )
                IconButton(
                    onClick = {
                        clipboardManager.setText(buildAnnotatedString { append(user.uid) })
                    },
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ContentCopy,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Composable
private fun UserNameField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier.padding(vertical = Dimens.SmallPadding.size),
            text = "User name", // TODO: localize
            style = MaterialTheme.typography.titleMedium,
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            placeholder = { "Enter user name" },
            singleLine = true,
        )
        SupportingRow {
            Text(
                text = "${value.length}/${ValidateUserName.MAX_LENGTH}",
            )
        }
    }
}
