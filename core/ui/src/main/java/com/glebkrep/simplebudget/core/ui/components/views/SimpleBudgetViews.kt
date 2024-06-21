package com.glebkrep.simplebudget.core.ui.components.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.glebkrep.simplebudget.core.ui.R
import com.glebkrep.simplebudget.core.ui.components.ComposeUtils.performClickVibration
import com.glebkrep.simplebudget.core.ui.components.ComposeUtils.playClickSound
import com.glebkrep.simplebudget.core.ui.components.clickableWithVibrationAndSound
import com.glebkrep.simplebudget.core.ui.theme.DefaultPadding

object SimpleBudgetViews {

    @Composable
    fun SimpleBudgetText(
        text: String,
        modifier: Modifier = Modifier,
        textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
        maxLines: Int = 1,
        disablePadding: Boolean = false,
        strikethrough: Boolean = false,
        color: Color = Color.Unspecified,
        textAlign: TextAlign? = null,
    ) {
        var newStyle = textStyle
        if (disablePadding) {
            newStyle = newStyle.copy(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            )
        }

        if (strikethrough) {
            newStyle = newStyle.copy(
                textDecoration = TextDecoration.LineThrough
            )
        }

        Text(
            text = text,
            maxLines = maxLines,
            color = color,
            textAlign = textAlign,
            style = newStyle,
            modifier = modifier
        )
    }

    @Composable
    fun SimpleBudgetSwitch(
        text: String,
        isChecked: Boolean,
        textColor: Color = Color.Unspecified,
        textStyle: TextStyle = MaterialTheme.typography.titleLarge,
        onCheckedChange: (Boolean) -> Unit,
        @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.fillMaxWidth(),
    ) {
        val source = remember { MutableInteractionSource() }
        val state = remember { mutableStateOf(isChecked) }
        Row(
            modifier = modifier
                .padding(vertical = DefaultPadding.DEFAULT_PADDING)
                .clickableWithVibrationAndSound(
                    interactionSource = source,
                    onClick = {
                        state.value = !state.value
                        onCheckedChange.invoke(state.value)
                    }
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SimpleBudgetText(text = text, color = textColor, textStyle = textStyle)
            androidx.compose.material3.Switch(
                checked = state.value,
                interactionSource = source, onCheckedChange = null
            )
        }
    }

    @Composable
    fun SimpleBudgetCheckbox(
        isChecked: Boolean,
        modifier: Modifier = Modifier,
        soundAndVibration: Boolean = true,
        onCheckedChange: (Boolean) -> Unit,
    ) {
        val hapticsFeedback = LocalHapticFeedback.current
        val view = LocalView.current
        Checkbox(checked = isChecked, onCheckedChange = {
            if (soundAndVibration) {
                hapticsFeedback.performClickVibration()
                view.playClickSound()
            }
            onCheckedChange.invoke(it)
        }, modifier = modifier)
    }

    @Composable
    fun SimpleBudgetIconButton(
        imageVector: ImageVector,
        contentDescription: String,
        modifier: Modifier = Modifier,
        tint: Color = Color.Unspecified,
        vibrate: Boolean = true,
        onClick: () -> (Unit),
    ) {
        val newModifier = if (vibrate) {
            modifier.clickableWithVibrationAndSound { onClick.invoke() }
        } else modifier.clickable {
            onClick.invoke()
        }
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint,
            modifier = newModifier
        )
    }

    @Composable
    fun SimpleBudgetTextButton(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        textColor: Color = Color.Unspecified,
        vibrate: Boolean = true
    ) {
        val newModifier = if (vibrate) {
            modifier.clickableWithVibrationAndSound { onClick.invoke() }
        } else modifier.clickable {
            onClick.invoke()
        }
        SimpleBudgetText(
            text = text,
            modifier = newModifier,
            color = textColor
        )
    }

    @Composable
    fun SimpleBudgetHeaderWithBackArrow(
        headerText: String,
        modifier: Modifier = Modifier,
        onBackClick: (() -> Unit)? = null,
        textColor: Color = MaterialTheme.colorScheme.primary,
        backArrowColor: Color = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        ConstraintLayout(modifier = modifier) {
            val (backBtn, header) = createRefs()
            if (onBackClick != null) {
                SimpleBudgetIconButton(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(R.string.core_ui_back),
                    modifier = Modifier
                        .constrainAs(backBtn) {
                            start.linkTo(parent.start, DefaultPadding.DEFAULT_PADDING)
                            top.linkTo(header.top)
                            bottom.linkTo(header.bottom)
                            width = Dimension.fillToConstraints
                        },
                    tint = backArrowColor,
                    onClick = {
                        onBackClick.invoke()
                    }
                )
            }

            SimpleBudgetText(
                text = headerText,
                textStyle = MaterialTheme.typography.headlineSmall,
                color = textColor,
                modifier = Modifier
                    .constrainAs(header) {
                        top.linkTo(parent.top, DefaultPadding.LARGE_PADDING)
                        start.linkTo(parent.start, DefaultPadding.DEFAULT_PADDING)
                        end.linkTo(parent.end, DefaultPadding.DEFAULT_PADDING)
                    }
            )
        }
    }

    @Composable
    fun SimpleBudgetCardButton(
        text: String,
        cardBackground: Color,
        textColor: Color,
        modifier: Modifier = Modifier,
        onClick: () -> (Unit)
    ) {
        Column(
            modifier
                .fillMaxWidth()
                .padding(DefaultPadding.BIGGER_PADDING),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(DefaultPadding.BIGGER_PADDING),
                colors = CardDefaults.cardColors().copy(
                    containerColor = cardBackground
                )
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = DefaultPadding.LARGE_PADDING)
                        .clickable { onClick.invoke() },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    SimpleBudgetText(
                        text = text, color = textColor,
                        maxLines = 4,
                        textStyle = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun SimpleBudgetTextPreview() {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        SimpleBudgetViews.SimpleBudgetText(text = "Sample Text")
    }
}

@Preview
@Composable
fun SimpleBudgetSwitchPreview() {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        SimpleBudgetViews.SimpleBudgetSwitch(
            text = "Sample Switch Text",
            isChecked = true,
            onCheckedChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DefaultPadding.DEFAULT_PADDING)
        )
    }
}


@Composable
@Preview
fun SimpleBudgetCheckBoxPreview() {
    Column(Modifier.fillMaxWidth()) {
        SimpleBudgetViews.SimpleBudgetCheckbox(isChecked = true) {

        }
    }
}


@Composable
@Preview
fun SimpleBudgetIconButtonPreview() {
    Column(Modifier.fillMaxWidth()) {
        SimpleBudgetViews.SimpleBudgetIconButton(
            imageVector = Icons.Default.Settings,
            contentDescription = "TEST",
            onClick = {}
        )
    }
}

@Composable
@Preview
fun SimpleBudgetTextButtonPreview() {
    Column(Modifier.fillMaxWidth()) {
        SimpleBudgetViews.SimpleBudgetTextButton(
            text = "Test text button",
            onClick = {}
        )
    }
}

@Composable
@Preview
fun SimpleBudgetHeaderPreview() {
    Column(Modifier.fillMaxWidth()) {
        SimpleBudgetViews.SimpleBudgetHeaderWithBackArrow(
            headerText = "HEADER",
            onBackClick = {},
            modifier = Modifier.fillMaxWidth()
        )
        SimpleBudgetViews.SimpleBudgetHeaderWithBackArrow(
            headerText = "HEADER",
            onBackClick = null,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
@Preview
fun SimpleBudgetCardButtonPreview() {
    Column(Modifier.fillMaxWidth()) {
        SimpleBudgetViews.SimpleBudgetCardButton(
            text = "TEST TEST\n multi line",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            textColor = MaterialTheme.colorScheme.onPrimary,
            cardBackground = MaterialTheme.colorScheme.primary
        )
    }
}
