package tech.appard.hvala.shared.core.data.network.repository

import tech.appard.hvala.shared.feature.profile.api.model.UserProfile
import tech.appard.hvala.shared.feature.profile.api.repository.ProfileRepository
import tech.appard.hvala.shared.core.data.network.NetworkClient

class ProfileRepositoryImpl(
    private val networkClient: NetworkClient,
) : ProfileRepository {
    override suspend fun getCurrentProfile(): UserProfile {
        networkClient.httpClient
        // Placeholder while backend contract is being integrated.
        return UserProfile(
            id = "1",
            fullName = "Vadim",
            email = "vadim@example.com",
        )
    }
}
