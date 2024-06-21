package com.glebkrep.simplebudget.core.ui.components.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.glebkrep.simplebudget.core.ui.R
import com.glebkrep.simplebudget.core.ui.components.ComposeUtils
import com.glebkrep.simplebudget.core.ui.components.clickableWithRipple
import com.glebkrep.simplebudget.core.ui.components.onTouchHeldWithRipple
import com.glebkrep.simplebudget.core.ui.theme.DefaultColors
import com.glebkrep.simplebudget.core.ui.theme.DefaultPadding
import com.glebkrep.simplebudget.core.ui.theme.SimpleBudgetTheme
import com.glebkrep.simplebudget.core.ui.theme.WithLocalRippleAlpha
import com.glebkrep.simplebudget.model.CalculatorButton
import kotlinx.coroutines.android.awaitFrame

@Composable
fun CalculatorView(
    calculatorInput: String,
    isEnterAllowed: Boolean,
    areCommentsAllowed: Boolean,
    isSignChangeAllowed: Boolean,
    onCommitTransaction: (calculatorInput: String, comment: String?) -> (Unit),
    onKeyTap: (CalculatorButton) -> (Unit),
    modifier: Modifier = Modifier
) {
    var isCommentMode by remember {
        mutableStateOf(false)
    }

    var areCommentsEnabled by remember {
        mutableStateOf(false)
    }

    var keyboardOpenedPreviously by remember {
        mutableStateOf(false)
    }
    val isKeyboardOpen by ComposeUtils.keyboardAsState()
    LaunchedEffect(isKeyboardOpen) {
        if (!isKeyboardOpen && keyboardOpenedPreviously) {
            isCommentMode = false
        } else {
            keyboardOpenedPreviously = true
        }
    }

    ConstraintLayout(modifier = modifier.background(MaterialTheme.colorScheme.background)) {
        val (calculatorBlock, inputBlock) = createRefs()
        val inputBlockModifier = Modifier.constrainAs(inputBlock) {
            start.linkTo(parent.start, DefaultPadding.BIGGER_PADDING)
            end.linkTo(parent.end, DefaultPadding.BIGGER_PADDING)
            top.linkTo(parent.top, DefaultPadding.DEFAULT_PADDING)
            width = Dimension.fillToConstraints
            height = Dimension.wrapContent
        }
        val calculatorBlockModifier = Modifier.constrainAs(calculatorBlock) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(inputBlock.bottom)
            bottom.linkTo(parent.bottom)
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
        }
//        when (true) {
        when (isCommentMode && areCommentsEnabled) {
            false -> {
                Row(
                    inputBlockModifier,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    if (areCommentsAllowed) {
                        SimpleBudgetViews.SimpleBudgetCheckbox(
                            isChecked = areCommentsEnabled,
                            onCheckedChange = {
                                areCommentsEnabled = it
                            })
                    }
                    SimpleBudgetViews.SimpleBudgetText(
                        text = calculatorInput,
                        color = if (calculatorInput in listOf(
                                "0",
                                "+0"
                            )
                        ) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onBackground,
                        textStyle = MaterialTheme.typography.displaySmall,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                CalculatorButtonsView(
                    onEnterClicked = {
                        if (areCommentsEnabled) {
                            isCommentMode = true
                        } else {
                            onCommitTransaction.invoke(
                                calculatorInput,
                                null
                            )
                        }
                    },
                    currentInput = calculatorInput,
                    isSignChangeAllowed = isSignChangeAllowed,
                    isEnterAllowed = isEnterAllowed,
                    onButtonClicked = onKeyTap,
                    modifier = calculatorBlockModifier
                )
            }

            true -> {
                var commentText: String by remember {
                    mutableStateOf("")
                }
                val focusRequester = remember { FocusRequester() }
                LaunchedEffect(Unit) {
                    awaitFrame()
                    focusRequester.requestFocus()
                }

                Row(
                    modifier = inputBlockModifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextField(
                        value = commentText,
                        onValueChange = { newVal ->
                            commentText = newVal
                        },
                        singleLine = true,
                        keyboardActions = KeyboardActions(
                            onDone = {
                                onCommitTransaction(calculatorInput, commentText)
                                isCommentMode = false
                            }
                        ),
                        label = {
                            SimpleBudgetViews.SimpleBudgetText(
                                text = stringResource(R.string.core_ui_comment),
                                textStyle = MaterialTheme.typography.labelLarge
                            )
                        },
                        textStyle = MaterialTheme.typography.bodyLarge,
                        colors = TextFieldDefaults.colors().copy(
                            focusedContainerColor = MaterialTheme.colorScheme.background,
                            unfocusedContainerColor = MaterialTheme.colorScheme.background
                        ),
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .weight(3f)
                    )

                    CalculatorButtonView(
                        key = CalculatorButton.SAVE_CHANGE,
                        isButtonEnabled = true,
                        modifier = Modifier
                            .weight(1f)
                            .height(64.dp),
                        onClick = {
                            onCommitTransaction(
                                calculatorInput,
                                commentText
                            )
                            isCommentMode = false
                        }
                    )
                }
                Box(modifier = calculatorBlockModifier)
            }
        }
    }
}

@Composable
private fun CalculatorButtonsView(
    currentInput: String,
    isEnterAllowed: Boolean,
    isSignChangeAllowed: Boolean,
    onEnterClicked: () -> (Unit),
    onButtonClicked: (CalculatorButton) -> (Unit),
    modifier: Modifier = Modifier,
    maxDigitsAllowed: Int = 9,
) {
    val isEnterEnabled = isEnterAllowed && currentInput !in listOf("0", "+0")
    val areDigitsEnabled =
        !(currentInput.contains(CalculatorButton.DOT.text)
                && currentInput.split(CalculatorButton.DOT.text)[1].length > 1)
                && currentInput.length < maxDigitsAllowed
    val isZeroEnabled = currentInput !in listOf("0", "+0") && areDigitsEnabled
    val isCommaEnabled = !currentInput.contains(CalculatorButton.DOT.text)
    val isBackEnabled = currentInput !in listOf("0", "+0")

    val signCalculatorButton = if (isSignChangeAllowed) {
        if (currentInput.contains("+")) {
            CalculatorButton.MINUS
        } else CalculatorButton.PLUS
    } else CalculatorButton.NONE
    val signButton: Pair<CalculatorButton, Boolean> =
        Pair(signCalculatorButton, isSignChangeAllowed)

    val firstColumn = arrayOf(
        Pair(CalculatorButton.ONE, areDigitsEnabled),
        Pair(CalculatorButton.FOUR, areDigitsEnabled),
        Pair(CalculatorButton.SEVEN, areDigitsEnabled),
        signButton
    )

    val secondColum = arrayOf(
        Pair(CalculatorButton.TWO, areDigitsEnabled),
        Pair(CalculatorButton.FIVE, areDigitsEnabled),
        Pair(CalculatorButton.EIGHT, areDigitsEnabled),
        Pair(CalculatorButton.ZERO, isZeroEnabled)
    )

    val thirdColum = arrayOf(
        Pair(CalculatorButton.THREE, areDigitsEnabled),
        Pair(CalculatorButton.SIX, areDigitsEnabled),
        Pair(CalculatorButton.NINE, areDigitsEnabled),
        Pair(CalculatorButton.DOT, isCommaEnabled),
    )

    val backButton = Pair(CalculatorButton.BACK, isBackEnabled)
    val enterButton = Pair(CalculatorButton.ENTER, isEnterEnabled)

    Row(modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            firstColumn.forEach {
                CalculatorButtonView(
                    key = it.first,
                    isButtonEnabled = it.second,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onButtonClicked.invoke(it.first)
                    })
            }
        }
        Column(
            Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            secondColum.forEach {
                CalculatorButtonView(
                    key = it.first,
                    isButtonEnabled = it.second,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onButtonClicked.invoke(it.first)
                    })
            }
        }
        Column(
            Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            thirdColum.forEach {
                CalculatorButtonView(
                    key = it.first,
                    isButtonEnabled = it.second,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onButtonClicked.invoke(it.first)
                    })
            }
        }
        Column(
            Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            CalculatorButtonView(
                key = backButton.first,
                isButtonEnabled = backButton.second,
                modifier = Modifier.weight(1f),
                onClick = {
                    onButtonClicked.invoke(backButton.first)
                })
            CalculatorButtonView(
                key = enterButton.first,
                isButtonEnabled = enterButton.second,
                modifier = Modifier.weight(3f),
                onClick = {
                    onEnterClicked.invoke()
                })
        }
    }
}

@Composable
fun CalculatorButtonView(
    key: CalculatorButton,
    isButtonEnabled: Boolean,
    onClick: () -> (Unit),
    modifier: Modifier = Modifier
) {
    val clickableModifier = if (isButtonEnabled) {
        if (key == CalculatorButton.BACK) {
            Modifier
                .onTouchHeldWithRipple({
                    onClick.invoke()
                })
        } else {
            Modifier
                .clickableWithRipple({
                    onClick.invoke()
                })
        }
    } else Modifier


    val backgroundColor =
        if (isButtonEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
    val textColor =
        if (isButtonEnabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer
    WithLocalRippleAlpha(0.5f) {
        Card(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    horizontal = DefaultPadding.DEFAULT_PADDING,
                    vertical = DefaultPadding.BIGGER_PADDING
                ),
            colors = CardDefaults.cardColors().copy(
                containerColor = backgroundColor
            ),
            shape = ShapeDefaults.Large,
        ) {
            Column(
                clickableModifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SimpleBudgetViews.SimpleBudgetText(
                    text = key.text,
                    color = textColor,
                    textStyle = MaterialTheme.typography.titleLarge
                )
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun CalculatorButtonsPreview() {
    SimpleBudgetTheme {
        CalculatorView(
            calculatorInput = "123",
            isEnterAllowed = false,
            areCommentsAllowed = true,
            isSignChangeAllowed = true,
            onCommitTransaction = { _, _ -> },
            onKeyTap = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
