@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.theme_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.model.SetTheme
import hu.piware.bricklog.feature.set.domain.model.SetThemeGroup
import hu.piware.bricklog.feature.set.presentation.set_list.SetListArguments
import hu.piware.bricklog.ui.theme.BricklogTheme
import hu.piware.bricklog.ui.theme.Dimens
import hu.piware.bricklog.ui.theme.Shapes
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ThemeListScreenRoot(
    viewModel: ThemeListViewModel = koinViewModel(),
    onSearchSets: (SetListArguments) -> Unit,
    onBackClick: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ThemeListScreen(
        modifier = Modifier.testTag("theme_list_screen"),
        state = state,
        onAction = { action ->
            when (action) {
                is ThemeListAction.OnBackClick -> onBackClick()
                is ThemeListAction.OnSearchSets -> onSearchSets(action.arguments)
            }
            viewModel.onAction(action)
        },
    )
}

@Composable
private fun ThemeListScreen(
    state: ThemeListState,
    onAction: (ThemeListAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text("Theme List")
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(ThemeListAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = Dimens.MediumPadding.size)
                .fillMaxSize(),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding(),
            ),
        ) {
            items(state.themeGroups) { themeGroup ->
                ThemeGroupToggleableRow(
                    themeGroup = themeGroup,
                ) {
                    themeGroup.themes.forEach { theme ->
                        ThemeRow(
                            theme = theme,
                            onClick = {
                                onAction(
                                    ThemeListAction.OnSearchSets(
                                        arguments = SetListArguments(
                                            filterOverrides = SetFilter(
                                                themes = setOf(theme.name),
                                            ),
                                            title = theme.name,
                                        ),
                                    ),
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ThemeGroupToggleableRow(
    themeGroup: SetThemeGroup,
    content: @Composable ColumnScope.() -> Unit,
) {
    var showContent by rememberSaveable { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(Shapes.large)
                .clickable { showContent = !showContent }
                .padding(Dimens.SmallPadding.size),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = themeGroup.name,
                style = MaterialTheme.typography.titleMedium,
            )

            val iconAngleProgress: Float by animateFloatAsState(
                targetValue = if (showContent) 1f else 0f,
                animationSpec = tween(
                    durationMillis = 200,
                    easing = FastOutSlowInEasing,
                ),
                label = "iconAngleProgress",
            )

            Icon(
                modifier = Modifier.rotate(iconAngleProgress * 180f),
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
            )
        }

        AnimatedVisibility(
            visible = showContent,
            enter = fadeIn() + expandIn(
                expandFrom = Alignment.BottomCenter,
                initialSize = { IntSize(it.width, 0) },
            ),
            exit = fadeOut() + shrinkOut(
                shrinkTowards = Alignment.BottomCenter,
                targetSize = { IntSize(it.width, 0) },
            ),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = Dimens.MediumPadding.size),
            ) {
                content()
            }
        }
    }
}

@Composable
private fun ThemeRow(
    theme: SetTheme,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.large)
            .clickable(onClick = onClick)
            .padding(Dimens.SmallPadding.size),
    ) {
        Text(
            text = "${theme.name} (${theme.setCount})",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview
@Composable
private fun ThemeListScreenPreview() {
    BricklogTheme {
        ThemeListScreen(
            state = ThemeListState(),
            onAction = {},
        )
    }
}
