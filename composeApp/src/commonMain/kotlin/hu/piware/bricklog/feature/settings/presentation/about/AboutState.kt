package hu.piware.bricklog.feature.settings.presentation.about

import hu.piware.bricklog.feature.settings.domain.model.DataSyncInfo

data class AboutState(
    val setDataSyncInfo: DataSyncInfo? = null,
    val eurRateDataSyncInfo: DataSyncInfo? = null,
)
