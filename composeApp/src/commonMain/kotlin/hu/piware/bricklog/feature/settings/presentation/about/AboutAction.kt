package hu.piware.bricklog.feature.settings.presentation.about

sealed interface AboutAction {
    data object OnBackClick : AboutAction
    data object OnChangelogClick : AboutAction
    data object OnLicenseClick : AboutAction
}
