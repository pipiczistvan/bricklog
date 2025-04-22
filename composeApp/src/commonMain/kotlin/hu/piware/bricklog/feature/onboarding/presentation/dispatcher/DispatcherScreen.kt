package hu.piware.bricklog.feature.onboarding.presentation.dispatcher

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DispatcherScreenRoot(
    onNavigateToDashboard: () -> Unit,
    onNavigateToDataFetch: () -> Unit,
    viewModel: DispatcherViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        val currentState = state

        when (currentState) {
            DispatcherState.Loading -> {}
            DispatcherState.NavigateToDataFetch -> onNavigateToDataFetch()
            DispatcherState.NavigateToHome -> onNavigateToDashboard()
        }
    }
}
