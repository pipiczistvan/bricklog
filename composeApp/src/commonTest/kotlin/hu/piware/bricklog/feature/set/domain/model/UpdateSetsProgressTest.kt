package hu.piware.bricklog.feature.set.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class UpdateSetsProgressTest {

    @Test
    fun testMapTotalProgress() {
        val progress = UpdateSetsProgress(
            stepProgress = 0.5f,
            step = UpdateSetsStep.PREPARE_BATCHES,
            totalProgress = 0.8f
        )

        val mappedProgress = progress.mapTotalProgress(0f..0.5f)

        assertEquals(mappedProgress.totalProgress, 0.4f)
    }
}
