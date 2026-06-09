package tech.appard.hvala.shared.feature.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import tech.appard.hvala.shared.core.i18n.ReviewStrings
import tech.appard.hvala.shared.core.i18n.appStrings
import tech.appard.hvala.shared.core.ui.components.fields.HvalaSelectField
import tech.appard.hvala.shared.core.ui.components.fields.SelectOption
import tech.appard.hvala.shared.core.ui.theme.FieldInput
import tech.appard.hvala.shared.core.ui.theme.CardBorder
import tech.appard.hvala.shared.core.ui.theme.FieldTitle
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.InputText
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.PrimaryMain
import tech.appard.hvala.shared.core.ui.theme.White
import tech.appard.hvala.shared.feature.profile.presentation.model.ReviewSortOrder

@Composable
fun ReviewListControls(
    selectedSortOrder: ReviewSortOrder,
    onSortOrderSelected: (ReviewSortOrder) -> Unit,
    onlyWithPhotos: Boolean,
    onOnlyWithPhotosToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val strings = appStrings().reviews
    val sortOptions = ReviewSortOrder.entries.map { order ->
        SelectOption(id = order.name, label = strings.sortOrderTitle(order))
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.horizontalMedium),
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalXXSmall),
    ) {
        Text(
            text = strings.sortLabel,
            style = FieldTitle.copy(color = GrayText),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensions.horizontalXSmall),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HvalaSelectField(
                modifier = Modifier.weight(1f),
                label = "",
                options = sortOptions,
                selectedOptionId = selectedSortOrder.name,
                onOptionSelected = { id ->
                    ReviewSortOrder.entries
                        .firstOrNull { it.name == id }
                        ?.let(onSortOrderSelected)
                },
            )

            ReviewFilterChip(
                label = strings.filterOnlyWithPhotos,
                isSelected = onlyWithPhotos,
                onClick = onOnlyWithPhotosToggle,
            )
        }
    }
}

@Composable
private fun ReviewFilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val shape = RoundedCornerShape(dimensions.defaultCornerRadius)
    val fieldHeight = dimensions.fieldsDefaultHeight

    Box(
        modifier = modifier
            .height(fieldHeight)
            .clip(shape)
            .background(if (isSelected) PrimaryMain.copy(alpha = 0.22f) else White)
            .border(
                width = 1.dp,
                color = if (isSelected) PrimaryMain else CardBorder,
                shape = shape,
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = dimensions.horizontalSmall),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = FieldInput.copy(
                color = if (isSelected) InputText else GrayText,
            ),
            maxLines = 1,
        )
    }
}

internal fun ReviewStrings.sortOrderTitle(order: ReviewSortOrder): String = when (order) {
    ReviewSortOrder.NewestFirst -> sortNewestFirst
    ReviewSortOrder.OldestFirst -> sortOldestFirst
    ReviewSortOrder.PositiveFirst -> sortPositiveFirst
    ReviewSortOrder.NegativeFirst -> sortNegativeFirst
}
