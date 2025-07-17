package hu.piware.bricklog.feature.set.presentation.dashboard

sealed interface DashboardEvent {
    data object LoginProposed : DashboardEvent
}
