package com.glebkrep.simplebudget.core.ui.theme

import android.app.Activity
import androidx.annotation.FloatRange
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = DefaultColors.Primary,
    secondary = DefaultColors.Secondary,
    background = DefaultColors.Background,
    primaryContainer = DefaultColors.PrimaryContainer,
    onPrimary = DefaultColors.BlackText,
    onPrimaryContainer = DefaultColors.LightGrayText,
    onBackground = DefaultColors.WhiteText,
)

@Composable
fun SimpleBudgetTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        else -> DarkColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Immutable
private class FullAlphaRipple(@FloatRange(0.0, 1.0) val alpha: Float) : RippleTheme {
    @Composable
    override fun defaultColor() = LocalContentColor.current

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(alpha, alpha, alpha, alpha)
}

@Composable
fun WithLocalRippleAlpha(
    @FloatRange(0.0, 1.0) rippleAlpha: Float = 1f,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalRippleTheme provides FullAlphaRipple(rippleAlpha)) {
        content.invoke()
    }
}
