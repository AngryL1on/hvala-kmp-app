package tech.appard.hvala.shared.feature.profile.api.repository

import tech.appard.hvala.shared.feature.profile.api.model.UserProfile

interface ProfileRepository {
    suspend fun getCurrentProfile(): UserProfile
}
