package tech.appard.hvala.shared.core.i18n

enum class AppLanguage(
    val code: String,
    val nativeName: String,
    val flagEmoji: String,
) {
    EN("en", "English", "🇬🇧"),
    CNR("cnr", "Crnogorski", "🇲🇪"),
    SR("sr", "Srpski", "🇷🇸"),
    BS("bs", "Bosanski", "🇧🇦"),
    HR("hr", "Hrvatski", "🇭🇷"),
    RU("ru", "Русский", "🇷🇺"),
    ;

    companion object {
        val pickerOrder: List<AppLanguage> = listOf(EN, CNR, SR, BS, HR, RU)

        val default: AppLanguage = RU

        fun fromCode(code: String?): AppLanguage? =
            entries.find { it.code == code }
    }
}
