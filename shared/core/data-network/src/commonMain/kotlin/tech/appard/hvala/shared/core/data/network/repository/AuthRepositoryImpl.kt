package tech.appard.hvala.shared.core.data.network.repository

import kotlinx.coroutines.flow.StateFlow
import tech.appard.hvala.shared.core.datastore.session.SessionStorage
import tech.appard.hvala.shared.feature.auth.domain.model.AuthCredentials
import tech.appard.hvala.shared.feature.auth.domain.model.RegistrationData
import tech.appard.hvala.shared.feature.auth.domain.model.UserProfile
import tech.appard.hvala.shared.feature.auth.domain.repository.AuthRepository
import tech.appard.hvala.shared.core.data.network.NetworkClient

class AuthRepositoryImpl(
    private val networkClient: NetworkClient,
    private val sessionStorage: SessionStorage,
) : AuthRepository {
    override fun isAuthenticated(): Boolean = sessionStorage.isAuthenticated()

    override val isAuthenticatedFlow: StateFlow<Boolean> = sessionStorage.isAuthenticatedFlow

    override suspend fun signIn(credentials: AuthCredentials): Boolean {
        networkClient.httpClient
        val success = credentials.login.isNotBlank() && credentials.password.length >= 4
        if (success) {
            sessionStorage.saveSession(
                UserProfile(
                    id = "1",
                    fullName = credentials.login.substringBefore("@").replaceFirstChar { it.uppercaseChar() },
                    email = credentials.login.trim(),
                    phone = "+382 67 123 456",
                ),
            )
        }
        return success
    }

    override suspend fun signUp(data: RegistrationData): Boolean {
        networkClient.httpClient
        val success = data.fullName.isNotBlank() &&
            data.email.contains("@") &&
            data.phone.filter(Char::isDigit).length >= 10 &&
            data.password.length >= 4
        if (success) {
            sessionStorage.saveSession(
                UserProfile(
                    id = "1",
                    fullName = data.fullName.trim(),
                    email = data.email.trim(),
                    phone = data.phone.trim(),
                ),
            )
        }
        return success
    }

    override suspend fun signOut() {
        networkClient.httpClient
        sessionStorage.clearSession()
    }
}
