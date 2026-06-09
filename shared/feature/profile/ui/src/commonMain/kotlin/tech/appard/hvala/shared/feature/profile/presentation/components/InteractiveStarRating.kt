package tech.appard.hvala.shared.feature.profile.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.PrimaryMain
import tech.appard.hvala.shared.core.ui.theme.StarInactive

@Composable
fun InteractiveStarRating(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    starSize: Dp? = null,
    maxStars: Int = 5,
) {
    val dimensions = LocalDimensions.current
    val iconSize = starSize ?: dimensions.iconDefaultSize

    Row(modifier = modifier) {
        repeat(maxStars) { index ->
            val starValue = index + 1
            val isFilled = starValue <= rating
            Icon(
                imageVector = if (isFilled) Icons.Filled.Star else Icons.Outlined.StarBorder,
                contentDescription = null,
                tint = if (isFilled) PrimaryMain else StarInactive,
                modifier = Modifier
                    .size(iconSize)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onRatingChange(starValue) },
                    ),
            )
        }
    }
}
