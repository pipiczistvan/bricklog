package hu.piware.bricklog.feature.currency.domain.model

import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_preload_fetch_step_download_file
import bricklog.composeapp.generated.resources.feature_set_preload_fetch_step_parse_rates
import bricklog.composeapp.generated.resources.feature_set_preload_fetch_step_prepare_export_info
import bricklog.composeapp.generated.resources.feature_set_preload_fetch_step_save_update_info
import bricklog.composeapp.generated.resources.feature_set_preload_fetch_step_store_rates
import bricklog.composeapp.generated.resources.feature_set_preload_fetch_step_uncompress_file
import hu.piware.bricklog.feature.core.domain.UpdateProgress
import hu.piware.bricklog.feature.core.presentation.UiText

typealias UpdateRatesProgress = UpdateProgress<UpdateRatesStep>

enum class UpdateRatesStep {
    PREPARE_EXPORT_INFO,
    DOWNLOAD_FILE,
    UNCOMPRESS_FILE,
    PARSE_RATES,
    STORE_RATES,
    SAVE_UPDATE_INFO,
}

fun UpdateRatesStep.toUiText(): UiText {
    val stringRes = when (this) {
        UpdateRatesStep.PREPARE_EXPORT_INFO -> Res.string.feature_set_preload_fetch_step_prepare_export_info
        UpdateRatesStep.DOWNLOAD_FILE -> Res.string.feature_set_preload_fetch_step_download_file
        UpdateRatesStep.UNCOMPRESS_FILE -> Res.string.feature_set_preload_fetch_step_uncompress_file
        UpdateRatesStep.PARSE_RATES -> Res.string.feature_set_preload_fetch_step_parse_rates
        UpdateRatesStep.STORE_RATES -> Res.string.feature_set_preload_fetch_step_store_rates
        UpdateRatesStep.SAVE_UPDATE_INFO -> Res.string.feature_set_preload_fetch_step_save_update_info
    }

    return UiText.StringResourceId(stringRes)
}
