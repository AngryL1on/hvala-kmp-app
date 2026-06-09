package tech.appard.hvala.shared.feature.settings.presentation.viewmodels

import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import tech.appard.hvala.shared.core.i18n.AppLanguage
import tech.appard.hvala.shared.core.i18n.SettingsStrings
import tech.appard.hvala.shared.core.i18n.strings
import tech.appard.hvala.shared.core.mvi.MviEffect
import tech.appard.hvala.shared.core.mvi.MviIntent
import tech.appard.hvala.shared.core.mvi.MviState
import tech.appard.hvala.shared.core.mvi.MviViewModel
import tech.appard.hvala.shared.feature.settings.domain.SetAppLanguageUseCase
import tech.appard.hvala.shared.feature.settings.domain.SetNotificationPreferencesUseCase
import tech.appard.hvala.shared.feature.settings.domain.model.NotificationPreferences
import tech.appard.hvala.shared.feature.settings.domain.repository.LocaleRepository
import tech.appard.hvala.shared.feature.settings.domain.repository.NotificationPreferencesRepository

enum class SettingsConfirmAction {
    Logout,
    DeleteAccount,
}

data class SettingsUiState(
    val language: AppLanguage = AppLanguage.default,
    val showLanguagePicker: Boolean = false,
    val draftLanguage: AppLanguage = AppLanguage.default,
    val showNotificationSettings: Boolean = false,
    val notificationPreferences: NotificationPreferences = NotificationPreferences(),
    val draftNotificationPreferences: NotificationPreferences = NotificationPreferences(),
    val confirmAction: SettingsConfirmAction? = null,
) : MviState {
    val strings: SettingsStrings
        get() = language.strings().settings
}

sealed interface SettingsIntent : MviIntent {
    data class MenuItemClicked(val id: String) : SettingsIntent
    data object LanguagePickerDismissed : SettingsIntent
    data class LanguageDraftSelected(val language: AppLanguage) : SettingsIntent
    data object LanguageConfirmed : SettingsIntent
    data object NotificationSettingsDismissed : SettingsIntent
    data class NotificationsEnabledChanged(val enabled: Boolean) : SettingsIntent
    data class NotificationSoundChanged(val enabled: Boolean) : SettingsIntent
    data object NotificationSettingsConfirmed : SettingsIntent
    data object LogoutRequested : SettingsIntent
    data object DeleteAccountRequested : SettingsIntent
    data object ConfirmDismissed : SettingsIntent
    data object LogoutConfirmed : SettingsIntent
    data object DeleteAccountConfirmed : SettingsIntent
}

sealed interface SettingsEffect : MviEffect {
    data object SessionEndRequested : SettingsEffect
}

class SettingsViewModel(
    private val localeRepository: LocaleRepository,
    private val notificationPreferencesRepository: NotificationPreferencesRepository,
    private val setAppLanguageUseCase: SetAppLanguageUseCase,
    private val setNotificationPreferencesUseCase: SetNotificationPreferencesUseCase,
) : MviViewModel<SettingsIntent, SettingsUiState, SettingsEffect>(SettingsUiState()) {

    init {
        viewModelScope.launch {
            localeRepository.languageFlow.collectLatest { language ->
                updateState { current ->
                    current.copy(
                        language = language,
                        draftLanguage = if (current.showLanguagePicker) {
                            current.draftLanguage
                        } else {
                            language
                        },
                    )
                }
            }
        }
        viewModelScope.launch {
            notificationPreferencesRepository.preferencesFlow.collectLatest { preferences ->
                updateState { current ->
                    current.copy(
                        notificationPreferences = preferences,
                        draftNotificationPreferences = if (current.showNotificationSettings) {
                            current.draftNotificationPreferences
                        } else {
                            preferences
                        },
                    )
                }
            }
        }
    }

    override suspend fun handleIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.MenuItemClicked -> when (intent.id) {
                "language" -> {
                    val currentLanguage = currentState().language
                    updateState {
                        it.copy(
                            showLanguagePicker = true,
                            draftLanguage = currentLanguage,
                        )
                    }
                }
                "notifications" -> {
                    val currentPreferences = currentState().notificationPreferences
                    updateState {
                        it.copy(
                            showNotificationSettings = true,
                            draftNotificationPreferences = currentPreferences,
                        )
                    }
                }
            }
            SettingsIntent.LanguagePickerDismissed -> {
                updateState {
                    it.copy(
                        showLanguagePicker = false,
                        draftLanguage = it.language,
                    )
                }
            }
            is SettingsIntent.LanguageDraftSelected -> {
                updateState { it.copy(draftLanguage = intent.language) }
            }
            SettingsIntent.LanguageConfirmed -> {
                val draftLanguage = currentState().draftLanguage
                setAppLanguageUseCase(draftLanguage)
                updateState {
                    it.copy(
                        showLanguagePicker = false,
                        draftLanguage = draftLanguage,
                    )
                }
            }
            SettingsIntent.NotificationSettingsDismissed -> {
                updateState {
                    it.copy(
                        showNotificationSettings = false,
                        draftNotificationPreferences = it.notificationPreferences,
                    )
                }
            }
            is SettingsIntent.NotificationsEnabledChanged -> {
                updateState { current ->
                    current.copy(
                        draftNotificationPreferences = current.draftNotificationPreferences.copy(
                            notificationsEnabled = intent.enabled,
                        ),
                    )
                }
            }
            is SettingsIntent.NotificationSoundChanged -> {
                updateState { current ->
                    current.copy(
                        draftNotificationPreferences = current.draftNotificationPreferences.copy(
                            soundEnabled = intent.enabled,
                        ),
                    )
                }
            }
            SettingsIntent.NotificationSettingsConfirmed -> {
                val draft = currentState().draftNotificationPreferences
                setNotificationPreferencesUseCase(draft)
                updateState {
                    it.copy(
                        showNotificationSettings = false,
                        notificationPreferences = draft,
                        draftNotificationPreferences = draft,
                    )
                }
            }
            SettingsIntent.LogoutRequested -> {
                updateState { it.copy(confirmAction = SettingsConfirmAction.Logout) }
            }
            SettingsIntent.DeleteAccountRequested -> {
                updateState { it.copy(confirmAction = SettingsConfirmAction.DeleteAccount) }
            }
            SettingsIntent.ConfirmDismissed -> {
                updateState { it.copy(confirmAction = null) }
            }
            SettingsIntent.LogoutConfirmed -> {
                updateState { it.copy(confirmAction = null) }
                sendEffect(SettingsEffect.SessionEndRequested)
            }
            SettingsIntent.DeleteAccountConfirmed -> {
                updateState { it.copy(confirmAction = null) }
                sendEffect(SettingsEffect.SessionEndRequested)
            }
        }
    }

    fun onMenuItemClick(id: String) = onIntent(SettingsIntent.MenuItemClicked(id))
    fun onLanguagePickerDismiss() = onIntent(SettingsIntent.LanguagePickerDismissed)
    fun onLanguageDraftSelected(language: AppLanguage) =
        onIntent(SettingsIntent.LanguageDraftSelected(language))
    fun onLanguageConfirmed() = onIntent(SettingsIntent.LanguageConfirmed)
    fun onNotificationSettingsDismiss() = onIntent(SettingsIntent.NotificationSettingsDismissed)
    fun onNotificationsEnabledChanged(enabled: Boolean) =
        onIntent(SettingsIntent.NotificationsEnabledChanged(enabled))
    fun onNotificationSoundChanged(enabled: Boolean) =
        onIntent(SettingsIntent.NotificationSoundChanged(enabled))
    fun onNotificationSettingsConfirmed() = onIntent(SettingsIntent.NotificationSettingsConfirmed)
    fun onLogoutClick() = onIntent(SettingsIntent.LogoutRequested)
    fun onDeleteAccountClick() = onIntent(SettingsIntent.DeleteAccountRequested)
    fun onConfirmDismiss() = onIntent(SettingsIntent.ConfirmDismissed)
    fun onLogoutConfirmed() = onIntent(SettingsIntent.LogoutConfirmed)
    fun onDeleteAccountConfirmed() = onIntent(SettingsIntent.DeleteAccountConfirmed)
}

typealias SettingsStateHolder = SettingsViewModel
