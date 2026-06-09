package tech.appard.hvala.shared.core.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

data class AppStrings(
    val common: CommonStrings,
    val nav: NavStrings,
    val auth: AuthStrings,
    val listings: ListingsStrings,
    val messages: MessagesStrings,
    val profile: ProfileStrings,
    val favorites: FavoritesStrings,
    val settings: SettingsStrings,
)

fun AppLanguage.strings(): AppStrings = AppStrings(
    common = commonStrings(),
    nav = navStrings(),
    auth = authStrings(),
    listings = listingsStrings(),
    messages = messagesStrings(),
    profile = profileStrings(),
    favorites = favoritesStrings(),
    settings = settingsStrings(),
)

val LocalAppLanguage = staticCompositionLocalOf { AppLanguage.default }

val LocalAppStrings = staticCompositionLocalOf { AppLanguage.default.strings() }

@Composable
fun ProvideAppLocalization(
    language: AppLanguage,
    content: @Composable () -> Unit,
) {
    val strings = language.strings()
    CompositionLocalProvider(
        LocalAppLanguage provides language,
        LocalAppStrings provides strings,
        content = content,
    )
}

@Composable
fun appStrings(): AppStrings = LocalAppStrings.current

@Composable
fun appLanguage(): AppLanguage = LocalAppLanguage.current
