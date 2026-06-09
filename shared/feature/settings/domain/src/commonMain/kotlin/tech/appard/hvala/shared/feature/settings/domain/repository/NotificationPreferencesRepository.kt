package tech.appard.hvala.shared.feature.settings.domain.repository

import kotlinx.coroutines.flow.StateFlow
import tech.appard.hvala.shared.feature.settings.domain.model.NotificationPreferences

interface NotificationPreferencesRepository {
    val preferencesFlow: StateFlow<NotificationPreferences>

    fun getPreferences(): NotificationPreferences

    suspend fun setPreferences(preferences: NotificationPreferences)
}
