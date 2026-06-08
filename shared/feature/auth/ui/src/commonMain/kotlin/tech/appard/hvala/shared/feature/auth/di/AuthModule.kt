package tech.appard.hvala.shared.feature.auth.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import tech.appard.hvala.shared.feature.auth.presentation.AuthStateHolder

val authFeatureModule = module {
    singleOf(::AuthStateHolder)
}
