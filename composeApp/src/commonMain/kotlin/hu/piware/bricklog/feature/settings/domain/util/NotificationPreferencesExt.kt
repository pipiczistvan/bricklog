package hu.piware.bricklog.feature.settings.domain.util

import hu.piware.bricklog.feature.settings.domain.model.NotificationPreferences

fun NotificationPreferences.newSetsEnabled(): Boolean {
    return general && newSets
}
