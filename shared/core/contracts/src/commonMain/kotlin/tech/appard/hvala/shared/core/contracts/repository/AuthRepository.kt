package tech.appard.hvala.shared.core.contracts.repository

import tech.appard.hvala.shared.core.contracts.model.AuthCredentials
import tech.appard.hvala.shared.core.contracts.model.RegistrationData

interface AuthRepository {
    fun isAuthenticated(): Boolean

    suspend fun signIn(credentials: AuthCredentials): Boolean

    suspend fun signUp(data: RegistrationData): Boolean

    suspend fun signOut()
}
