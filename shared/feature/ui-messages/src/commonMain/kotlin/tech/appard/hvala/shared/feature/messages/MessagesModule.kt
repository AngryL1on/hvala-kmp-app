package tech.appard.hvala.shared.feature.messages

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val messagesFeatureModule = module {
    singleOf(::MessagesStateHolder)
}
