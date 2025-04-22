package hu.piware.bricklog.feature.settings.presentation.changelog

import hu.piware.bricklog.feature.settings.domain.model.Changelog

data class ChangelogState(
    val changelog: Changelog? = null,
)
