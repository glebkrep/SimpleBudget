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
import com.glebkrep.simplebudget.core.data.data.models.CalculatorEvent
import com.glebkrep.simplebudget.core.ui.components.views.SimpleBudgetViews
import com.glebkrep.simplebudget.core.ui.theme.DefaultPadding
import com.glebkrep.simplebudget.core.ui.theme.SimpleBudgetTheme
import com.glebkrep.simplebudget.feature.calculator.R

@Composable
fun CalculatorStartNextDayPage(
    budgetLeft: String,
    daysLeft: String,
    onEvent: (CalculatorEvent) -> (Unit)
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("calculator:start_next_day")
    ) {
        SimpleBudgetViews.SimpleBudgetHeaderWithBackArrow(
            headerText = stringResource(R.string.feature_calculator_new_day),
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
                modifier = Modifier.testTag("calculator:start_next_day:total_left")
            )
            SimpleBudgetViews.SimpleBudgetText(
                text = stringResource(R.string.feature_calculator_days_left, daysLeft),
                textStyle = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.testTag("calculator:start_next_day:days_left")
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {

            SimpleBudgetViews.SimpleBudgetCardButton(
                text = stringResource(R.string.feature_calculator_start_next_day),
                cardBackground = MaterialTheme.colorScheme.secondary,
                textColor = MaterialTheme.colorScheme.onPrimary,
                onClick = {
                    onEvent.invoke(CalculatorEvent.SelectStartNextDay)
                }
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorStartNextDayPagePreview() {
    SimpleBudgetTheme {
        Column(Modifier.fillMaxSize()) {
            CalculatorStartNextDayPage(
                budgetLeft = "10000",
                daysLeft = "5", onEvent = {
                }
            )
        }

    }
}
