package tech.appard.hvala.shared.feature.favorites

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val favoritesFeatureModule = module {
    singleOf(::FavoritesViewModel)
}
