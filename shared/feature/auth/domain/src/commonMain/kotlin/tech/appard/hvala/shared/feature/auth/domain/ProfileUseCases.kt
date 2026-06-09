package tech.appard.hvala.shared.feature.auth.domain

import tech.appard.hvala.shared.feature.auth.domain.model.UserProfile
import tech.appard.hvala.shared.feature.auth.domain.repository.ProfileRepository

class GetCurrentProfileUseCase(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(): UserProfile = profileRepository.getCurrentProfile()
}

class UpdateProfileUseCase(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(profile: UserProfile) {
        profileRepository.updateProfile(profile)
    }
}
