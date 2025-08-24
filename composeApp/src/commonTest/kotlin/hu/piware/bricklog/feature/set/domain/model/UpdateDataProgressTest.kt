package hu.piware.bricklog.feature.set.domain.model

import hu.piware.bricklog.feature.core.domain.mapTotalProgress
import hu.piware.bricklog.feature.core.domain.usecase.UpdateDataProgress
import hu.piware.bricklog.feature.core.domain.usecase.UpdateDataStep
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdateDataProgressTest {

    @Test
    fun testMapTotalProgress() {
        val progress = UpdateDataProgress(
            stepProgress = 0.5f,
            step = UpdateDataStep.PREPARE_EXPORT_INFO,
            totalProgress = 0.8f
        )

        val mappedProgress = progress.mapTotalProgress(0f..0.5f)

        assertEquals(mappedProgress.totalProgress, 0.4f)
    }
}
