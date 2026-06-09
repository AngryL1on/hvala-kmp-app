package tech.appard.hvala.shared.core.datastore

interface PreferencesStore {
    fun getString(key: String): String?

    fun putString(key: String, value: String?)

    fun getBoolean(key: String): Boolean?

    fun putBoolean(key: String, value: Boolean)

    fun remove(key: String)
}
