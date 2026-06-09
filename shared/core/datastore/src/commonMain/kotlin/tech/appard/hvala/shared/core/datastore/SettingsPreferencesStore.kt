package tech.appard.hvala.shared.core.datastore

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

internal class SettingsPreferencesStore(
    private val settings: Settings,
) : PreferencesStore {

    override fun getString(key: String): String? = settings.getStringOrNull(key)

    override fun putString(key: String, value: String?) {
        if (value == null) {
            settings.remove(key)
        } else {
            settings[key] = value
        }
    }

    override fun getBoolean(key: String): Boolean? = settings.getBooleanOrNull(key)

    override fun putBoolean(key: String, value: Boolean) {
        settings[key] = value
    }

    override fun remove(key: String) {
        settings.remove(key)
    }
}
