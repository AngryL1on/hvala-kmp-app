package tech.appard.hvala.shared.feature.profile

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val profileFeatureModule = module {
    singleOf(::ProfileStateHolder)
    singleOf(::SellerProfileStateHolder)
}
