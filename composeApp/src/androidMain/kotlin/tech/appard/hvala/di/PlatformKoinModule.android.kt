package tech.appard.hvala.di

import android.content.Context
import org.koin.core.module.Module
import org.koin.dsl.module
import tech.appard.hvala.shared.core.database.driver.DatabaseDriverFactory

internal object AndroidKoinContext {
    lateinit var applicationContext: Context
        private set

    fun init(context: Context) {
        applicationContext = context.applicationContext
    }
}

fun initKoin(applicationContext: Context) {
    AndroidKoinContext.init(applicationContext)
    initKoinInternal()
}

internal actual fun platformKoinModule(): Module = module {
    single { DatabaseDriverFactory(AndroidKoinContext.applicationContext) }
}
