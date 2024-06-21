package com.glebkrep.simplebudget.core.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import com.glebkrep.simplebudget.core.ui.components.ComposeUtils.performClickVibration
import com.glebkrep.simplebudget.core.ui.components.ComposeUtils.playClickSound
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds


@SuppressLint("ComposableModifierFactory")
@Composable
fun Modifier.clickableWithRipple(
    onClick: () -> Unit,
    soundAndVibration: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    color: Color = Color.White,
) = composed {
    val haptic = LocalHapticFeedback.current
    val view = LocalView.current
    this.then(
        clickable(
            interactionSource = interactionSource,
            indication = rememberRipple(color = color),
        ) {
            if (soundAndVibration) {
                haptic.performClickVibration()
                view.playClickSound()
            }
            onClick.invoke()
        }
    )
}

private fun Modifier.onTouchHeld(
    initialDelay: Duration = 500.milliseconds,
    interactionSource: MutableInteractionSource,
    onTouchHeld: () -> Unit,
    tickDecreaseDouble: Double = 0.5,
    minDelay: Long = 105L
) = composed {
    val currentClickListener by rememberUpdatedState(onTouchHeld)

    this.then(pointerInput(interactionSource) {
        coroutineScope {
            awaitEachGesture {
                val down = awaitFirstDown(requireUnconsumed = false)
                val job = launch {
                    var currentDelayMillis = initialDelay.inWholeMilliseconds
                    while (down.pressed) {
                        currentClickListener.invoke()
                        delay(currentDelayMillis)
                        val nextMillis =
                            currentDelayMillis - (currentDelayMillis * tickDecreaseDouble)
                        currentDelayMillis = nextMillis.toLong().coerceAtLeast(minDelay)
                    }
                }
                waitForUpOrCancellation()
                job.cancel()
            }
        }

    })
}


fun Modifier.onTouchHeldWithRipple(
    onTouchHeld: () -> Unit,
    soundAndVibration: Boolean = true,
    initialDelay: Duration = 500.milliseconds,
    color: Color = Color.White
) = composed {
    val haptic = LocalHapticFeedback.current
    val view = LocalView.current
    val interactionSource = remember {
        MutableInteractionSource()
    }
    this.then(
        clickableWithRipple(
            onClick = { },
            interactionSource = interactionSource,
            color = color,
            soundAndVibration = false
        ).then(
            onTouchHeld(onTouchHeld = {
                if (soundAndVibration) {
                    haptic.performClickVibration()
                    view.playClickSound()
                }
                onTouchHeld.invoke()
            }, interactionSource = interactionSource, initialDelay = initialDelay)
        )
    )
}


fun Modifier.clickableWithVibrationAndSound(
    onClick: () -> Unit
) = composed {
    val haptic = LocalHapticFeedback.current
    val view = LocalView.current
    this.then(
        clickable {
            haptic.performClickVibration()
            view.playClickSound()
            onClick.invoke()
        }
    )
}

fun Modifier.clickableWithVibrationAndSound(
    interactionSource: MutableInteractionSource,
    onClick: () -> Unit,
) = composed {
    val haptic = LocalHapticFeedback.current
    val view = LocalView.current
    this.then(
        clickable(
            interactionSource = interactionSource,
            null,
            onClick = {
                haptic.performClickVibration()
                view.playClickSound()
                onClick.invoke()
            }
        )
    )
}
