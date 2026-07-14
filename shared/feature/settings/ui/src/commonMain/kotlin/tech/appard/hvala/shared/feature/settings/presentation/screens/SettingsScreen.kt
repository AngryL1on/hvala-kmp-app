package tech.appard.hvala.shared.feature.settings.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import tech.appard.hvala.shared.core.ui.platform.rememberImagePicker
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tech.appard.hvala.shared.core.ui.components.buttons.PrimaryButton
import tech.appard.hvala.shared.core.ui.components.dialogs.HvalaConfirmDialog
import tech.appard.hvala.shared.core.ui.theme.ButtonLarge
import tech.appard.hvala.shared.core.ui.theme.HvalaTheme
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.ScreenBackground
import tech.appard.hvala.shared.core.i18n.AppLanguage
import tech.appard.hvala.shared.feature.settings.presentation.components.LanguagePickerDialog
import tech.appard.hvala.shared.feature.settings.presentation.components.NotificationSettingsDialog
import tech.appard.hvala.shared.feature.settings.presentation.components.SettingsMenuCard
import tech.appard.hvala.shared.feature.settings.presentation.components.SettingsProfileHeader
import tech.appard.hvala.shared.feature.settings.presentation.model.SettingsMenuItem
import tech.appard.hvala.shared.feature.settings.presentation.viewmodels.SettingsConfirmAction
import tech.appard.hvala.shared.feature.settings.presentation.viewmodels.SettingsEffect
import tech.appard.hvala.shared.feature.settings.presentation.viewmodels.SettingsViewModel
import tech.appard.hvala.shared.feature.settings.presentation.viewmodels.SettingsUiState

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    fullName: String,
    email: String,
    phone: String,
    modifier: Modifier = Modifier,
    avatarUrl: String? = null,
    onEditProfileClick: () -> Unit = {},
    onAvatarPicked: (String) -> Unit = {},
    onSessionEnd: () -> Unit = {},
    onInformationClick: () -> Unit = {},
    onPrivacyPolicyClick: () -> Unit = {},
    onContactsClick: () -> Unit = {},
    onHelpClick: () -> Unit = {},
) {
    val state by viewModel.state.collectAsState()
    val imagePicker = rememberImagePicker()

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                SettingsEffect.SessionEndRequested -> onSessionEnd()
                SettingsEffect.InformationRequested -> onInformationClick()
                SettingsEffect.PrivacyPolicyRequested -> onPrivacyPolicyClick()
                SettingsEffect.ContactsRequested -> onContactsClick()
                SettingsEffect.HelpRequested -> onHelpClick()
            }
        }
    }

    SettingsContent(
        modifier = modifier,
        state = state,
        fullName = fullName,
        email = email,
        phone = phone,
        avatarUrl = avatarUrl,
        onEditProfileClick = onEditProfileClick,
        onEditAvatarClick = { imagePicker.pick { uri -> uri?.let(onAvatarPicked) } },
        onMenuItemClick = viewModel::onMenuItemClick,
        onLogoutClick = viewModel::onLogoutClick,
        onDeleteAccountClick = viewModel::onDeleteAccountClick,
        onConfirmDismiss = viewModel::onConfirmDismiss,
        onLogoutConfirmed = viewModel::onLogoutConfirmed,
        onDeleteAccountConfirmed = viewModel::onDeleteAccountConfirmed,
        onLanguagePickerDismiss = viewModel::onLanguagePickerDismiss,
        onLanguageDraftSelected = viewModel::onLanguageDraftSelected,
        onLanguageConfirmed = viewModel::onLanguageConfirmed,
        onNotificationSettingsDismiss = viewModel::onNotificationSettingsDismiss,
        onNotificationsEnabledChanged = viewModel::onNotificationsEnabledChanged,
        onNotificationSoundChanged = viewModel::onNotificationSoundChanged,
        onNotificationSettingsConfirmed = viewModel::onNotificationSettingsConfirmed,
    )
}

@Composable
private fun SettingsContent(
    state: SettingsUiState,
    fullName: String,
    email: String,
    phone: String,
    avatarUrl: String?,
    onEditProfileClick: () -> Unit,
    onEditAvatarClick: () -> Unit,
    onMenuItemClick: (String) -> Unit,
    onLogoutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    onConfirmDismiss: () -> Unit,
    onLogoutConfirmed: () -> Unit,
    onDeleteAccountConfirmed: () -> Unit,
    onLanguagePickerDismiss: () -> Unit,
    onLanguageDraftSelected: (AppLanguage) -> Unit,
    onLanguageConfirmed: () -> Unit,
    onNotificationSettingsDismiss: () -> Unit,
    onNotificationsEnabledChanged: (Boolean) -> Unit,
    onNotificationSoundChanged: (Boolean) -> Unit,
    onNotificationSettingsConfirmed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val strings = state.strings

    val generalMenuItems = remember(strings) {
        listOf(
            SettingsMenuItem("notifications", strings.notifications, Icons.Outlined.Notifications),
            SettingsMenuItem("language", strings.language, Icons.Outlined.Language),
            SettingsMenuItem("info", strings.info, Icons.Outlined.Info),
            SettingsMenuItem("privacy_policy", strings.privacyPolicy, Icons.Outlined.Description),
            SettingsMenuItem("contacts", strings.contacts, Icons.Outlined.Badge),
            SettingsMenuItem("help", strings.help, Icons.AutoMirrored.Outlined.HelpOutline),
        )
    }
    val deleteAccountItem = remember(strings) {
        SettingsMenuItem(
            id = "delete_account",
            title = strings.deleteAccount,
            icon = Icons.Outlined.DeleteOutline,
            isDestructive = true,
        )
    }
    val logoutItem = remember(strings) {
        SettingsMenuItem(
            id = "logout",
            title = strings.logout,
            icon = Icons.AutoMirrored.Outlined.Logout,
            isDestructive = true,
        )
    }

    if (state.showNotificationSettings) {
        NotificationSettingsDialog(
            title = strings.notificationsTitle,
            notificationsLabel = strings.notificationsToggle,
            soundLabel = strings.notificationsSoundToggle,
            cancelText = strings.cancel,
            confirmText = strings.ok,
            preferences = state.draftNotificationPreferences,
            onNotificationsChanged = onNotificationsEnabledChanged,
            onSoundChanged = onNotificationSoundChanged,
            onConfirm = onNotificationSettingsConfirmed,
            onDismiss = onNotificationSettingsDismiss,
        )
    }

    if (state.showLanguagePicker) {
        LanguagePickerDialog(
            title = strings.selectLanguage,
            languages = AppLanguage.pickerOrder,
            selectedLanguage = state.draftLanguage,
            cancelText = strings.cancel,
            confirmText = strings.ok,
            onLanguageSelected = onLanguageDraftSelected,
            onConfirm = onLanguageConfirmed,
            onDismiss = onLanguagePickerDismiss,
        )
    }

    when (state.confirmAction) {
        SettingsConfirmAction.Logout -> HvalaConfirmDialog(
            title = strings.logoutTitle,
            message = strings.logoutMessage,
            confirmText = strings.logoutConfirm,
            dismissText = strings.cancel,
            isDestructive = true,
            onConfirm = onLogoutConfirmed,
            onDismiss = onConfirmDismiss,
        )
        SettingsConfirmAction.DeleteAccount -> HvalaConfirmDialog(
            title = strings.deleteTitle,
            message = strings.deleteMessage,
            confirmText = strings.deleteConfirm,
            dismissText = strings.cancel,
            isDestructive = true,
            onConfirm = onDeleteAccountConfirmed,
            onDismiss = onConfirmDismiss,
        )
        null -> Unit
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = dimensions.horizontalMedium)
            .padding(bottom = dimensions.verticalLarge),
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalMedium),
    ) {
        SettingsProfileHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensions.verticalLarge),
            fullName = fullName,
            email = email,
            phone = phone,
            avatarUrl = avatarUrl,
            onEditAvatarClick = onEditAvatarClick,
        )

        PrimaryButton(
            text = strings.editProfile,
            onClick = onEditProfileClick,
            modifier = Modifier.fillMaxWidth(),
            textStyle = ButtonLarge,
        )

        SettingsMenuCard(
            items = generalMenuItems,
            onItemClick = { item -> onMenuItemClick(item.id) },
        )

        SettingsMenuCard(
            items = listOf(deleteAccountItem),
            onItemClick = { onDeleteAccountClick() },
        )

        SettingsMenuCard(
            items = listOf(logoutItem),
            onItemClick = { onLogoutClick() },
        )
    }
}

@Composable
@Preview
private fun SettingsScreenPreview() {
    HvalaTheme {
        SettingsContent(
            state = SettingsUiState(language = AppLanguage.RU),
            fullName = "Vadim",
            email = "vadim.lushina@gmail.com",
            phone = "+382 67 123 456",
            avatarUrl = null,
            onEditProfileClick = {},
            onEditAvatarClick = {},
            onMenuItemClick = {},
            onLogoutClick = {},
            onDeleteAccountClick = {},
            onConfirmDismiss = {},
            onLogoutConfirmed = {},
            onDeleteAccountConfirmed = {},
            onLanguagePickerDismiss = {},
            onLanguageDraftSelected = {},
            onLanguageConfirmed = {},
            onNotificationSettingsDismiss = {},
            onNotificationsEnabledChanged = {},
            onNotificationSoundChanged = {},
            onNotificationSettingsConfirmed = {},
        )
    }
}
