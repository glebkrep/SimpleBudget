package com.glebkrep.simplebudget.feature.calculator.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.glebkrep.simplebudget.core.data.data.models.BudgetUiState
import com.glebkrep.simplebudget.core.data.data.models.CalculatorEvent
import com.glebkrep.simplebudget.core.ui.components.views.SimpleBudgetViews
import com.glebkrep.simplebudget.core.ui.theme.DefaultColors
import com.glebkrep.simplebudget.core.ui.theme.DefaultPadding
import com.glebkrep.simplebudget.core.ui.theme.SimpleBudgetTheme
import com.glebkrep.simplebudget.feature.calculator.R
import com.glebkrep.simplebudget.feature.calculator.logic.DiffAnimationState
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun CalculatorScreenHeaderView(
    budgetData: BudgetUiState,
    diffAnimationState: DiffAnimationState?,
    onEvent: (CalculatorEvent) -> (Unit),
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = DefaultPadding.DEFAULT,
                start = DefaultPadding.DEFAULT,
                end = DefaultPadding.DEFAULT
            )
    ) {
        ConstraintLayout(modifier = modifier.fillMaxWidth()) {
            val (daysBlock, settingsButton) = createRefs()
            SimpleBudgetViews.SimpleBudgetIconButton(
                imageVector = Icons.Default.Settings,
                contentDescription = stringResource(R.string.feature_calculator_delete1),
                onClick = {
                    onEvent.invoke(CalculatorEvent.OnSettingsClicked)
                },
                tint = DefaultColors.LightGrayText,
                modifier = Modifier.constrainAs(settingsButton) {
                    end.linkTo(parent.end, DefaultPadding.DEFAULT)
                    top.linkTo(parent.top, DefaultPadding.DEFAULT)
                }
            )
            SimpleBudgetViews.SimpleBudgetText(
                text = stringResource(
                    R.string.feature_calculator_days,
                    budgetData.daysUntilBilling
                ),
                color = DefaultColors.LightGrayText,
                modifier = Modifier.constrainAs(daysBlock) {
                    start.linkTo(parent.start, DefaultPadding.DEFAULT)
                    top.linkTo(settingsButton.top)
                    end.linkTo(parent.end, DefaultPadding.DEFAULT)
                    width = Dimension.wrapContent
                },
                textStyle = MaterialTheme.typography.bodyLarge
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(vertical = DefaultPadding.DEFAULT)
        ) {
            BudgetText(
                title = stringResource(R.string.feature_calculator_total),
                newBudget = budgetData.newMoneyLeft,
                oldBudget = budgetData.oldMoneyLeft,
                diff = diffAnimationState?.totalDiff,
                modifier = Modifier.weight(1f),
            )
            BudgetText(
                title = stringResource(R.string.feature_calculator_daily),
                newBudget = budgetData.newDailyBudget,
                oldBudget = budgetData.oldDailyBudget,
                diff = diffAnimationState?.dailyDiff,
                modifier = Modifier.weight(1f),
            )
            BudgetText(
                title = stringResource(R.string.feature_calculator_today),
                newBudget = budgetData.newTodayBudget,
                oldBudget = budgetData.oldTodayBudget,
                diff = diffAnimationState?.todayDiff,
                modifier = Modifier.weight(1f),
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(top = DefaultPadding.DEFAULT),
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
internal fun BudgetText(
    title: String,
    newBudget: String?,
    oldBudget: String,
    diff: String?,
    modifier: Modifier = Modifier
) {

    var bottomValueVisibility by remember {
        mutableStateOf(false)
    }
    var bottomValueText by remember {
        mutableStateOf("")
    }
    var bottomValueColor by remember {
        mutableStateOf(DefaultColors.GoodGreen)
    }

    LaunchedEffect(diff, newBudget) {
        when {
            diff == null && newBudget == null -> {
                bottomValueVisibility = false
            }

            newBudget != null -> {
                bottomValueColor = if (newBudget.toDouble() > oldBudget.toDouble()) {
                    DefaultColors.GoodGreen
                } else DefaultColors.BadRed
                bottomValueText = newBudget
                bottomValueVisibility = true
            }

            diff != null -> {
                bottomValueColor =
                    if (diff.contains("+")) DefaultColors.GoodGreen else DefaultColors.BadRed
                bottomValueText = diff
                bottomValueVisibility = true
            }
        }
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        SimpleBudgetViews.SimpleBudgetText(
            title, color = MaterialTheme.colorScheme.primary,
            textStyle = MaterialTheme.typography.headlineSmall
        )
        SimpleBudgetAnimatedText(
            doubleValue = oldBudget,
            color = MaterialTheme.colorScheme.onBackground,
            textStyle = MaterialTheme.typography.titleLarge
        )
        AnimatedVisibility(visible = bottomValueVisibility, enter = fadeIn(initialAlpha = 1f)) {
            SimpleBudgetViews.SimpleBudgetText(
                bottomValueText,
                color = bottomValueColor,
                textStyle = MaterialTheme.typography.titleLarge
            )
        }
    }
}


@Composable
@Preview
private fun CalculatorScreenHeaderViewPreview() {
    SimpleBudgetTheme {
        CalculatorScreenHeaderView(
            budgetData = BudgetUiState(
                daysUntilBilling = "5",
                newMoneyLeft = "100",
                oldMoneyLeft = "200",
                newDailyBudget = "10",
                oldDailyBudget = "20",
                newTodayBudget = "5",
                oldTodayBudget = "10",
                areCommentsEnabled = false,
                currentInput = "",
                recentTransactions = persistentListOf()
            ),
            diffAnimationState = DiffAnimationState(
                totalDiff = "+100",
                dailyDiff = "-10",
                todayDiff = "+5"
            ),
            onEvent = {},
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}
