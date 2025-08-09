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
import androidx.compose.material3.FilledTonalButton
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
import bricklog.composeapp.generated.resources.feature_settings_appearance_label_currency_region
import bricklog.composeapp.generated.resources.feature_settings_appearance_label_currency_target
import bricklog.composeapp.generated.resources.feature_settings_appearance_title
import bricklog.composeapp.generated.resources.feature_settings_appearance_title_currency
import bricklog.composeapp.generated.resources.feature_settings_appearance_title_dashboard
import bricklog.composeapp.generated.resources.feature_settings_appearance_title_featured_sets
import bricklog.composeapp.generated.resources.feature_settings_appearance_title_greetings
import bricklog.composeapp.generated.resources.feature_settings_appearance_title_theme
import hu.piware.bricklog.feature.core.presentation.components.ContentColumn
import hu.piware.bricklog.feature.core.presentation.components.LoadingOverlay
import hu.piware.bricklog.feature.currency.domain.model.toUiText
import hu.piware.bricklog.feature.currency.domain.util.CURRENCY_OPTIONS
import hu.piware.bricklog.feature.set.presentation.dashboard.utils.FeaturedSetType
import hu.piware.bricklog.feature.set.presentation.dashboard.utils.stringResource
import hu.piware.bricklog.feature.settings.domain.model.ThemeOption
import hu.piware.bricklog.feature.settings.presentation.appearance.components.CurrencyCodeBottomSheet
import hu.piware.bricklog.feature.settings.presentation.appearance.components.CurrencyRegionBottomSheet
import hu.piware.bricklog.feature.settings.presentation.appearance.components.DoubleHorizontalDivider
import hu.piware.bricklog.feature.user.domain.model.isAuthenticated
import hu.piware.bricklog.feature.user.presentation.components.NameField
import hu.piware.bricklog.feature.user.presentation.util.isValidName
import hu.piware.bricklog.mock.PreviewData
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
        },
    )
}

@Composable
private fun AppearanceScreen(
    state: AppearanceState,
    onAction: (AppearanceAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    LoadingOverlay(
        isLoading = state.isLoading,
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
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
                ) {
                    DashboardSettings(
                        state = state,
                        onAction = onAction,
                    )

                    DoubleHorizontalDivider()

                    CurrencySettings(
                        state = state,
                        onAction = onAction,
                    )

                    DoubleHorizontalDivider()

                    ThemeSettings(
                        state = state,
                        onAction = onAction,
                    )
                }
            }
        }
    }
}

@Composable
private fun DashboardSettings(
    state: AppearanceState,
    onAction: (AppearanceAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.MediumPadding.size),
        verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
    ) {
        Text(
            text = stringResource(Res.string.feature_settings_appearance_title_dashboard),
            style = MaterialTheme.typography.headlineMedium,
        )

        FeaturedSetsSettings(
            hiddenFeaturedSets = state.userPreferences.hiddenFeaturedSets,
            onHiddenFeaturedSetsChange = { hiddenFeaturedSets ->
                onAction(
                    AppearanceAction.OnUserPreferencesChange(
                        state.userPreferences.copy(hiddenFeaturedSets = hiddenFeaturedSets),
                        false,
                    ),
                )
            },
        )

        if (state.currentUser.isAuthenticated) {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = Dimens.SmallPadding.size),
            )

            GreetingsSettings(
                hideGreetings = state.userPreferences.hideGreetings,
                onHideGreetingsChange = { hideGreetings ->
                    onAction(
                        AppearanceAction.OnUserPreferencesChange(
                            state.userPreferences.copy(hideGreetings = hideGreetings),
                            true,
                        ),
                    )
                },
                displayName = state.userPreferences.displayName,
                onDisplayNameChange = { displayName ->
                    onAction(
                        AppearanceAction.OnUserPreferencesChange(
                            state.userPreferences.copy(displayName = displayName),
                            true,
                        ),
                    )
                },
            )
        }
    }
}

@Composable
private fun FeaturedSetsSettings(
    hiddenFeaturedSets: List<FeaturedSetType>,
    onHiddenFeaturedSetsChange: (List<FeaturedSetType>) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.SmallPadding.size),
        verticalArrangement = Arrangement.spacedBy(Dimens.ExtraSmallPadding.size),
    ) {
        Text(
            text = stringResource(Res.string.feature_settings_appearance_title_featured_sets),
            style = MaterialTheme.typography.titleLarge,
        )

        for (type in FeaturedSetType.entries) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(type.stringResource),
                    style = MaterialTheme.typography.titleMedium,
                )

                Switch(
                    checked = !hiddenFeaturedSets.contains(type),
                    onCheckedChange = {
                        onHiddenFeaturedSetsChange(
                            if (it) {
                                hiddenFeaturedSets - type
                            } else {
                                hiddenFeaturedSets + type
                            },
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun GreetingsSettings(
    hideGreetings: Boolean,
    onHideGreetingsChange: (Boolean) -> Unit,
    displayName: String?,
    onDisplayNameChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.feature_settings_appearance_title_greetings),
                style = MaterialTheme.typography.titleLarge,
            )

            Switch(
                checked = !hideGreetings,
                onCheckedChange = {
                    onHideGreetingsChange(!it)
                },
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val focusManager = LocalFocusManager.current

            var name by remember { mutableStateOf("") }
            var isNameValid by remember { mutableStateOf(true) }
            NameField(
                modifier = Modifier.weight(1f),
                value = name,
                onValueChange = { name = it },
                labelValue = displayName?.ifEmpty { null },
                onValidate = { isNameValid = it },
            )

            FilledIconButton(
                onClick = {
                    focusManager.clearFocus()
                    if (isValidName(name)) {
                        onDisplayNameChange(name)
                    }
                },
                enabled = isNameValid,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Save,
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
private fun CurrencySettings(
    state: AppearanceState,
    onAction: (AppearanceAction) -> Unit,
) {
    var showCurrencyRegionSheet by remember { mutableStateOf(false) }
    var showCurrencyCodeSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.MediumPadding.size),
        verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
    ) {
        Text(
            text = stringResource(Res.string.feature_settings_appearance_title_currency),
            style = MaterialTheme.typography.headlineMedium,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.feature_settings_appearance_label_currency_region),
                style = MaterialTheme.typography.titleMedium,
            )

            FilledTonalButton(
                onClick = { showCurrencyRegionSheet = true },
            ) {
                Text(state.userPreferences.currencyRegion.toUiText().asString())
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.feature_settings_appearance_label_currency_target),
                style = MaterialTheme.typography.titleMedium,
            )

            FilledTonalButton(
                onClick = { showCurrencyCodeSheet = true },
            ) {
                Text(state.userPreferences.targetCurrencyCode)
            }
        }
    }

    if (showCurrencyRegionSheet) {
        CurrencyRegionBottomSheet(
            selectedItem = state.userPreferences.currencyRegion,
            onSelectionChange = {
                onAction(
                    AppearanceAction.OnUserPreferencesChange(
                        preferences = state.userPreferences.copy(currencyRegion = it),
                        showLoading = false,
                    ),
                )
            },
            onDismiss = { showCurrencyRegionSheet = false },
        )
    }

    if (showCurrencyCodeSheet) {
        CurrencyCodeBottomSheet(
            availableOptions = CURRENCY_OPTIONS,
            selectedItem = state.userPreferences.targetCurrencyCode,
            onSelectionChange = {
                onAction(
                    AppearanceAction.OnUserPreferencesChange(
                        preferences = state.userPreferences.copy(targetCurrencyCode = it),
                        showLoading = false,
                    ),
                )
            },
            onDismiss = { showCurrencyCodeSheet = false },
        )
    }
}

@Composable
private fun ThemeSettings(
    state: AppearanceState,
    onAction: (AppearanceAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.MediumPadding.size),
        verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
    ) {
        Text(
            text = stringResource(Res.string.feature_settings_appearance_title_theme),
            style = MaterialTheme.typography.headlineMedium,
        )

        val choices = ThemeOption.entries.toTypedArray()
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth(),
        ) {
            choices.forEachIndexed { index, themeOption ->
                SegmentedButton(
                    modifier = Modifier.testTag("theme_settings:theme_option"),
                    selected = state.themeOption == themeOption,
                    onClick = {
                        onAction(AppearanceAction.OnThemeOptionChange(themeOption))
                    },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = choices.count(),
                    ),
                ) {
                    Text(stringResource(themeOption.titleRes))
                }
            }
        }
    }
}

@Preview
@Composable
private fun AppearanceScreenPreview() {
    BricklogTheme {
        AppearanceScreen(
            state = AppearanceState(
                currentUser = PreviewData.user,
            ),
            onAction = {},
        )
    }
}
