package tech.appard.hvala.shared.feature.settings.domain.model

data class NotificationPreferences(
    val notificationsEnabled: Boolean = false,
    val soundEnabled: Boolean = true,
)
