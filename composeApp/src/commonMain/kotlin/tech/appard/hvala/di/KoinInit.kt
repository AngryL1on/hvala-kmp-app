package tech.appard.hvala.di

import org.koin.core.context.startKoin

private var koinStarted = false

fun initKoin() {
    if (koinStarted) return
    koinStarted = true

    startKoin {
        modules(appModule)
    }
}
