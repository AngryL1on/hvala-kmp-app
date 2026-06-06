package tech.appard.hvala.shared.core.contracts.repository

import tech.appard.hvala.shared.core.contracts.model.AuthCredentials

interface AuthRepository {
    fun isAuthenticated(): Boolean

    suspend fun signIn(credentials: AuthCredentials): Boolean

    suspend fun signOut()
}
