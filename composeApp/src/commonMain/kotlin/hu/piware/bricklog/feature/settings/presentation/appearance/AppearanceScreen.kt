@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.settings.presentation.appearance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import bricklog.composeapp.generated.resources.feature_settings_appearance_title
import bricklog.composeapp.generated.resources.feature_settings_appearance_title_greetings
import bricklog.composeapp.generated.resources.feature_settings_appearance_title_theme
import hu.piware.bricklog.feature.core.presentation.components.ContentColumn
import hu.piware.bricklog.feature.core.presentation.components.LoadingOverlay
import hu.piware.bricklog.feature.settings.domain.model.ThemeOption
import hu.piware.bricklog.feature.settings.domain.model.UserPreferences
import hu.piware.bricklog.feature.user.presentation.components.NameField
import hu.piware.bricklog.feature.user.presentation.util.isValidName
import hu.piware.bricklog.ui.theme.BricklogTheme
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppearanceScreenRoot(
    viewModel: AppearanceViewModel = koinViewModel(),
    onBackClick: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    AppearanceScreen(
        modifier = Modifier.testTag("appearance_screen"),
        state = state,
        onAction = { action ->
            when (action) {
                AppearanceAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun AppearanceScreen(
    state: AppearanceState,
    onAction: (AppearanceAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    LoadingOverlay(
        isLoading = state.isLoading
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    title = {
                        Text(stringResource(Res.string.feature_settings_appearance_title))
                    },
                    navigationIcon = {
                        IconButton(onClick = { onAction(AppearanceAction.OnBackClick) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null
                            )
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
                )
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)
                ) {
                    if (state.currentUser != null) {
                        GreetingsSettings(
                            userPreferences = state.userPreferences,
                            onUserPreferencesChange = { preferences, showLoading ->
                                onAction(
                                    AppearanceAction.OnUserPreferencesChange(
                                        preferences,
                                        showLoading
                                    )
                                )
                            }
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = Dimens.SmallPadding.size)
                        )
                    }

                    ThemeSettings(
                        theme = state.themeOption,
                        onThemeChange = {
                            onAction(AppearanceAction.OnThemeOptionChange(it))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun GreetingsSettings(
    userPreferences: UserPreferences,
    onUserPreferencesChange: (UserPreferences, Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(Res.string.feature_settings_appearance_title_greetings),
            style = MaterialTheme.typography.titleLarge
        )

        Switch(
            checked = userPreferences.showGreetings,
            onCheckedChange = {
                onUserPreferencesChange(userPreferences.copy(showGreetings = it), false)
            }
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val focusManager = LocalFocusManager.current

        var displayName by remember { mutableStateOf("") }
        var isDisplayNameValid by remember { mutableStateOf(true) }
        NameField(
            modifier = Modifier.weight(1f),
            value = displayName,
            onValueChange = { displayName = it },
            labelValue = userPreferences.displayName?.ifEmpty { null },
            onValidate = { isDisplayNameValid = it },
        )

        FilledIconButton(
            onClick = {
                focusManager.clearFocus()
                if (isValidName(displayName)) {
                    onUserPreferencesChange(userPreferences.copy(displayName = displayName), true)
                }
            },
            enabled = isDisplayNameValid
        ) {
            Icon(
                imageVector = Icons.Outlined.Save,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun ThemeSettings(
    theme: ThemeOption,
    onThemeChange: (ThemeOption) -> Unit,
) {
    Text(
        text = stringResource(Res.string.feature_settings_appearance_title_theme),
        style = MaterialTheme.typography.titleLarge
    )

    val choices = ThemeOption.entries.toTypedArray()
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        choices.forEachIndexed { index, themeOption ->
            SegmentedButton(
                modifier = Modifier.testTag("theme_settings:theme_option"),
                selected = theme == themeOption,
                onClick = {
                    onThemeChange(themeOption)
                },
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = choices.count()
                )
            ) {
                Text(stringResource(themeOption.titleRes))
            }
        }
    }
}

@Preview
@Composable
private fun AppearanceScreenPreview() {
    BricklogTheme {
        AppearanceScreen(
            state = AppearanceState(),
            onAction = {}
        )
    }
}
