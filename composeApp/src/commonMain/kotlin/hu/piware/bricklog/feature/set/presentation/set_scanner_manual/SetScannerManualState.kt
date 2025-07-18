package hu.piware.bricklog.feature.set.presentation.set_scanner_manual

import hu.piware.bricklog.feature.set.domain.model.SetDetails

data class SetScannerManualState(
    val collectibleSetDetails: List<SetDetails> = emptyList(),
)
