package hu.piware.bricklog.feature.settings.presentation.license

sealed interface LicenseAction {
    data object OnBackClick : LicenseAction
}
