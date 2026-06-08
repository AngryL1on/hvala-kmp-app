package tech.appard.hvala.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.IntOffset

private const val PUSH_DURATION = 320
private const val TAB_DURATION = 240
private const val ROOT_DURATION = 260
private const val MODAL_DURATION = 300
private const val FADE_DURATION = 200
private const val FAB_ORIGIN_Y = 1f
private const val FAB_ORIGIN_X = 0.5f

private val fabTransformOrigin = TransformOrigin(FAB_ORIGIN_X, FAB_ORIGIN_Y)

internal fun screenTransitionSpec(
    transition: NavTransition,
    from: Route,
    to: Route,
): ContentTransform {
    return when (transition) {
        NavTransition.Forward -> forwardTransition()
        NavTransition.Back -> backTransition(from)
        NavTransition.Tab -> tabFadeThroughTransition()
        NavTransition.Root -> rootFadeScaleTransition()
        NavTransition.Modal -> modalSlideUpTransition()
        NavTransition.FabModal -> fabExpandTransition()
    }
}

private fun forwardTransition(): ContentTransform {
    val pushSlideTween = tween<IntOffset>(PUSH_DURATION, easing = FastOutSlowInEasing)
    val fadeTween = tween<Float>(FADE_DURATION, easing = FastOutSlowInEasing)
    val scaleTween = tween<Float>(PUSH_DURATION, easing = FastOutSlowInEasing)

    return slideInHorizontally(pushSlideTween) { fullWidth -> fullWidth } +
        fadeIn(fadeTween) +
        scaleIn(scaleTween, initialScale = 0.98f) togetherWith
        slideOutHorizontally(pushSlideTween) { fullWidth -> -fullWidth / 4 } +
        fadeOut(fadeTween) +
        scaleOut(scaleTween, targetScale = 0.98f)
}

private fun backTransition(from: Route): ContentTransform {
    if (from.isModal()) {
        return when (from) {
            Route.CreateListing -> fabCollapseTransition()
            else -> modalSlideDownTransition()
        }
    }

    val pushSlideTween = tween<IntOffset>(PUSH_DURATION, easing = FastOutSlowInEasing)
    val fadeTween = tween<Float>(FADE_DURATION, easing = FastOutSlowInEasing)
    val scaleTween = tween<Float>(PUSH_DURATION, easing = FastOutSlowInEasing)

    return slideInHorizontally(pushSlideTween) { fullWidth -> -fullWidth / 4 } +
        fadeIn(fadeTween) +
        scaleIn(scaleTween, initialScale = 0.98f) togetherWith
        slideOutHorizontally(pushSlideTween) { fullWidth -> fullWidth } +
        fadeOut(fadeTween) +
        scaleOut(scaleTween, targetScale = 0.98f)
}

private fun tabFadeThroughTransition(): ContentTransform {
    val fadeTween = tween<Float>(TAB_DURATION, easing = FastOutSlowInEasing)
    val enterFadeTween = tween<Float>(
        durationMillis = TAB_DURATION,
        delayMillis = TAB_DURATION / 3,
        easing = FastOutSlowInEasing,
    )
    val scaleTween = tween<Float>(TAB_DURATION, easing = FastOutSlowInEasing)

    return fadeIn(enterFadeTween) +
        scaleIn(scaleTween, initialScale = 0.92f) togetherWith
        fadeOut(fadeTween) +
        scaleOut(scaleTween, targetScale = 0.92f)
}

private fun rootFadeScaleTransition(): ContentTransform {
    val rootTween = tween<Float>(ROOT_DURATION, easing = FastOutSlowInEasing)

    return fadeIn(rootTween) +
        scaleIn(rootTween, initialScale = 0.96f) togetherWith
        fadeOut(rootTween) +
        scaleOut(rootTween, targetScale = 0.96f)
}

private fun modalSlideUpTransition(): ContentTransform {
    val slideTween = tween<IntOffset>(MODAL_DURATION, easing = FastOutSlowInEasing)
    val fadeTween = tween<Float>(FADE_DURATION, easing = FastOutSlowInEasing)
    val scaleTween = tween<Float>(MODAL_DURATION, easing = FastOutSlowInEasing)

    return slideInVertically(slideTween) { fullHeight -> fullHeight } +
        fadeIn(fadeTween) +
        scaleIn(scaleTween, initialScale = 0.98f) togetherWith
        fadeOut(tween(FADE_DURATION, easing = FastOutSlowInEasing))
}

private fun modalSlideDownTransition(): ContentTransform {
    val slideTween = tween<IntOffset>(MODAL_DURATION, easing = FastOutSlowInEasing)
    val fadeTween = tween<Float>(FADE_DURATION, easing = FastOutSlowInEasing)
    val scaleTween = tween<Float>(MODAL_DURATION, easing = FastOutSlowInEasing)

    return fadeIn(fadeTween) +
        scaleIn(scaleTween, initialScale = 0.98f) togetherWith
        slideOutVertically(slideTween) { fullHeight -> fullHeight } +
        fadeOut(fadeTween) +
        scaleOut(scaleTween, targetScale = 0.98f)
}

private fun fabExpandTransition(): ContentTransform {
    val scaleTween = tween<Float>(MODAL_DURATION, easing = FastOutSlowInEasing)
    val fadeTween = tween<Float>(
        durationMillis = MODAL_DURATION,
        delayMillis = 40,
        easing = FastOutSlowInEasing,
    )

    return scaleIn(
        animationSpec = scaleTween,
        initialScale = 0.12f,
        transformOrigin = fabTransformOrigin,
    ) + fadeIn(fadeTween) togetherWith
        fadeOut(tween(FADE_DURATION, easing = FastOutSlowInEasing)) +
        scaleOut(
            animationSpec = tween(MODAL_DURATION, easing = FastOutSlowInEasing),
            targetScale = 0.96f,
            transformOrigin = fabTransformOrigin,
        )
}

private fun fabCollapseTransition(): ContentTransform {
    val scaleTween = tween<Float>(MODAL_DURATION, easing = FastOutSlowInEasing)
    val fadeTween = tween<Float>(FADE_DURATION, easing = FastOutSlowInEasing)

    return fadeIn(fadeTween) togetherWith
        scaleOut(
            animationSpec = scaleTween,
            targetScale = 0.12f,
            transformOrigin = fabTransformOrigin,
        ) + fadeOut(fadeTween)
}
