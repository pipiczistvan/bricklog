@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package hu.piware.bricklog.feature.onboarding.presentation.preload

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_preload_fetch_btn_retry
import hu.piware.bricklog.App
import hu.piware.bricklog.feature.core.domain.usecase.UpdateDataProgress
import hu.piware.bricklog.feature.core.domain.usecase.UpdateDataStep
import hu.piware.bricklog.feature.core.domain.usecase.toUiText
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PreloadScreenRoot(
    viewModel: PreloadViewModel = koinViewModel(),
    onNavigateToDispatcher: () -> Unit,
) {
    App.firstScreenLoaded = true

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    PreloadScreen(
        state = state,
        onAction = { action ->
            when (action) {
                PreloadAction.OnContinueClick -> onNavigateToDispatcher()
                else -> Unit
            }
            viewModel.onAction(action)
        },
    )
}

@Composable
private fun PreloadScreen(
    state: PreloadState,
    onAction: (PreloadAction) -> Unit,
) {
    when (state) {
        is PreloadState.Initial -> Unit

        is PreloadState.Success -> {
            onAction(PreloadAction.OnContinueClick)
        }

        is PreloadState.Loading -> {
            Scaffold {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Dimens.LargePadding.size),
                    ) {
                        LoadingIndicator(state.progress.totalProgress)
                        Text(state.progress.step.toUiText().asString())
                    }
                }
            }
        }

        is PreloadState.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(
                    onClick = { onAction(PreloadAction.OnRetryClick) },
                ) {
                    Text(stringResource(Res.string.feature_set_preload_fetch_btn_retry))
                }
            }
        }
    }
}

@Composable
private fun LoadingIndicator(progress: Float) {
    Box(
        contentAlignment = Alignment.Center,
    ) {
        val animatedProgress by animateFloatAsState(
            targetValue = progress,
            animationSpec = tween(durationMillis = 500),
            label = "Set update progress",
        )
        ContainedLoadingIndicator(
            modifier = Modifier
                .size(100.dp),
        )
        CircularProgressIndicator(
            modifier = Modifier
                .size(120.dp),
            strokeWidth = 6.dp,
            progress = { animatedProgress },
        )
    }
}

@Preview
@Composable
private fun PreloadScreenPreview() {
    MaterialTheme {
        PreloadScreen(
            state = PreloadState.Loading(
                UpdateDataProgress(
                    stepProgress = 0.5f,
                    step = UpdateDataStep.PREPARE_EXPORT_INFO,
                    totalProgress = 0.5f,
                ),
            ),
            onAction = {},
        )
    }
}
