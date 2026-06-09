package tech.appard.hvala.shared.feature.settings.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import tech.appard.hvala.shared.core.datastore.PreferencesStore
import tech.appard.hvala.shared.feature.settings.domain.model.NotificationPreferences
import tech.appard.hvala.shared.feature.settings.domain.repository.NotificationPreferencesRepository

internal class NotificationPreferencesRepositoryImpl(
    private val preferences: PreferencesStore,
) : NotificationPreferencesRepository {

    private val _preferences = MutableStateFlow(loadPreferences())
    override val preferencesFlow: StateFlow<NotificationPreferences> = _preferences.asStateFlow()

    override fun getPreferences(): NotificationPreferences = _preferences.value

    override suspend fun setPreferences(preferences: NotificationPreferences) {
        this.preferences.putBoolean(NOTIFICATIONS_ENABLED_KEY, preferences.notificationsEnabled)
        this.preferences.putBoolean(SOUND_ENABLED_KEY, preferences.soundEnabled)
        _preferences.value = preferences
    }

    private fun loadPreferences(): NotificationPreferences =
        NotificationPreferences(
            notificationsEnabled = preferences.getBoolean(NOTIFICATIONS_ENABLED_KEY) ?: false,
            soundEnabled = preferences.getBoolean(SOUND_ENABLED_KEY) ?: true,
        )

    private companion object {
        const val NOTIFICATIONS_ENABLED_KEY = "notifications_enabled"
        const val SOUND_ENABLED_KEY = "notifications_sound_enabled"
    }
}
