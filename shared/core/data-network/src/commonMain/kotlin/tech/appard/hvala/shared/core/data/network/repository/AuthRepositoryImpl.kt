package tech.appard.hvala.shared.core.data.network.repository

import tech.appard.hvala.shared.core.contracts.model.AuthCredentials
import tech.appard.hvala.shared.core.contracts.repository.AuthRepository
import tech.appard.hvala.shared.core.data.network.NetworkClient

class AuthRepositoryImpl(
    private val networkClient: NetworkClient,
) : AuthRepository {
    private var authenticated = false

    override fun isAuthenticated(): Boolean = authenticated

    override suspend fun signIn(credentials: AuthCredentials): Boolean {
        networkClient.httpClient
        // Placeholder for real API call. Keeping it deterministic for now.
        val success = credentials.login.isNotBlank() && credentials.password.length >= 4
        if (success) {
            authenticated = true
        }
        return success
    }

    override suspend fun signOut() {
        networkClient.httpClient
        authenticated = false
    }
}
