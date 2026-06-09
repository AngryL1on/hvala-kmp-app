package tech.appard.hvala.shared.feature.profile.domain.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import tech.appard.hvala.shared.feature.auth.domain.GetCurrentProfileUseCase
import tech.appard.hvala.shared.feature.auth.domain.UpdateProfileUseCase
import tech.appard.hvala.shared.feature.profile.domain.GetProfileOverviewUseCase
import tech.appard.hvala.shared.feature.profile.domain.GetSellerProfileUseCase

val profileDomainModule = module {
    factoryOf(::GetProfileOverviewUseCase)
    factoryOf(::GetSellerProfileUseCase)
    factoryOf(::GetCurrentProfileUseCase)
    factoryOf(::UpdateProfileUseCase)
}
