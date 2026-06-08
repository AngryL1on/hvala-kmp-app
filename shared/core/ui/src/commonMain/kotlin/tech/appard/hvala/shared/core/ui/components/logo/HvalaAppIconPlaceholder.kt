package tech.appard.hvala.shared.core.ui.components.logo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import tech.appard.hvala.shared.core.ui.components.logo.paths.HvalaAppIconPaths
import tech.appard.hvala.shared.core.ui.theme.PrimaryMain
import tech.appard.hvala.shared.core.ui.theme.ScreenBackground
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain
import kotlin.math.min

/**
 * App icon placeholder for avatars and listing images when no photo is available.
 *
 * @param variant [HvalaAppIconVariant.Full] — full launcher icon with wordmark;
 *   [HvalaAppIconVariant.Compact] — frame and letter H only (better for small crops).
 */
@Immutable
enum class HvalaAppIconVariant {
    Full,
    Compact,
}

@Composable
fun HvalaAppIconPlaceholder(
    modifier: Modifier = Modifier,
    variant: HvalaAppIconVariant = HvalaAppIconVariant.Full,
    showBackground: Boolean = true,
    iconScale: Float = 0.55f,
) {
    Box(
        modifier = if (showBackground) {
            modifier.background(ScreenBackground)
        } else {
            modifier
        },
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawAppIcon(variant = variant, iconScale = iconScale)
        }
    }
}

private fun DrawScope.drawAppIcon(
    variant: HvalaAppIconVariant,
    iconScale: Float,
) {
    val paths = HvalaAppIconPaths.parsed
    val viewBoxHeight = when (variant) {
        HvalaAppIconVariant.Full -> HvalaAppIconPaths.VIEWBOX_HEIGHT
        HvalaAppIconVariant.Compact -> HvalaAppIconPaths.COMPACT_VIEWBOX_HEIGHT
    }
    val fitScale = min(
        size.width / HvalaAppIconPaths.VIEWBOX_WIDTH,
        size.height / viewBoxHeight,
    ) * iconScale.coerceIn(0.1f, 1f)
    val offsetX = (size.width - HvalaAppIconPaths.VIEWBOX_WIDTH * fitScale) / 2f
    val offsetY = (size.height - viewBoxHeight * fitScale) / 2f

    withTransform({
        translate(offsetX, offsetY)
        scale(fitScale, fitScale, pivot = Offset.Zero)
    }) {
        drawPath(paths.frame, SecondaryMain)
        drawPath(paths.letterH, PrimaryMain)
        if (variant == HvalaAppIconVariant.Full) {
            drawPath(paths.bottomBar, PrimaryMain)
            drawPath(paths.wordmark, SecondaryMain)
        }
    }
}
