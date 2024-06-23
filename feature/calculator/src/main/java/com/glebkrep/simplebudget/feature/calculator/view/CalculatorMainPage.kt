package com.glebkrep.simplebudget.feature.calculator.view

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.glebkrep.simplebudget.core.data.data.models.BudgetUiState
import com.glebkrep.simplebudget.core.data.data.models.CalculatorEvent
import com.glebkrep.simplebudget.core.ui.components.views.CalculatorView
import com.glebkrep.simplebudget.core.ui.components.views.SimpleBudgetViews
import com.glebkrep.simplebudget.core.ui.theme.DefaultColors
import com.glebkrep.simplebudget.core.ui.theme.DefaultPadding
import com.glebkrep.simplebudget.core.ui.theme.DefaultValues
import com.glebkrep.simplebudget.core.ui.theme.SimpleBudgetTheme
import com.glebkrep.simplebudget.feature.calculator.R
import com.glebkrep.simplebudget.feature.calculator.logic.DiffAnimationState
import com.glebkrep.simplebudget.model.UiRecentTransaction
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.android.awaitFrame

@Composable
internal fun CalculatorMainPage(
    budgetUiState: BudgetUiState,
    diffAnimationState: DiffAnimationState?,
    onEvent: (CalculatorEvent) -> (Unit)
) {
    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()
    var lastDrawnListSize by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(budgetUiState.recentTransactions) {
        awaitFrame()
        val newListSize = budgetUiState.recentTransactions.size
        if (lastDrawnListSize > newListSize) {
            lastDrawnListSize = newListSize
            return@LaunchedEffect
        }
        lastDrawnListSize = newListSize
        listState.animateScrollToItem(0)
    }


    ConstraintLayout(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }) {
        val (todayBlock, calculatorButtons, recentTransactions) = createRefs()
        val bottomLine = createGuidelineFromBottom(0.4f)

        CalculatorScreenHeaderView(
            budgetData = budgetUiState,
            diffAnimationState = diffAnimationState,
            onEvent = { onEvent.invoke(it) },
            modifier = Modifier.constrainAs(todayBlock) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
            },
        )

        LazyColumn(
            Modifier
                .constrainAs(recentTransactions) {
                    top.linkTo(todayBlock.bottom)
                    bottom.linkTo(bottomLine)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
            reverseLayout = true,
            state = listState
        ) {
            items(
                budgetUiState.recentTransactions,
                { listItem: UiRecentTransaction -> listItem.id }) { item ->
                RecentTransactionView(recentTransaction = item, index = item.id) {
                    onEvent.invoke(it)
                }
            }
        }

        CalculatorView(
            calculatorInput = budgetUiState.currentInput,
            isEnterAllowed = !((budgetUiState.oldMoneyLeft <= "0" || budgetUiState.oldMoneyLeft.startsWith(
                "-"
            )) && !budgetUiState.currentInput.contains(
                "+"
            )),
            areCommentsAllowed = budgetUiState.areCommentsEnabled,
            isSignChangeAllowed = true,
            onCommitTransaction = { calculatorInput: String, comment: String? ->
                onEvent.invoke(
                    CalculatorEvent.CommitTransaction(input = calculatorInput, comment = comment)
                )
            },
            onKeyTap = { onEvent.invoke(CalculatorEvent.KeyTap(it)) },
            modifier = Modifier.constrainAs(calculatorButtons) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                top.linkTo(bottomLine)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecentTransactionView(
    recentTransaction: UiRecentTransaction,
    index: Int,
    onEvent: (CalculatorEvent) -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            it == SwipeToDismissBoxValue.EndToStart
        }
    )

    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onEvent.invoke(CalculatorEvent.DeleteRecent(index))
        }
    }
    //todo rework list item size not to use constant
    val cardModifier = Modifier
        .fillMaxWidth()
        .padding(
            horizontal = DefaultPadding.BIG,
            vertical = DefaultPadding.BIG
        )
        .height(DefaultValues.LIST_ITEM_SIZE)


    SwipeToDismissBox(state = dismissState,
        enableDismissFromEndToStart = true,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Card(modifier = cardModifier.padding(DefaultPadding.SMALL)) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(DefaultColors.BadRed)
                        .padding(
                            horizontal = DefaultPadding.LARGE,
                            vertical = DefaultPadding.BIG
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(modifier = Modifier)
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = stringResource(R.string.feature_calculator_delete)
                    )
                }
            }
        }, content = {
            Card(
                modifier = cardModifier,
                colors = CardDefaults.cardColors().copy(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
            ) {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal = DefaultPadding.LARGE,
                            vertical = DefaultPadding.SMALL
                        )
                ) {
                    val (commentAndDate, value) = createRefs()

//                Row(
//                    Modifier
//                        .fillMaxSize()
//                        .padding(
//                            horizontal = DefaultPadding.BIG_PADDING,
//                            vertical = DefaultPadding.SMALL_PADDING
//                        ),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
                    val numberColor =
                        if (recentTransaction.isInBillingPeriod) {
                            if (recentTransaction.isPlusOperation) {
                                DefaultColors.GoodGreen
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            }
                        } else {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        }

                    val commentColor = if (recentTransaction.isInBillingPeriod) {
                        MaterialTheme.colorScheme.onBackground
                    } else {
                        DefaultColors.LightGrayText
                    }
                    Column(modifier = Modifier.constrainAs(commentAndDate) {
                        start.linkTo(parent.start)
                        end.linkTo(value.start, margin = DefaultPadding.LARGE)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    }) {
                        SimpleBudgetViews.SimpleBudgetText(
                            text = recentTransaction.comment.orEmpty(),
                            color = commentColor,
                            textStyle = MaterialTheme.typography.titleLarge
                        )
                        SimpleBudgetViews.SimpleBudgetText(
                            text = recentTransaction.prettyDate,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textStyle = MaterialTheme.typography.titleLarge

                        )
                    }
                    SimpleBudgetViews.SimpleBudgetText(
                        text = recentTransaction.prettyValue,
                        color = numberColor,
                        textStyle = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.constrainAs(value) {
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.wrapContent
                        },
                        textAlign = TextAlign.End
                    )
                }
            }
        })
}


@Preview(showBackground = true)
@Composable
private fun CalculatorScreenPreview() {
    SimpleBudgetTheme {
        Column(Modifier.fillMaxSize()) {
            CalculatorMainPage(
                budgetUiState = BudgetUiState(
                    currentInput = "123",
                    daysUntilBilling = "123",
                    oldTodayBudget = "10",
                    newTodayBudget = "5",
                    oldDailyBudget = "50",
                    newDailyBudget = "10",
                    oldMoneyLeft = "200",
                    newMoneyLeft = "100",
                    recentTransactions = persistentListOf(
                        UiRecentTransaction(
                            id = 1,
                            "11 000",
                            "20:25 April, 10 2023",
                            comment = "helloe very long commnet"
                        ),
                        UiRecentTransaction(
                            id = 2,
                            "5000",
                            "20:25 May, 10 2024"
                        ),
                        UiRecentTransaction(
                            id = 3,
                            "31.2",
                            "14:25 September, 10 2023",
                            comment = "helloe 22 22 2"
                        ),
                        UiRecentTransaction(
                            id = 4,
                            "55",
                            "03:25 April, 10 2023"
                        ),
                        UiRecentTransaction(
                            id = 5,
                            "20 000",
                            "20:12 April, 10 2024"
                        ),
                    ),
                    areCommentsEnabled = true
                ),
                diffAnimationState = null,
                onEvent = {
                }
            )
        }

    }
}
