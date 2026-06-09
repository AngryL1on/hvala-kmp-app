package tech.appard.hvala.shared.feature.settings.domain

import tech.appard.hvala.shared.feature.settings.domain.model.NotificationPreferences
import tech.appard.hvala.shared.feature.settings.domain.repository.NotificationPreferencesRepository

class GetNotificationPreferencesUseCase(
    private val repository: NotificationPreferencesRepository,
) {
    operator fun invoke(): NotificationPreferences = repository.getPreferences()
}

class SetNotificationPreferencesUseCase(
    private val repository: NotificationPreferencesRepository,
) {
    suspend operator fun invoke(preferences: NotificationPreferences) {
        repository.setPreferences(preferences)
    }
}
