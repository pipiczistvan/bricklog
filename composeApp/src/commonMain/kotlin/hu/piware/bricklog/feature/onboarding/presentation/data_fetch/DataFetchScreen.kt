@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package hu.piware.bricklog.feature.onboarding.presentation.data_fetch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_onboarding_data_fetch_btn_retry
import hu.piware.bricklog.App
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DataFetchScreenRoot(
    viewModel: DataFetchViewModel = koinViewModel(),
    onNavigateToDashboard: () -> Unit,
) {
    App.firstScreenLoaded = true

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    DataFetchScreen(
        state = state,
        onAction = { action ->
            when (action) {
                DataFetchAction.OnContinueClick -> onNavigateToDashboard()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun DataFetchScreen(
    state: DataFetchState,
    onAction: (DataFetchAction) -> Unit,
) {
    when (state) {
        is DataFetchState.Success -> {
            onAction(DataFetchAction.OnContinueClick)
        }

        is DataFetchState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ContainedLoadingIndicator()
            }
        }

        is DataFetchState.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { onAction(DataFetchAction.OnRetryClick) }
                ) {
                    Text(stringResource(Res.string.feature_set_onboarding_data_fetch_btn_retry))
                }
            }
        }
    }
}

@Preview
@Composable
private fun DataFetchScreenPreview() {
    MaterialTheme {
        DataFetchScreen(
            state = DataFetchState.Loading,
            onAction = {}
        )
    }
}
