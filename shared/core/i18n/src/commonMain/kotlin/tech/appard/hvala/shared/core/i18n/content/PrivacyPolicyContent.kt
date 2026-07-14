package tech.appard.hvala.shared.core.i18n.content

import tech.appard.hvala.shared.core.i18n.AppLanguage

internal fun privacyPolicyContent(language: AppLanguage): String = when (language) {
    AppLanguage.RU -> privacyPolicyContentRu
    AppLanguage.EN -> privacyPolicyContentEn
    AppLanguage.SR,
    AppLanguage.CNR,
    AppLanguage.BS,
    AppLanguage.HR,
    -> privacyPolicyContentSr
}
