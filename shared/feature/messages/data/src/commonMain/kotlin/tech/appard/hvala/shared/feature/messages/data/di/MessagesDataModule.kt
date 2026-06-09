package tech.appard.hvala.shared.feature.messages.data.di

import org.koin.dsl.module
import tech.appard.hvala.shared.feature.messages.data.repository.JsonMessagesRepository
import tech.appard.hvala.shared.feature.messages.data.source.MessagesJsonDataSource
import tech.appard.hvala.shared.feature.messages.domain.repository.MessagesRepository

val messagesDataModule = module {
    single { MessagesJsonDataSource() }
    single<MessagesRepository> { JsonMessagesRepository(get(), get(), get(), get(), get()) }
}
