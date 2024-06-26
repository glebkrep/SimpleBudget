package com.glebkrep.simplebudget.feature.calculator.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.glebkrep.simplebudget.core.ui.components.views.SimpleBudgetViews
import com.glebkrep.simplebudget.core.ui.theme.DefaultPadding
import com.glebkrep.simplebudget.core.ui.theme.SimpleBudgetTheme
import com.glebkrep.simplebudget.feature.calculator.R
import com.glebkrep.simplebudget.feature.calculator.logic.CalculatorEvent

@Composable
internal fun CalculatorChooseIncreasePage(
    budgetLeft: String,
    daysLeft: String,
    dailyFromTo: Pair<String, String>,
    todayFromTo: Pair<String, String>,
    onEvent: (CalculatorEvent) -> (Unit)
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("calculator:increase")
    ) {
        SimpleBudgetViews.SimpleBudgetHeaderWithBackArrow(
            headerText = stringResource(R.string.feature_calculator_header_increase),
            modifier = Modifier.fillMaxWidth()
        )
        Column(
            Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = DefaultPadding.BIG,
                    vertical = DefaultPadding.LARGE
                ), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SimpleBudgetViews.SimpleBudgetText(
                text = stringResource(R.string.feature_calculator_total_left, budgetLeft),
                textStyle = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.testTag("calculator:increase:total_left")
            )
            SimpleBudgetViews.SimpleBudgetText(
                text = stringResource(R.string.feature_calculator_days_left, daysLeft),
                textStyle = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.testTag("calculator:increase:days_left")
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize().padding(horizontal = DefaultPadding.LARGE)
        ) {

            SimpleBudgetViews.SimpleBudgetCardButton(
                text = "${stringResource(R.string.feature_calculator_increase_daily)}\n${
                    stringResource(
                        R.string.feature_calculator_from_to,
                        dailyFromTo.first,
                        dailyFromTo.second
                    )
                }",
                cardBackground = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onPrimary,
                onClick = {
                    onEvent.invoke(CalculatorEvent.SelectIncreaseDaily)
                }
            )

            SimpleBudgetViews.SimpleBudgetCardButton(
                text = "${stringResource(R.string.feature_calculator_increase_today)}\n${
                    stringResource(
                        R.string.feature_calculator_from_to,
                        todayFromTo.first,
                        todayFromTo.second
                    )
                }",
                cardBackground = MaterialTheme.colorScheme.secondary,
                textColor = MaterialTheme.colorScheme.onPrimary,
                onClick = {
                    onEvent.invoke(CalculatorEvent.SelectIncreaseToday)
                }
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun CalculatorChooseIncreaseScreenPreview() {
    SimpleBudgetTheme {
        Column(Modifier.fillMaxSize()) {
            CalculatorChooseIncreasePage(
                dailyFromTo = Pair("12334.54", "12523.4"),
                todayFromTo = Pair("12334.54", "125223.4"),
                budgetLeft = "10000",
                daysLeft = "5", onEvent = {
//                    Logger.log(it.toString())
                }
            )
        }

    }
}
