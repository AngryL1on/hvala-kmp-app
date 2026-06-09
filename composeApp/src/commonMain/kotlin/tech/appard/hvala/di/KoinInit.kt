package tech.appard.hvala.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module

private var koinStarted = false

fun initKoin() {
    initKoinInternal()
}

internal fun initKoinInternal() {
    if (koinStarted) return
    koinStarted = true

    startKoin {
        modules(appModule, platformKoinModule())
    }
}

internal expect fun platformKoinModule(): Module
