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
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tech.appard.hvala.shared.core.ui.components.buttons.PrimaryButton
import tech.appard.hvala.shared.core.ui.components.dialogs.HvalaConfirmDialog
import tech.appard.hvala.shared.core.ui.theme.ButtonLarge
import tech.appard.hvala.shared.core.ui.theme.HvalaTheme
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.ScreenBackground
import tech.appard.hvala.shared.feature.settings.presentation.model.SettingsMenuItem
import tech.appard.hvala.shared.feature.settings.presentation.components.SettingsMenuCard
import tech.appard.hvala.shared.feature.settings.presentation.components.SettingsProfileHeader

private enum class SettingsConfirmAction {
    Logout,
    DeleteAccount,
}

@Composable
fun SettingsScreen(
    fullName: String,
    email: String,
    modifier: Modifier = Modifier,
    avatarUrl: String? = null,
    onEditProfileClick: () -> Unit = {},
    onEditAvatarClick: () -> Unit = {},
    onMenuItemClick: (SettingsMenuItem) -> Unit = {},
    onDeleteAccountClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
) {
    val dimensions = LocalDimensions.current
    var confirmAction by remember { mutableStateOf<SettingsConfirmAction?>(null) }

    val generalMenuItems = remember {
        listOf(
            SettingsMenuItem("notifications", "Уведомления", Icons.Outlined.Notifications),
            SettingsMenuItem("language", "Язык", Icons.Outlined.Language),
            SettingsMenuItem("info", "Информация", Icons.Outlined.Info),
            SettingsMenuItem("contacts", "Контакты", Icons.Outlined.Badge),
            SettingsMenuItem("help", "Помощь", Icons.AutoMirrored.Outlined.HelpOutline),
        )
    }
    val deleteAccountItem = remember {
        SettingsMenuItem(
            id = "delete_account",
            title = "Удалить аккаунт",
            icon = Icons.Outlined.DeleteOutline,
            isDestructive = true,
        )
    }
    val logoutItem = remember {
        SettingsMenuItem(
            id = "logout",
            title = "Выйти",
            icon = Icons.AutoMirrored.Outlined.Logout,
            isDestructive = true,
        )
    }

    when (confirmAction) {
        SettingsConfirmAction.Logout -> HvalaConfirmDialog(
            title = "Выйти из аккаунта?",
            message = "Вы уверены, что хотите выйти? Для входа потребуется снова ввести логин и пароль.",
            confirmText = "Выйти",
            isDestructive = true,
            onConfirm = {
                confirmAction = null
                onLogoutClick()
            },
            onDismiss = { confirmAction = null },
        )
        SettingsConfirmAction.DeleteAccount -> HvalaConfirmDialog(
            title = "Удалить аккаунт?",
            message = "Это действие необратимо. Все ваши данные и объявления будут удалены.",
            confirmText = "Удалить",
            isDestructive = true,
            onConfirm = {
                confirmAction = null
                onDeleteAccountClick()
            },
            onDismiss = { confirmAction = null },
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
            avatarUrl = avatarUrl,
            onEditAvatarClick = onEditAvatarClick,
        )

        PrimaryButton(
            text = "Редактировать информацию",
            onClick = onEditProfileClick,
            modifier = Modifier.fillMaxWidth(),
            textStyle = ButtonLarge,
        )

        SettingsMenuCard(
            items = generalMenuItems,
            onItemClick = onMenuItemClick,
        )

        SettingsMenuCard(
            items = listOf(deleteAccountItem),
            onItemClick = { confirmAction = SettingsConfirmAction.DeleteAccount },
        )

        SettingsMenuCard(
            items = listOf(logoutItem),
            onItemClick = { confirmAction = SettingsConfirmAction.Logout },
        )
    }
}

@Composable
@Preview
private fun SettingsScreenPreview() {
    HvalaTheme {
        SettingsScreen(
            fullName = "Vadim",
            email = "vadim.lushina@gmail.com",
        )
    }
}
