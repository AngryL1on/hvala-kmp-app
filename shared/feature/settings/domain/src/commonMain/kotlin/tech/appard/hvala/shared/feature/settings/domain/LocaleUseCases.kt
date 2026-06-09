package tech.appard.hvala.shared.feature.settings.domain

import tech.appard.hvala.shared.core.i18n.AppLanguage
import tech.appard.hvala.shared.feature.settings.domain.repository.LocaleRepository

class GetAppLanguageUseCase(
    private val localeRepository: LocaleRepository,
) {
    operator fun invoke(): AppLanguage = localeRepository.getLanguage()
}

class SetAppLanguageUseCase(
    private val localeRepository: LocaleRepository,
) {
    suspend operator fun invoke(language: AppLanguage) {
        localeRepository.setLanguage(language)
    }
}
