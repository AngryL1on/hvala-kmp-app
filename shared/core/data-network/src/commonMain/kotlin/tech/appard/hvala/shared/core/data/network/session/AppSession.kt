package tech.appard.hvala.shared.core.data.network.session

import tech.appard.hvala.shared.feature.auth.domain.model.UserProfile

internal object AppSession {
    var isAuthenticated: Boolean = false
    private var registeredProfile: UserProfile? = null

    fun currentProfile(): UserProfile = registeredProfile ?: DEFAULT_PROFILE

    fun register(profile: UserProfile) {
        registeredProfile = profile
        isAuthenticated = true
    }

    fun clear() {
        registeredProfile = null
        isAuthenticated = false
    }

    private val DEFAULT_PROFILE = UserProfile(
        id = "1",
        fullName = "Vadim",
        email = "vadim@example.com",
    )
}
