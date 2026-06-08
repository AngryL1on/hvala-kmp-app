package tech.appard.hvala.shared.feature.messages.domain.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import tech.appard.hvala.shared.feature.messages.domain.GetChatMessagesUseCase
import tech.appard.hvala.shared.feature.messages.domain.GetChatThreadUseCase
import tech.appard.hvala.shared.feature.messages.domain.GetChatThreadsUseCase
import tech.appard.hvala.shared.feature.messages.domain.OpenChatForListingUseCase
import tech.appard.hvala.shared.feature.messages.domain.SendChatMessageUseCase

val messagesDomainModule = module {
    factoryOf(::GetChatThreadsUseCase)
    factoryOf(::GetChatMessagesUseCase)
    factoryOf(::SendChatMessageUseCase)
    factoryOf(::OpenChatForListingUseCase)
    factoryOf(::GetChatThreadUseCase)
}
