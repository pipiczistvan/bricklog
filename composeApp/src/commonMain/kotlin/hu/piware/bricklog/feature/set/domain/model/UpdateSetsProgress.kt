package hu.piware.bricklog.feature.set.domain.model

import hu.piware.bricklog.feature.core.domain.FlowForValue
import hu.piware.bricklog.feature.core.domain.FlowProgressCollector
import hu.piware.bricklog.feature.core.domain.collectForValue

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
