package hu.piware.bricklog.feature.core.domain

data class UpdateProgress<S>(
    val stepProgress: Float,
    val step: S,
    val totalProgress: Float = stepProgress,
)

fun <S> UpdateProgress<S>.mapTotalProgress(range: ClosedFloatingPointRange<Float>): UpdateProgress<S> {
    val clampedProgress = totalProgress.coerceIn(0f, 1f)
    return copy(totalProgress = range.start + (range.endInclusive - range.start) * clampedProgress)
}

suspend fun <S, T> FlowProgressCollector<UpdateProgress<S>>.awaitInProgressRange(
    progressRange: ClosedFloatingPointRange<Float>,
    flowForValue: () -> FlowForValue<T, UpdateProgress<S>>,
): T {
    return flowForValue().collectForValue { progress ->
        emitProgress(progress.mapTotalProgress(progressRange))
    }
}
