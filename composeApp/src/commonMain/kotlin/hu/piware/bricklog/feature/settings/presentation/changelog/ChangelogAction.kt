package hu.piware.bricklog.feature.settings.presentation.changelog

sealed interface ChangelogAction {
    data object OnBackClick : ChangelogAction
}
