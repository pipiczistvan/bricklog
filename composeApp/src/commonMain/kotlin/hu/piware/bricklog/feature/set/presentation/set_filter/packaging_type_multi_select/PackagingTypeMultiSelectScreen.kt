@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.set_filter.packaging_type_multi_select

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
import bricklog.composeapp.generated.resources.packaging_type_multi_select_clear_selection
import bricklog.composeapp.generated.resources.packaging_type_multi_select_search_hint
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PackagingTypeMultiSelectScreenRoot(
    viewModel: PackagingTypeMultiSelectViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onApplyClick: (PackagingTypeMultiSelectArguments) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    PackagingTypeMultiSelectScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is PackagingTypeMultiSelectAction.OnBackClick -> onBackClick()
                is PackagingTypeMultiSelectAction.OnApplyClick -> onApplyClick(action.arguments)
                else -> Unit
            }
            viewModel.onAction(action)
        },
        modifier = Modifier.testTag("packaging_type_multi_select_screen"),
    )
}

@Composable
private fun PackagingTypeMultiSelectScreen(
    state: PackagingTypeMultiSelectState,
    onAction: (PackagingTypeMultiSelectAction) -> Unit,
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
                    onValueChange = { onAction(PackagingTypeMultiSelectAction.OnQueryChange(it)) },
                    placeholder = {
                        Text(stringResource(Res.string.packaging_type_multi_select_search_hint))
                    },
                    leadingIcon = {
                        IconButton(onClick = { onAction(PackagingTypeMultiSelectAction.OnBackClick) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    trailingIcon = {
                        Row {
                            IconButton(onClick = {
                                onAction(PackagingTypeMultiSelectAction.OnQueryChange(""))
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null
                                )
                            }

                            TextButton(
                                onClick = {
                                    onAction(
                                        PackagingTypeMultiSelectAction.OnSelectionChange(emptySet())
                                    )
                                }
                            ) {
                                Text(stringResource(Res.string.packaging_type_multi_select_clear_selection))
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
                    onAction(
                        PackagingTypeMultiSelectAction.OnApplyClick(
                            PackagingTypeMultiSelectArguments(state.selectedPackagingTypes)
                        )
                    )
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .testTag("packaging_type_multi_select:list")
                .fillMaxSize(),
            contentPadding = padding
        ) {
            items(state.availablePackagingTypes) { packagingType ->
                ListItem(
                    headlineContent = {
                        Text(packagingType)
                    },
                    leadingContent = {
                        Checkbox(
                            checked = state.selectedPackagingTypes.contains(packagingType),
                            onCheckedChange = { checked ->
                                val packagingTypes = if (checked) {
                                    state.selectedPackagingTypes + packagingType
                                } else {
                                    state.selectedPackagingTypes - packagingType
                                }

                                onAction(
                                    PackagingTypeMultiSelectAction.OnSelectionChange(packagingTypes)
                                )
                            }
                        )
                    }
                )
            }
        }
    }
}
