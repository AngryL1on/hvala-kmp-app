package tech.appard.hvala.shared.feature.profile.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.PrimaryMain
import tech.appard.hvala.shared.core.ui.theme.StarInactive
import kotlin.math.floor

@Composable
fun StarRatingRow(
    rating: Float,
    modifier: Modifier = Modifier,
    starSize: Dp? = null,
    maxStars: Int = 5,
) {
    val dimensions = LocalDimensions.current
    val iconSize = starSize ?: dimensions.iconDefaultSize
    val filledStars = floor(rating).toInt().coerceIn(0, maxStars)

    Row(modifier = modifier) {
        repeat(maxStars) { index ->
            Icon(
                imageVector = if (index < filledStars) Icons.Filled.Star else Icons.Outlined.StarBorder,
                contentDescription = null,
                tint = if (index < filledStars) PrimaryMain else StarInactive,
                modifier = Modifier.size(iconSize),
            )
        }
    }
}

fun formatRatingValue(rating: Float): String {
    val rounded = (rating * 10).toInt() / 10f
    val whole = rounded.toInt()
    val fraction = ((rounded * 10).toInt() % 10)
    return "$whole.$fraction"
}
