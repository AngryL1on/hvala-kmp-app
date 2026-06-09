package tech.appard.hvala.shared.feature.profile.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import tech.appard.hvala.shared.feature.profile.presentation.viewmodels.EditProfileViewModel
import tech.appard.hvala.shared.feature.profile.presentation.viewmodels.ProfileViewModel
import tech.appard.hvala.shared.feature.profile.presentation.viewmodels.SellerProfileViewModel

val profileFeatureModule = module {
    singleOf(::ProfileViewModel)
    singleOf(::SellerProfileViewModel)
    singleOf(::EditProfileViewModel)
}
