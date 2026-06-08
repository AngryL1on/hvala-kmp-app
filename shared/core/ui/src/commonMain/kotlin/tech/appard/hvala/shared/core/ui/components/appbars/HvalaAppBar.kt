package tech.appard.hvala.shared.core.ui.components.appbars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import tech.appard.hvala.shared.core.ui.theme.PrimaryMain
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain
import tech.appard.hvala.shared.core.ui.theme.TitleLarge
import tech.appard.hvala.shared.core.ui.theme.White

@Immutable
data class HvalaAppBarState(
    val title: String? = null,
    val showBackButton: Boolean = false,
    val centerTitle: Boolean = false,
    val showSettingsButton: Boolean = false,
)

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HvalaAppBar(
    state: HvalaAppBarState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit = {},
) {
    val colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
    val actions: @Composable androidx.compose.foundation.layout.RowScope.() -> Unit = {
        if (state.showSettingsButton) {
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = PrimaryMain,
                )
            }
        }
    }
    val navigationIcon: @Composable () -> Unit = {
        if (state.showBackButton) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = SecondaryMain,
                )
            }
        }
    }
    val title: @Composable () -> Unit = {
        state.title?.let { title ->
            Text(
                text = title,
                style = TitleLarge.copy(color = SecondaryMain),
            )
        }
    }

    if (state.centerTitle) {
        CenterAlignedTopAppBar(
            modifier = modifier,
            title = title,
            navigationIcon = navigationIcon,
            actions = actions,
            colors = colors,
        )
    } else {
        TopAppBar(
            modifier = modifier,
            title = title,
            navigationIcon = navigationIcon,
            actions = actions,
            colors = colors,
        )
    }
}

@Composable
fun rememberHvalaAppBarState(
    title: String? = null,
    showBackButton: Boolean = false,
    centerTitle: Boolean = false,
    showSettingsButton: Boolean = false,
): HvalaAppBarState = remember(title, showBackButton, centerTitle, showSettingsButton) {
    HvalaAppBarState(
        title = title,
        showBackButton = showBackButton,
        centerTitle = centerTitle,
        showSettingsButton = showSettingsButton,
    )
}
