package tech.appard.hvala.shared.feature.favorites.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import tech.appard.hvala.shared.feature.favorites.presentation.viewmodels.FavoritesViewModel

val favoritesFeatureModule = module {
    singleOf(::FavoritesViewModel)
}
