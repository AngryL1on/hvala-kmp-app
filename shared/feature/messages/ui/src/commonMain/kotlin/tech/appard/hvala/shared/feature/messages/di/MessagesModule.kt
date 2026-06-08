package tech.appard.hvala.shared.feature.messages.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import tech.appard.hvala.shared.feature.messages.presentation.viewmodels.MessagesViewModel

val messagesFeatureModule = module {
    singleOf(::MessagesViewModel)
}
