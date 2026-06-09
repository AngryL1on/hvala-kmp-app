package tech.appard.hvala.shared.core.datastore.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import tech.appard.hvala.shared.core.datastore.PreferencesStore
import tech.appard.hvala.shared.feature.auth.domain.model.UserProfile

class SessionStorage(
    private val preferences: PreferencesStore,
) {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    private var cachedAuthenticated: Boolean = preferences.getBoolean(KEY_AUTHENTICATED) ?: false
    private var cachedProfile: UserProfile? = loadStoredProfile()
    private val _isAuthenticatedFlow = MutableStateFlow(cachedAuthenticated)
    val isAuthenticatedFlow: StateFlow<Boolean> = _isAuthenticatedFlow.asStateFlow()

    fun isAuthenticated(): Boolean = cachedAuthenticated

    fun currentProfile(): UserProfile = when {
        cachedAuthenticated && cachedProfile != null -> cachedProfile!!
        else -> GUEST_PROFILE
    }

    fun saveSession(profile: UserProfile) {
        cachedAuthenticated = true
        cachedProfile = profile
        preferences.putBoolean(KEY_AUTHENTICATED, true)
        preferences.putString(KEY_PROFILE, encodeProfile(profile))
        _isAuthenticatedFlow.value = true
    }

    fun updateProfile(profile: UserProfile) {
        if (!cachedAuthenticated) return
        cachedProfile = profile
        preferences.putString(KEY_PROFILE, encodeProfile(profile))
    }

    fun clearSession() {
        cachedAuthenticated = false
        cachedProfile = null
        preferences.putBoolean(KEY_AUTHENTICATED, false)
        preferences.remove(KEY_PROFILE)
        _isAuthenticatedFlow.value = false
    }

    private fun loadStoredProfile(): UserProfile? {
        val raw = preferences.getString(KEY_PROFILE) ?: return null
        return runCatching {
            json.decodeFromString<StoredUserProfile>(raw).toDomain()
        }.getOrNull()
    }

    private fun encodeProfile(profile: UserProfile): String =
        json.encodeToString(profile.toStored())

    private companion object {
        const val KEY_AUTHENTICATED = "session_authenticated"
        const val KEY_PROFILE = "session_profile"

        val GUEST_PROFILE = UserProfile(
            id = "1",
            fullName = "Vadim",
            email = "vadim@example.com",
            phone = "+382 67 123 456",
        )
    }
}
