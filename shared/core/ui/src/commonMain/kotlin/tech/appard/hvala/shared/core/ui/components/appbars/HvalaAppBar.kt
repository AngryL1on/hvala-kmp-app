package tech.appard.hvala.shared.core.ui.components.appbars

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
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
    val leadingAvatarColorArgb: Long? = null,
)

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HvalaAppBar(
    state: HvalaAppBarState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit = {},
    onTitleClick: (() -> Unit)? = null,
) {
    val colors = TopAppBarDefaults.topAppBarColors(
        containerColor = White,
        scrolledContainerColor = White,
    )
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
    val dimensions = LocalDimensions.current
    val title: @Composable () -> Unit = {
        state.title?.let { title ->
            val avatarColor = state.leadingAvatarColorArgb
            if (avatarColor != null && !state.centerTitle) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensions.horizontalXSmall),
                    modifier = if (onTitleClick != null) {
                        Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onTitleClick,
                        )
                    } else {
                        Modifier
                    },
                ) {
                    Box(
                        modifier = Modifier
                            .size(dimensions.chatHeaderAvatarSize)
                            .clip(CircleShape)
                            .background(Color(avatarColor)),
                    )
                    Text(
                        text = title,
                        style = TitleLarge.copy(color = SecondaryMain),
                    )
                }
            } else {
                Text(
                    text = title,
                    style = TitleLarge.copy(color = SecondaryMain),
                )
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(White),
    ) {
        if (state.centerTitle) {
            CenterAlignedTopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = title,
                navigationIcon = navigationIcon,
                actions = actions,
                colors = colors,
            )
        } else {
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = title,
                navigationIcon = navigationIcon,
                actions = actions,
                colors = colors,
            )
        }
    }
}

@Composable
fun rememberHvalaAppBarState(
    title: String? = null,
    showBackButton: Boolean = false,
    centerTitle: Boolean = false,
    showSettingsButton: Boolean = false,
    leadingAvatarColorArgb: Long? = null,
): HvalaAppBarState = remember(
    title,
    showBackButton,
    centerTitle,
    showSettingsButton,
    leadingAvatarColorArgb,
) {
    HvalaAppBarState(
        title = title,
        showBackButton = showBackButton,
        centerTitle = centerTitle,
        showSettingsButton = showSettingsButton,
        leadingAvatarColorArgb = leadingAvatarColorArgb,
    )
}
