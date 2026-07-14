package tech.appard.hvala.shared.core.i18n

import tech.appard.hvala.shared.core.i18n.content.privacyPolicyContent

data class PrivacyPolicyStrings(
    val screenTitle: String,
    val body: String,
)

internal fun AppLanguage.privacyPolicyStrings(): PrivacyPolicyStrings = PrivacyPolicyStrings(
    screenTitle = privacyPolicyScreenTitle(this),
    body = privacyPolicyContent(this),
)

private fun privacyPolicyScreenTitle(language: AppLanguage): String = when (language) {
    AppLanguage.RU -> "Политика обработки данных"
    AppLanguage.EN -> "Privacy Policy"
    AppLanguage.SR -> "Politika obrade podataka"
    AppLanguage.CNR -> "Politika obrade podataka"
    AppLanguage.HR -> "Politika obrade podataka"
    AppLanguage.BS -> "Politika obrade podataka"
}
