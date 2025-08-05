package hu.piware.bricklog.feature.onboarding.domain.usecase

import hu.piware.bricklog.feature.core.domain.awaitInProgressRange
import hu.piware.bricklog.feature.core.domain.flowForResult
import hu.piware.bricklog.feature.core.domain.mapProgress
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.core.presentation.UiTextUpdateProgress
import hu.piware.bricklog.feature.currency.domain.model.toUiText
import hu.piware.bricklog.feature.currency.domain.usecase.UpdateEurRatesWithProgress
import hu.piware.bricklog.feature.set.domain.model.toUiText
import hu.piware.bricklog.feature.set.domain.usecase.UpdateSetsWithProgress
import org.koin.core.annotation.Single

@Single
class UpdateDataWithProgress(
    private val updateSetsWithProgress: UpdateSetsWithProgress,
    private val updateEurRatesWithProgress: UpdateEurRatesWithProgress,
) {
    operator fun invoke(force: Boolean = false) = flowForResult {
        awaitInProgressRange(0f..0.9f) {
            updateSetsWithProgress(force).mapProgress { progress ->
                UiTextUpdateProgress(
                    stepProgress = progress.stepProgress,
                    step = progress.step.toUiText(),
                    totalProgress = progress.totalProgress,
                )
            }
        }.onError { return@flowForResult it }

        awaitInProgressRange(0.9f..1f) {
            updateEurRatesWithProgress(force).mapProgress { progress ->
                UiTextUpdateProgress(
                    stepProgress = progress.stepProgress,
                    step = progress.step.toUiText(),
                    totalProgress = progress.totalProgress,
                )
            }
        }.onError { return@flowForResult it }
    }
}
