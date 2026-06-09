package tech.appard.hvala.shared.feature.settings.domain.repository

import kotlinx.coroutines.flow.StateFlow
import tech.appard.hvala.shared.core.i18n.AppLanguage

interface LocaleRepository {
    val languageFlow: StateFlow<AppLanguage>

    fun getLanguage(): AppLanguage

    suspend fun setLanguage(language: AppLanguage)
}
