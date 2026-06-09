package tech.appard.hvala.shared.feature.auth.domain.repository

import kotlinx.coroutines.flow.StateFlow
import tech.appard.hvala.shared.feature.auth.domain.model.AuthCredentials
import tech.appard.hvala.shared.feature.auth.domain.model.RegistrationData
import tech.appard.hvala.shared.feature.auth.domain.model.UserProfile

interface AuthRepository {
    fun isAuthenticated(): Boolean

    val isAuthenticatedFlow: StateFlow<Boolean>

    suspend fun signIn(credentials: AuthCredentials): Boolean

    suspend fun signUp(data: RegistrationData): Boolean

    suspend fun signOut()
}

interface ProfileRepository {
    suspend fun getCurrentProfile(): UserProfile

    suspend fun updateProfile(profile: UserProfile)
}
