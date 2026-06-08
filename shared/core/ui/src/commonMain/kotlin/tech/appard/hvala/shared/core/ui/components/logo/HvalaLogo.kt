package tech.appard.hvala.shared.core.ui.components.logo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.Dp
import tech.appard.hvala.shared.core.ui.components.logo.paths.HvalaLogoPaths
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.White

val HvalaLogoAspectRatio: Float
    get() = HvalaLogoPaths.VIEWBOX_WIDTH / HvalaLogoPaths.VIEWBOX_HEIGHT

enum class HvalaLogoVariant {
    Default,
    OnPrimaryBackground,
}

@Composable
fun HvalaLogo(
    modifier: Modifier = Modifier,
    variant: HvalaLogoVariant = HvalaLogoVariant.Default,
    width: Dp = LocalDimensions.current.logoSize,
) {
    val paths = HvalaLogoPaths.parsed
    val colors = when (variant) {
        HvalaLogoVariant.Default -> LogoColors(
            frame = HvalaLogoPaths.LogoFrameColor,
            letters = HvalaLogoPaths.LogoYellowColor,
            bottomBar = HvalaLogoPaths.LogoYellowColor,
            subtitle = HvalaLogoPaths.LogoSubtitleColor,
        )
        HvalaLogoVariant.OnPrimaryBackground -> LogoColors(
            frame = HvalaLogoPaths.LogoFrameColor,
            letters = White,
            bottomBar = White,
            subtitle = HvalaLogoPaths.LogoSubtitleColor,
        )
    }

    Canvas(
        modifier = modifier
            .width(width)
            .aspectRatio(HvalaLogoPaths.VIEWBOX_WIDTH / HvalaLogoPaths.VIEWBOX_HEIGHT),
    ) {
        val scaleX = size.width / HvalaLogoPaths.VIEWBOX_WIDTH
        val scaleY = size.height / HvalaLogoPaths.VIEWBOX_HEIGHT

        withTransform({
            scale(scaleX, scaleY, pivot = Offset.Zero)
        }) {
            drawPath(paths.frame, colors.frame)
            drawPath(paths.bottomBar, colors.bottomBar)
            drawPath(paths.letterH, colors.letters)
            drawPath(paths.letterV, colors.letters)
            drawPath(paths.letterALeft, colors.letters)
            drawPath(paths.letterL, colors.letters)
            drawPath(paths.letterARight, colors.letters)
            drawPath(paths.subtitle, colors.subtitle)
        }
    }
}

private data class LogoColors(
    val frame: Color,
    val letters: Color,
    val bottomBar: Color,
    val subtitle: Color,
)
