package tech.appard.hvala.shared.feature.auth.api.repository

import tech.appard.hvala.shared.feature.auth.api.model.AuthCredentials

interface AuthRepository {
    fun isAuthenticated(): Boolean

    suspend fun signIn(credentials: AuthCredentials): Boolean

    suspend fun signOut()
}
