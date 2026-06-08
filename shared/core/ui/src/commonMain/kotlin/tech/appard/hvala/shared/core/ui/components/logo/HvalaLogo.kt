package tech.appard.hvala.shared.core.ui.components.logo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.withTransform
import tech.appard.hvala.shared.core.ui.components.logo.paths.HvalaLogoPaths
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions

@Composable
fun HvalaLogo(
    modifier: Modifier = Modifier,
) {
    val paths = HvalaLogoPaths.parsed

    Canvas(
        modifier = modifier
            .width(LocalDimensions.current.logoSize)
            .aspectRatio(HvalaLogoPaths.VIEWBOX_WIDTH / HvalaLogoPaths.VIEWBOX_HEIGHT),
    ) {
        val scaleX = size.width / HvalaLogoPaths.VIEWBOX_WIDTH
        val scaleY = size.height / HvalaLogoPaths.VIEWBOX_HEIGHT

        withTransform({
            scale(scaleX, scaleY, pivot = Offset.Zero)
        }) {
            drawPath(paths.frame, HvalaLogoPaths.LogoFrameColor)
            drawPath(paths.bottomBar, HvalaLogoPaths.LogoYellowColor)
            drawPath(paths.letterH, HvalaLogoPaths.LogoYellowColor)
            drawPath(paths.letterV, HvalaLogoPaths.LogoYellowColor)
            drawPath(paths.letterALeft, HvalaLogoPaths.LogoYellowColor)
            drawPath(paths.letterL, HvalaLogoPaths.LogoYellowColor)
            drawPath(paths.letterARight, HvalaLogoPaths.LogoYellowColor)
            drawPath(paths.subtitle, HvalaLogoPaths.LogoSubtitleColor)
        }
    }
}
