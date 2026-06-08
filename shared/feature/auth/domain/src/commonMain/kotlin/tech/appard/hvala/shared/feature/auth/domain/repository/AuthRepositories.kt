package tech.appard.hvala.shared.feature.auth.domain.repository

import tech.appard.hvala.shared.feature.auth.domain.model.AuthCredentials
import tech.appard.hvala.shared.feature.auth.domain.model.RegistrationData
import tech.appard.hvala.shared.feature.auth.domain.model.UserProfile

interface AuthRepository {
    fun isAuthenticated(): Boolean

    suspend fun signIn(credentials: AuthCredentials): Boolean

    suspend fun signUp(data: RegistrationData): Boolean

    suspend fun signOut()
}

interface ProfileRepository {
    suspend fun getCurrentProfile(): UserProfile
}
