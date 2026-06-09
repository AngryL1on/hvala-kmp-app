package tech.appard.hvala.shared.feature.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import tech.appard.hvala.shared.core.ui.theme.FieldTitle
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.InputText
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain
import tech.appard.hvala.shared.core.ui.theme.TabContainerBackground
import tech.appard.hvala.shared.core.ui.theme.TabSelectedBackground
import tech.appard.hvala.shared.core.i18n.appStrings
import tech.appard.hvala.shared.feature.profile.presentation.model.ProfileListingsTab

@Composable
fun ProfileSegmentedTabs(
    selectedTab: ProfileListingsTab,
    onTabSelected: (ProfileListingsTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val strings = appStrings().profile

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensions.defaultCornerRadius))
            .background(TabContainerBackground)
            .padding(dimensions.horizontalXXSmall),
        horizontalArrangement = Arrangement.spacedBy(dimensions.horizontalXXSmall),
    ) {
        ProfileSegmentedTab(
            text = strings.activeTab,
            isSelected = selectedTab == ProfileListingsTab.Active,
            onClick = { onTabSelected(ProfileListingsTab.Active) },
            modifier = Modifier.weight(1f),
        )
        ProfileSegmentedTab(
            text = strings.archiveTab,
            isSelected = selectedTab == ProfileListingsTab.Archive,
            onClick = { onTabSelected(ProfileListingsTab.Archive) },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun ProfileSegmentedTab(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val backgroundColor = if (isSelected) TabSelectedBackground else Color.Transparent

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(dimensions.defaultCornerRadius))
            .background(backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(vertical = dimensions.verticalSmall),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalXXSmall),
    ) {
        Text(
            text = text,
            style = FieldTitle.copy(
                color = if (isSelected) InputText else GrayText,
            ),
        )
        if (isSelected) {
            Box(
                modifier = Modifier
                    .width(dimensions.tabIndicatorWidth)
                    .height(dimensions.tabIndicatorHeight)
                    .clip(RoundedCornerShape(dimensions.defaultCornerRadius))
                    .background(SecondaryMain),
            )
        } else {
            Box(modifier = Modifier.height(dimensions.tabIndicatorHeight))
        }
    }
}
