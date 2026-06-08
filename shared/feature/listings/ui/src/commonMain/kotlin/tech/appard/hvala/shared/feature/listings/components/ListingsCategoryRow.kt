package tech.appard.hvala.shared.feature.listings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import tech.appard.hvala.shared.feature.listings.ui.model.UIListingCategory
import tech.appard.hvala.shared.core.ui.theme.BodyMedium
import tech.appard.hvala.shared.core.ui.theme.InputText
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.PrimaryMain
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain
import tech.appard.hvala.shared.core.ui.theme.White

@Composable
fun ListingsCategoryRow(
    categories: List<UIListingCategory>,
    selectedCategoryId: String?,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val chipShape = RoundedCornerShape(dimensions.defaultCornerRadius)
    val listState = rememberLazyListState()
    val orderedCategories = remember(categories, selectedCategoryId) {
        selectedCategoryId?.let { selectedId ->
            categories.find { it.id == selectedId }?.let { selected ->
                listOf(selected) + categories.filter { it.id != selectedId }
            }
        } ?: categories
    }

    LaunchedEffect(selectedCategoryId) {
        if (selectedCategoryId != null) {
            listState.animateScrollToItem(0)
        }
    }

    LazyRow(
        modifier = modifier,
        state = listState,
        contentPadding = PaddingValues(horizontal = dimensions.horizontalMedium),
        horizontalArrangement = Arrangement.spacedBy(dimensions.horizontalXSmall),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items(orderedCategories, key = { it.id }) { category ->
            val isSelected = category.id == selectedCategoryId
            val chipBorderColor = if (isSelected) SecondaryMain else PrimaryMain
            val interactionSource = remember(category.id) { MutableInteractionSource() }

            Box(
                modifier = Modifier
                    .height(dimensions.categoryChipHeight)
                    .clip(chipShape)
                    .border(
                        width = 1.dp,
                        color = chipBorderColor,
                        shape = chipShape,
                    )
                    .background(if (isSelected) SecondaryMain else White)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { onCategorySelected(category.id) },
                    )
                    .padding(horizontal = dimensions.horizontalMedium),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = category.title,
                    style = BodyMedium.copy(
                        color = if (isSelected) White else InputText,
                    ),
                )
            }
        }
    }
}
