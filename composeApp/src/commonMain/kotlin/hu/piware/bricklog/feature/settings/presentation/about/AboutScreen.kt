@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.settings.presentation.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.app_logo
import bricklog.composeapp.generated.resources.app_name
import bricklog.composeapp.generated.resources.feature_settings_about_btn_changelog
import bricklog.composeapp.generated.resources.feature_settings_about_btn_license
import bricklog.composeapp.generated.resources.feature_settings_about_btn_source_code
import bricklog.composeapp.generated.resources.feature_settings_about_label_brickset
import bricklog.composeapp.generated.resources.feature_settings_about_label_build_version
import bricklog.composeapp.generated.resources.feature_settings_about_label_developer
import bricklog.composeapp.generated.resources.feature_settings_about_label_kmp
import bricklog.composeapp.generated.resources.feature_settings_about_label_made_in
import bricklog.composeapp.generated.resources.feature_settings_about_link_brickset
import bricklog.composeapp.generated.resources.feature_settings_about_link_kmp
import bricklog.composeapp.generated.resources.feature_settings_about_link_source_code
import bricklog.composeapp.generated.resources.feature_settings_about_title
import bricklog.composeapp.generated.resources.feature_settings_about_title_developed_by
import bricklog.composeapp.generated.resources.feature_settings_about_title_framework
import bricklog.composeapp.generated.resources.feature_settings_about_title_lego_api
import bricklog.composeapp.generated.resources.feature_settings_about_title_tools
import bricklog.composeapp.generated.resources.github_logo
import hu.piware.bricklog.BuildKonfig
import hu.piware.bricklog.feature.core.presentation.components.ActionNavigationType
import hu.piware.bricklog.feature.core.presentation.components.ActionRow
import hu.piware.bricklog.feature.core.presentation.components.ContentColumn
import hu.piware.bricklog.feature.settings.presentation.about.components.InfoCard
import hu.piware.bricklog.ui.theme.BricklogTheme
import hu.piware.bricklog.ui.theme.Dimens
import hu.piware.bricklog.ui.theme.OverpassMonoTypography
import hu.piware.bricklog.ui.theme.Shapes
import hu.piware.bricklog.util.BuildConfig
import hu.piware.bricklog.util.RELEASE_VERSION
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AboutScreenRoot(
    onBackClick: () -> Unit,
    onChangelogClick: () -> Unit,
    onLicenseClick: () -> Unit,
) {
    AboutScreen(
        modifier = Modifier.testTag("about_screen"),
        onAction = { action ->
            when (action) {
                AboutAction.OnBackClick -> onBackClick()
                AboutAction.OnChangelogClick -> onChangelogClick()
                AboutAction.OnLicenseClick -> onLicenseClick()
            }
        }
    )
}

@Composable
private fun AboutScreen(
    onAction: (AboutAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(Res.string.feature_settings_about_title)) },
                navigationIcon = {
                    IconButton(onClick = { onAction(AboutAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->
        ContentColumn(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding()
            )
        ) {
            AppProfile(
                modifier = Modifier
                    .padding(24.dp)
            )
            ActionsTable(
                onAction = onAction,
                modifier = Modifier.padding(12.dp)
            )
            HorizontalDivider()
            DevelopmentTeamSection(
                modifier = Modifier.padding(12.dp)
            )
            HorizontalDivider()
            ToolsSection(
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Composable
private fun ToolsSection(
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(Res.string.feature_settings_about_title_tools),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InfoCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(Res.string.feature_settings_about_title_lego_api),
                    description = stringResource(Res.string.feature_settings_about_label_brickset),
                    onClick = {
                        scope.launch {
                            uriHandler.openUri(getString(Res.string.feature_settings_about_link_brickset))
                        }
                    }
                )
                InfoCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(Res.string.feature_settings_about_title_framework),
                    description = stringResource(Res.string.feature_settings_about_label_kmp),
                    onClick = {
                        scope.launch {
                            uriHandler.openUri(getString(Res.string.feature_settings_about_link_kmp))
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun DevelopmentTeamSection(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        InfoCard(
            title = stringResource(Res.string.feature_settings_about_title_developed_by),
            description = stringResource(Res.string.feature_settings_about_label_developer),
            onClick = {}
        )
        Text(
            text = stringResource(Res.string.feature_settings_about_label_made_in),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun ActionsTable(
    onAction: (AboutAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .clip(Shapes.large)
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
    ) {
        ActionRow(
            title = stringResource(Res.string.feature_settings_about_btn_source_code),
            onClick = {
                scope.launch {
                    uriHandler.openUri(getString(Res.string.feature_settings_about_link_source_code))
                }
            },
            startIcon = {
                Icon(
                    painter = painterResource(Res.drawable.github_logo),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            navigationType = ActionNavigationType.LINK
        )
        HorizontalDivider()
        ActionRow(
            modifier = Modifier.testTag("about:changelog_btn"),
            title = stringResource(Res.string.feature_settings_about_btn_changelog),
            onClick = {
                onAction(AboutAction.OnChangelogClick)
            },
            startIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Notes,
                    contentDescription = null
                )
            },
            navigationType = ActionNavigationType.IN_APP
        )
        HorizontalDivider()
        ActionRow(
            modifier = Modifier.testTag("about:license_btn"),
            title = stringResource(Res.string.feature_settings_about_btn_license),
            onClick = {
                onAction(AboutAction.OnLicenseClick)
            },
            startIcon = {
                Icon(
                    imageVector = Icons.Default.Balance,
                    contentDescription = null,
                )
            },
            navigationType = ActionNavigationType.IN_APP
        )
    }
}

@Composable
private fun AppProfile(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.width(100.dp),
            painter = painterResource(Res.drawable.app_logo),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(Dimens.MediumPadding.size))
        Column {
            Text(
                text = stringResource(Res.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(
                    Res.string.feature_settings_about_label_build_version,
                    BuildConfig.VERSION_NAME,
                    BuildConfig.RELEASE_VERSION,
                    BuildKonfig.REVISION
                ),
                style = OverpassMonoTypography().bodySmall
            )
        }
    }
}

@Preview
@Composable
private fun AboutScreenPreview() {
    BricklogTheme {
        AboutScreen(
            onAction = {}
        )
    }
}
