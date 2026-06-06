package tech.appard.hvala.shared.core.contracts.repository

import tech.appard.hvala.shared.core.contracts.model.UserProfile

interface ProfileRepository {
    suspend fun getCurrentProfile(): UserProfile
}
