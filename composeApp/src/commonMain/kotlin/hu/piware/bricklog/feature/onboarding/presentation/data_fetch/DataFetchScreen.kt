
package hu.piware.bricklog.feature.onboarding.presentation.data_fetch

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import bricklog.composeapp.generated.resources.feature_set_onboarding_data_fetch_btn_retry
import hu.piware.bricklog.App
import hu.piware.bricklog.feature.core.presentation.UiTextUpdateProgress
import hu.piware.bricklog.feature.set.domain.model.UpdateSetsStep
import hu.piware.bricklog.feature.set.domain.model.toUiText
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DataFetchScreenRoot(
    viewModel: DataFetchViewModel = koinViewModel(),
    onNavigateToDispatcher: () -> Unit,
) {
    App.firstScreenLoaded = true

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    DataFetchScreen(
        state = state,
        onAction = { action ->
            when (action) {
                DataFetchAction.OnContinueClick -> onNavigateToDispatcher()
                else -> Unit
            }
            viewModel.onAction(action)
        },
    )
}

@Composable
private fun DataFetchScreen(
    state: DataFetchState,
    onAction: (DataFetchAction) -> Unit,
) {
    when (state) {
        is DataFetchState.Initial -> Unit

        is DataFetchState.Success -> {
            onAction(DataFetchAction.OnContinueClick)
        }

        is DataFetchState.Loading -> {
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
                        Text(state.progress.step.asString())
                    }
                }
            }
        }

        is DataFetchState.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(
                    onClick = { onAction(DataFetchAction.OnRetryClick) },
                ) {
                    Text(stringResource(Res.string.feature_set_onboarding_data_fetch_btn_retry))
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
        CircularProgressIndicator(
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
private fun DataFetchScreenPreview() {
    MaterialTheme {
        DataFetchScreen(
            state = DataFetchState.Loading(
                UiTextUpdateProgress(
                    stepProgress = 0.5f,
                    step = UpdateSetsStep.PREPARE_BATCHES.toUiText(),
                    totalProgress = 0.5f,
                ),
            ),
            onAction = {},
        )
    }
}
