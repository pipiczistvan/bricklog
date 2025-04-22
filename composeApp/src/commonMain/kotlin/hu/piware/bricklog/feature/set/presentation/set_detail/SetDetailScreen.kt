@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.set_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.barcode_ean
import bricklog.composeapp.generated.resources.barcode_upc
import bricklog.composeapp.generated.resources.go_back
import hu.piware.bricklog.feature.core.presentation.components.ContentColumn
import hu.piware.bricklog.feature.core.presentation.sharedElement
import hu.piware.bricklog.feature.set.domain.model.Instruction
import hu.piware.bricklog.feature.set.domain.model.SetUI
import hu.piware.bricklog.feature.set.domain.model.setID
import hu.piware.bricklog.feature.set.presentation.components.SetImage
import hu.piware.bricklog.feature.set.presentation.set_detail.components.BlurredImageBackground
import hu.piware.bricklog.feature.set.presentation.set_detail.components.SetBarcode
import hu.piware.bricklog.feature.set.presentation.set_detail.components.SetDetailsTable
import hu.piware.bricklog.feature.set.presentation.set_detail.components.SetInstructionsTable
import hu.piware.bricklog.feature.set.presentation.set_detail.util.createFirstSetDetailTableColumns
import hu.piware.bricklog.feature.set.presentation.set_detail.util.createSecondSetDetailTableColumns
import hu.piware.bricklog.feature.set.presentation.set_detail.util.createThirdSetDetailTableColumns
import hu.piware.bricklog.feature.set.presentation.set_image.SetImageArguments
import hu.piware.bricklog.ui.theme.Dimens
import hu.piware.bricklog.ui.theme.Shapes
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import qrgenerator.barcodepainter.BarcodeFormat

@Composable
fun SetDetailScreenRoot(
    viewModel: SetDetailViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onImageClick: (SetImageArguments) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    SetDetailScreen(
        modifier = Modifier
            .testTag("set_detail_screen"),
        state = state,
        onAction = { action ->
            when (action) {
                is SetDetailAction.OnBackClick -> onBackClick()
                is SetDetailAction.OnImageClick -> onImageClick(
                    SetImageArguments(action.setId, state.sharedElementPrefix)
                )

                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun SetDetailScreen(
    modifier: Modifier = Modifier,
    state: SetDetailState,
    onAction: (SetDetailAction) -> Unit,
) {
    if (state.setUI == null) {
        return
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = { onAction(SetDetailAction.OnBackClick) },
                        colors = IconButtonDefaults.filledIconButtonColors().copy(
                            containerColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.go_back),
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onAction(SetDetailAction.OnFavouriteClick(state.setUI.setID)) },
                        colors = IconButtonDefaults.filledIconButtonColors().copy(
                            containerColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = if (state.setUI.isFavourite) Icons.Default.Star else Icons.Outlined.StarOutline,
                            contentDescription = stringResource(Res.string.go_back),
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        BlurredImageBackground(
            modifier = Modifier.fillMaxSize(),
            image = state.setUI.set.image
        )

        Content(
            setUI = state.setUI,
            instructions = state.instructions,
            sharedElementPrefix = state.sharedElementPrefix,
            onImageClick = { onAction(SetDetailAction.OnImageClick(state.setUI.setID)) },
            paddingValues = padding
        )
    }
}

@Composable
private fun Content(
    setUI: SetUI,
    instructions: List<Instruction>?,
    sharedElementPrefix: String,
    onImageClick: () -> Unit,
    paddingValues: PaddingValues,
) {
    ContentColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.MediumPadding.size),
        contentPadding = PaddingValues(
            top = paddingValues.calculateTopPadding(),
            bottom = paddingValues.calculateBottomPadding()
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size)
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        SetHeaderImage(
            setUI = setUI,
            sharedElementPrefix = sharedElementPrefix,
            onClick = onImageClick
        )

        Text(
            text = setUI.set.name ?: "",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        SetDetails(
            setUI = setUI,
            instructions = instructions
        )
    }
}

@Composable
private fun SetHeaderImage(
    setUI: SetUI,
    sharedElementPrefix: String,
    onClick: () -> Unit,
) {
    ElevatedCard(
        modifier = Modifier
            .height(250.dp)
            .aspectRatio(1f)
            .sharedElement("${sharedElementPrefix}/image/${setUI.setID}"),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 15.dp
        )
    ) {
        val interactionSource = remember { MutableInteractionSource() }

        SetImage(
            image = setUI.set.image,
            modifier = Modifier
                .testTag("set_detail:image")
                .fillMaxSize()
                .background(Color.White)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                )
        )
    }
}

@Composable
private fun SetDetails(
    setUI: SetUI,
    instructions: List<Instruction>?,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier.clip(Shapes.medium)
        ) {
            SetDetailsTable(createFirstSetDetailTableColumns(setUI), swapColors = false)
            SetDetailsTable(createSecondSetDetailTableColumns(setUI), swapColors = true)
            SetDetailsTable(createThirdSetDetailTableColumns(setUI), swapColors = false)
        }

        SetInstructionsTable(
            modifier = Modifier.clip(Shapes.medium),
            instructions = instructions
        )

        setUI.set.barcodeEAN?.let {
            TitledSetDetail(
                title = stringResource(Res.string.barcode_ean)
            ) {
                SetBarcode(
                    barcode = it,
                    format = BarcodeFormat.EAN13
                )
            }

            setUI.set.barcodeUPC?.let {
                HorizontalDivider()
            }
        }

        setUI.set.barcodeUPC?.let {
            TitledSetDetail(
                title = stringResource(Res.string.barcode_upc)
            ) {
                SetBarcode(
                    barcode = it,
                    format = BarcodeFormat.UPCA
                )
            }
        }
    }
}

@Composable
private fun TitledSetDetail(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )

        content()
    }
}
