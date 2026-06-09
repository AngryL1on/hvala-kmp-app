package tech.appard.hvala.shared.feature.settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import tech.appard.hvala.shared.core.i18n.appStrings
import tech.appard.hvala.shared.core.ui.components.logo.HvalaAppIconPlaceholder
import tech.appard.hvala.shared.core.ui.components.logo.HvalaAppIconVariant
import tech.appard.hvala.shared.core.ui.theme.BodyMedium
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.InputText
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.PrimaryMain
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain
import tech.appard.hvala.shared.core.ui.theme.TitleMedium
import tech.appard.hvala.shared.core.ui.theme.White

@Composable
fun SettingsProfileHeader(
    fullName: String,
    email: String,
    modifier: Modifier = Modifier,
    avatarUrl: String? = null,
    onEditAvatarClick: () -> Unit = {},
) {
    val dimensions = LocalDimensions.current
    val strings = appStrings().common

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.padding(bottom = dimensions.verticalMedium),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(dimensions.settingsAvatarSize)
                    .clip(CircleShape)
                    .border(
                        width = dimensions.avatarBorderWidth,
                        color = PrimaryMain,
                        shape = CircleShape,
                    )
                    .background(White),
                contentAlignment = Alignment.Center,
            ) {
                if (avatarUrl.isNullOrBlank()) {
                    HvalaAppIconPlaceholder(
                        modifier = Modifier.fillMaxSize(),
                        variant = HvalaAppIconVariant.Full,
                        showBackground = false,
                        iconScale = 0.55f,
                    )
                }
                // TODO: show loaded photo when avatarUrl is provided and image loading is integrated
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 4.dp, y = 4.dp)
                    .size(dimensions.settingsEditBadgeSize)
                    .clip(CircleShape)
                    .background(SecondaryMain)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onEditAvatarClick,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = strings.editAvatar,
                    tint = White,
                    modifier = Modifier.size(dimensions.iconDefaultSize - 4.dp),
                )
            }
        }

        Text(
            text = fullName,
            style = TitleMedium.copy(color = InputText),
            textAlign = TextAlign.Center,
        )
        Text(
            text = email,
            style = BodyMedium.copy(color = GrayText),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = dimensions.verticalXXSmall),
        )
    }
}
