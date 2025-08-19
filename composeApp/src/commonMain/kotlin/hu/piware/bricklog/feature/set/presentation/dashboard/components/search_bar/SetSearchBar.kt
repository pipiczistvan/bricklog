@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_search_btn_show_all
import bricklog.composeapp.generated.resources.feature_set_search_title_results
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.model.setID
import hu.piware.bricklog.feature.set.presentation.components.SetFilterRow
import hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.components.SearchBarInputField
import hu.piware.bricklog.feature.set.presentation.set_detail.SetDetailArguments
import hu.piware.bricklog.feature.set.presentation.set_list.SetListArguments
import hu.piware.bricklog.ui.theme.Dimens
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SetSearchBar(
    state: SetSearchBarState,
    onAction: (SetSearchBarAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    SearchBar(
        modifier = modifier,
        inputField = {
            SearchBarInputField(
                modifier = Modifier
                    .testTag("search_bar:input_field"),
                expanded = expanded,
                onExpandedChange = { expanded = it },
                onDrawerClick = { onAction(SetSearchBarAction.OnDrawerClick) },
                onScanClick = { onAction(SetSearchBarAction.OnScanClick) },
                onSearch = {
                    expanded = false
                },
                query = state.typedQuery,
                onQueryChange = { onAction(SetSearchBarAction.OnQueryChange(it)) },
                onClearClick = { onAction(SetSearchBarAction.OnClearClick) },
            )
        },
        expanded = expanded,
        onExpandedChange = { expanded = it },
        colors = SearchBarDefaults.colors(
            dividerColor = Color.Transparent,
        ),
    ) {
        Content(
            state = state,
            onAction = { action ->
                onAction(action)
            },
        )
    }
}

@Composable
private fun Content(
    state: SetSearchBarState,
    onAction: (SetSearchBarAction) -> Unit,
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.testTag("search_bar:content"),
    ) {
        SetFilterRow(
            filterPreferences = state.filterPreferences,
            onFilterPreferencesChange = { onAction(SetSearchBarAction.OnFilterChange(it)) },
            filterDomain = state.filterDomain,
        )

        if (state.searchResults.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(Res.string.feature_set_search_title_results),
                    style = MaterialTheme.typography.titleLarge,
                )
                TextButton(
                    modifier = Modifier
                        .testTag("search_bar:show_all_button"),
                    onClick = {
                        scope.launch {
                            onAction(
                                SetSearchBarAction.OnShowAllClick(
                                    SetListArguments.Filtered(
                                        title = getString(Res.string.feature_set_search_title_results),
                                        filterOverrides = SetFilter(
                                            query = state.typedQuery,
                                        ),
                                    ),
                                ),
                            )
                        }
                    },
                ) {
                    Text(stringResource(Res.string.feature_set_search_btn_show_all))
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                items(state.searchResults) { setDetails ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onAction(
                                    SetSearchBarAction.OnSetClick(
                                        SetDetailArguments(
                                            setId = setDetails.setID,
                                            sharedElementPrefix = "search_bar",
                                        ),
                                    ),
                                )
                            },
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(
                                    horizontal = Dimens.MediumPadding.size,
                                    vertical = Dimens.SmallPadding.size,
                                ),
                            text = setDetails.set.name ?: "",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.navigationBarsPadding())
                }
            }
        }
    }
}

@Preview
@Composable
private fun SetSearchBarPreview() {
    MaterialTheme {
        SetSearchBar(
            state = SetSearchBarState(),
            onAction = {},
        )
    }
}
