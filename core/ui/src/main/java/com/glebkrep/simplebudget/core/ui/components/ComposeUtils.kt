package com.glebkrep.simplebudget.core.ui.components

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity

object ComposeUtils {

    @Composable
    fun keyboardAsState(): State<Boolean> {
        val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
        return rememberUpdatedState(isImeVisible)
    }

    private val DEFAULT_HAPTIC_FEEDBACK = HapticFeedbackType.LongPress
    fun HapticFeedback.performClickVibration() {
        this.performHapticFeedback(DEFAULT_HAPTIC_FEEDBACK)
    }

    fun View.playClickSound() {
        this.playSoundEffect(SoundEffectConstants.CLICK)
    }


}


