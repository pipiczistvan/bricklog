package hu.piware.bricklog.feature.set.domain.model

import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_onboarding_data_fetch_step_download_file
import bricklog.composeapp.generated.resources.feature_set_onboarding_data_fetch_step_parse_sets
import bricklog.composeapp.generated.resources.feature_set_onboarding_data_fetch_step_prepare_batches
import bricklog.composeapp.generated.resources.feature_set_onboarding_data_fetch_step_save_update_info
import bricklog.composeapp.generated.resources.feature_set_onboarding_data_fetch_step_store_sets
import bricklog.composeapp.generated.resources.feature_set_onboarding_data_fetch_step_uncompress_file
import hu.piware.bricklog.feature.core.domain.FlowForValue
import hu.piware.bricklog.feature.core.domain.FlowProgressCollector
import hu.piware.bricklog.feature.core.domain.collectForValue
import hu.piware.bricklog.feature.core.presentation.UiText

data class UpdateSetsProgress(
    val stepProgress: Float,
    val step: UpdateSetsStep,
    val totalProgress: Float = stepProgress,
)

enum class UpdateSetsStep {
    PREPARE_BATCHES,
    DOWNLOAD_FILE,
    UNCOMPRESS_FILE,
    PARSE_SETS,
    STORE_SETS,
    SAVE_UPDATE_INFO,
}

fun UpdateSetsProgress.mapTotalProgress(range: ClosedFloatingPointRange<Float>): UpdateSetsProgress {
    val clampedProgress = totalProgress.coerceIn(0f, 1f)
    return copy(totalProgress = range.start + (range.endInclusive - range.start) * clampedProgress)
}

suspend fun <T> FlowProgressCollector<UpdateSetsProgress>.awaitInProgressRange(
    progressRange: ClosedFloatingPointRange<Float>,
    flowForValue: () -> FlowForValue<T, UpdateSetsProgress>,
): T {
    return flowForValue().collectForValue { progress ->
        emitProgress(progress.mapTotalProgress(progressRange))
    }
}

fun UpdateSetsStep.toUiText(): UiText {
    val stringRes = when (this) {
        UpdateSetsStep.PREPARE_BATCHES -> Res.string.feature_set_onboarding_data_fetch_step_prepare_batches
        UpdateSetsStep.DOWNLOAD_FILE -> Res.string.feature_set_onboarding_data_fetch_step_download_file
        UpdateSetsStep.UNCOMPRESS_FILE -> Res.string.feature_set_onboarding_data_fetch_step_uncompress_file
        UpdateSetsStep.PARSE_SETS -> Res.string.feature_set_onboarding_data_fetch_step_parse_sets
        UpdateSetsStep.STORE_SETS -> Res.string.feature_set_onboarding_data_fetch_step_store_sets
        UpdateSetsStep.SAVE_UPDATE_INFO -> Res.string.feature_set_onboarding_data_fetch_step_save_update_info
    }

    return UiText.StringResourceId(stringRes)
}
