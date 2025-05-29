@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)

package hu.piware.bricklog.feature.set.presentation.set_image

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.go_back
import hu.piware.bricklog.feature.set.domain.model.Image
import hu.piware.bricklog.feature.set.presentation.components.SetImage
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SetImageScreenRoot(
    viewModel: SetImageViewModel = koinViewModel(),
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SetImageScreen(
        modifier = Modifier
            .testTag("set_image_screen"),
        state = uiState,
        onAction = { action ->
            when (action) {
                SetImageAction.OnBackClick -> onBackClick()
            }
        }
    )
}

@Composable
fun SetImageScreen(
    modifier: Modifier = Modifier,
    state: SetImageState,
    onAction: (SetImageAction) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = { onAction(SetImageAction.OnBackClick) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(Res.string.go_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = Color.Transparent
                )
            )
        }
    ) {
        SetImagePager(
            images = state.images
        )
    }
}

@Composable
private fun SetImagePager(
    images: List<Image>,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState { images.size }

    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        beyondViewportPageCount = 1
    ) {
        SetImage(
            modifier = Modifier
                .fillMaxSize(),
            image = images[it],
            zoomable = true
        )
    }
}
