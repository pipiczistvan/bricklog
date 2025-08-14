@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package hu.piware.bricklog.feature.set.presentation.set_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_detail_instructions_empty
import bricklog.composeapp.generated.resources.feature_set_detail_instructions_info_dialog_label
import bricklog.composeapp.generated.resources.feature_set_detail_instructions_title
import hu.piware.bricklog.feature.core.presentation.components.ActionNavigationType
import hu.piware.bricklog.feature.core.presentation.components.ActionRow
import hu.piware.bricklog.feature.core.presentation.components.AlertIconButton
import hu.piware.bricklog.feature.set.domain.model.Instruction
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SetInstructionsTable(
    modifier: Modifier = Modifier,
    instructions: List<Instruction>?,
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f),
                text = stringResource(Res.string.feature_set_detail_instructions_title),
                fontWeight = FontWeight.Bold,
            )
            AlertIconButton {
                Text(stringResource(Res.string.feature_set_detail_instructions_info_dialog_label))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)),
        ) {
            if (instructions == null) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    ContainedLoadingIndicator(
                        modifier = Modifier
                            .padding(12.dp),
                    )
                }
            } else {
                val filteredInstructions = instructions
                    .filter { it.description != null && it.URL != null }

                if (filteredInstructions.isNotEmpty()) {
                    val uriHandler = LocalUriHandler.current

                    filteredInstructions.forEach {
                        ActionRow(
                            title = it.description!!,
                            onClick = {
                                uriHandler.openUri(it.URL!!)
                            },
                            navigationType = ActionNavigationType.LINK,
                        )
                        if (it != filteredInstructions.last()) {
                            HorizontalDivider()
                        }
                    }
                } else {
                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = stringResource(Res.string.feature_set_detail_instructions_empty),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SetInstructionsTablePreview() {
    MaterialTheme {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
        ) {
            SetInstructionsTable(
                instructions = null,
            )
        }
    }
}
