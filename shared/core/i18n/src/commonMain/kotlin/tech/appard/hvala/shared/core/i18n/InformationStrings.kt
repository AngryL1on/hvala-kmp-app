package tech.appard.hvala.shared.core.i18n
data class InformationStrings(
    val screenTitle: String,
    val chooseLanguage: String,
)

internal fun AppLanguage.informationStrings(): InformationStrings = when (this) {
    AppLanguage.RU -> InformationStrings(
        screenTitle = "Часто задаваемые вопросы",
        chooseLanguage = "Выберите ваш язык",
    )
    AppLanguage.EN -> InformationStrings(
        screenTitle = "FAQ Page",
        chooseLanguage = "Choose your language",
    )
    AppLanguage.SR -> InformationStrings(
        screenTitle = "FAQ stranica",
        chooseLanguage = "Izaberite svoj jezik",
    )
    AppLanguage.CNR -> InformationStrings(
        screenTitle = "FAQ stranica",
        chooseLanguage = "Izaberite svoj jezik",
    )
    AppLanguage.BS -> InformationStrings(
        screenTitle = "FAQ stranica",
        chooseLanguage = "Izaberite svoj jezik",
    )
    AppLanguage.HR -> InformationStrings(
        screenTitle = "FAQ stranica",
        chooseLanguage = "Izaberite svoj jezik",
    )
}
