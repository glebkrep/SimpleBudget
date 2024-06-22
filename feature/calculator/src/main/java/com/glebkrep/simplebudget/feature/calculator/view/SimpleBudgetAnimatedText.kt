package com.glebkrep.simplebudget.feature.calculator.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.glebkrep.simplebudget.core.ui.components.views.SimpleBudgetViews

@Composable
fun SimpleBudgetAnimatedText(
    doubleValue: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    color: Color = Color.Unspecified,
) {
    Row(modifier = modifier) {
        doubleValue.forEach {
            SimpleBudgetAnimatedChar(
                char = it,
                textColor = color,
                textStyle = textStyle
            )
        }
    }
}

@Composable
private fun SimpleBudgetAnimatedChar(char: Char, textColor: Color, textStyle: TextStyle) {
    val animationTime = 500L

    val intAnimation by animateIntAsState(
        targetValue = char.toIntWithDot(),
        animationSpec = tween(durationMillis = animationTime.toInt(), easing = LinearEasing),
        label = "Char Value Animation",
    )

    AnimatedContent(targetState = intAnimation, transitionSpec = {
        if (targetState < initialState) {
            slideInVertically { it } togetherWith slideOutVertically { -it }
        } else {
            slideInVertically { -it } togetherWith slideOutVertically { it }
        }
    }, label = "Char Text View Animation") { newChar ->
        SimpleBudgetViews.SimpleBudgetText(
            text = newChar.toCharWithDot().toString(),
            textStyle = textStyle,
            color = textColor
        )
    }
}

private const val DOT_CODE = 10

fun Char.toIntWithDot() = this.digitToIntOrNull() ?: DOT_CODE

fun Int.toCharWithDot(): Char {
    return if (this == DOT_CODE) {
        '.'
    } else {
        this.toString().toCharArray().first()
    }
}
