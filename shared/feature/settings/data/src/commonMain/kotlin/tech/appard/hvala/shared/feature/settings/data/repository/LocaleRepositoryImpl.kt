package tech.appard.hvala.shared.feature.settings.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import tech.appard.hvala.shared.core.datastore.PreferencesStore
import tech.appard.hvala.shared.core.i18n.AppLanguage
import tech.appard.hvala.shared.feature.settings.domain.repository.LocaleRepository

internal class LocaleRepositoryImpl(
    private val preferences: PreferencesStore,
) : LocaleRepository {

    private val _language = MutableStateFlow(loadLanguage())
    override val languageFlow: StateFlow<AppLanguage> = _language.asStateFlow()

    override fun getLanguage(): AppLanguage = _language.value

    override suspend fun setLanguage(language: AppLanguage) {
        preferences.putString(LANGUAGE_KEY, language.code)
        _language.value = language
    }

    private fun loadLanguage(): AppLanguage {
        val code = preferences.getString(LANGUAGE_KEY)
        return AppLanguage.fromCode(code) ?: AppLanguage.default
    }

    private companion object {
        const val LANGUAGE_KEY = "app_language"
    }
}
