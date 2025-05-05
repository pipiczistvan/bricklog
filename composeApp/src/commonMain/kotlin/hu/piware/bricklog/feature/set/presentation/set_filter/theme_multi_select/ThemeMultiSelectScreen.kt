@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.set_filter.theme_multi_select

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.theme_multi_select_clear_selection
import bricklog.composeapp.generated.resources.theme_multi_select_search_hint
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ThemeMultiSelectScreenRoot(
    viewModel: ThemeMultiSelectViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onApplyClick: (ThemeMultiSelectArguments) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ThemeMultiSelectScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is ThemeMultiSelectAction.OnBackClick -> onBackClick()
                is ThemeMultiSelectAction.OnApplyClick -> onApplyClick(action.arguments)
                else -> Unit
            }
            viewModel.onAction(action)
        },
        modifier = Modifier.testTag("theme_multi_select_screen"),
    )
}

@Composable
private fun ThemeMultiSelectScreen(
    state: ThemeMultiSelectState,
    onAction: (ThemeMultiSelectAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Spacer(modifier = Modifier.statusBarsPadding())
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = state.searchQuery,
                    onValueChange = { onAction(ThemeMultiSelectAction.OnQueryChange(it)) },
                    placeholder = {
                        Text(stringResource(Res.string.theme_multi_select_search_hint))
                    },
                    leadingIcon = {
                        IconButton(onClick = { onAction(ThemeMultiSelectAction.OnBackClick) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    trailingIcon = {
                        Row {
                            IconButton(onClick = {
                                onAction(ThemeMultiSelectAction.OnQueryChange(""))
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null
                                )
                            }

                            TextButton(
                                onClick = {
                                    onAction(
                                        ThemeMultiSelectAction.OnSelectionChange(emptySet())
                                    )
                                }
                            ) {
                                Text(stringResource(Res.string.theme_multi_select_clear_selection))
                            }
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                    )
                )
            }

        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {
                    Text("Apply")
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null
                    )
                },
                onClick = {
                    onAction(ThemeMultiSelectAction.OnApplyClick(ThemeMultiSelectArguments(state.selectedThemes)))
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .testTag("theme_multi_select:list")
                .fillMaxSize(),
            contentPadding = padding
        ) {
            items(state.availableThemes) { theme ->
                ListItem(
                    headlineContent = {
                        Text(theme)
                    },
                    leadingContent = {
                        Checkbox(
                            checked = state.selectedThemes.contains(theme),
                            onCheckedChange = { checked ->
                                val themes = if (checked) {
                                    state.selectedThemes + theme
                                } else {
                                    state.selectedThemes - theme
                                }

                                onAction(
                                    ThemeMultiSelectAction.OnSelectionChange(themes)
                                )
                            }
                        )
                    }
                )
            }
        }
    }
}
