package hu.piware.bricklog.feature.set.domain.model

import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_preload_fetch_step_download_file
import bricklog.composeapp.generated.resources.feature_set_preload_fetch_step_parse_sets
import bricklog.composeapp.generated.resources.feature_set_preload_fetch_step_prepare_batches
import bricklog.composeapp.generated.resources.feature_set_preload_fetch_step_save_update_info
import bricklog.composeapp.generated.resources.feature_set_preload_fetch_step_store_sets
import bricklog.composeapp.generated.resources.feature_set_preload_fetch_step_uncompress_file
import hu.piware.bricklog.feature.core.domain.UpdateProgress
import hu.piware.bricklog.feature.core.presentation.UiText

typealias UpdateSetsProgress = UpdateProgress<UpdateSetsStep>

enum class UpdateSetsStep {
    PREPARE_BATCHES,
    DOWNLOAD_FILE,
    UNCOMPRESS_FILE,
    PARSE_SETS,
    STORE_SETS,
    SAVE_UPDATE_INFO,
}

fun UpdateSetsStep.toUiText(): UiText {
    val stringRes = when (this) {
        UpdateSetsStep.PREPARE_BATCHES -> Res.string.feature_set_preload_fetch_step_prepare_batches
        UpdateSetsStep.DOWNLOAD_FILE -> Res.string.feature_set_preload_fetch_step_download_file
        UpdateSetsStep.UNCOMPRESS_FILE -> Res.string.feature_set_preload_fetch_step_uncompress_file
        UpdateSetsStep.PARSE_SETS -> Res.string.feature_set_preload_fetch_step_parse_sets
        UpdateSetsStep.STORE_SETS -> Res.string.feature_set_preload_fetch_step_store_sets
        UpdateSetsStep.SAVE_UPDATE_INFO -> Res.string.feature_set_preload_fetch_step_save_update_info
    }

    return UiText.StringResourceId(stringRes)
}
