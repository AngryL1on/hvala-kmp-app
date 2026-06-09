package tech.appard.hvala.shared.core.data.network.repository

import tech.appard.hvala.shared.feature.auth.domain.model.UserProfile
import tech.appard.hvala.shared.feature.auth.domain.repository.ProfileRepository
import tech.appard.hvala.shared.core.data.network.NetworkClient
import tech.appard.hvala.shared.core.data.network.session.AppSession

class ProfileRepositoryImpl(
    private val networkClient: NetworkClient,
) : ProfileRepository {
    override suspend fun getCurrentProfile(): UserProfile {
        networkClient.httpClient
        return AppSession.currentProfile()
    }

    override suspend fun updateProfile(profile: UserProfile) {
        networkClient.httpClient
        AppSession.updateProfile(profile)
    }
}
