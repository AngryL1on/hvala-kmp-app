package tech.appard.hvala.shared.core.data.network.repository

import tech.appard.hvala.shared.core.datastore.session.SessionStorage
import tech.appard.hvala.shared.feature.auth.domain.model.UserProfile
import tech.appard.hvala.shared.feature.auth.domain.repository.ProfileRepository
import tech.appard.hvala.shared.core.data.network.NetworkClient

class ProfileRepositoryImpl(
    private val networkClient: NetworkClient,
    private val sessionStorage: SessionStorage,
) : ProfileRepository {
    override suspend fun getCurrentProfile(): UserProfile {
        networkClient.httpClient
        return sessionStorage.currentProfile()
    }

    override suspend fun updateProfile(profile: UserProfile) {
        networkClient.httpClient
        sessionStorage.updateProfile(profile)
    }
}
