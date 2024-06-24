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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.glebkrep.simplebudget.core.ui.components.views.SimpleBudgetViews
import com.glebkrep.simplebudget.core.ui.theme.DefaultPadding
import com.glebkrep.simplebudget.core.ui.theme.SimpleBudgetTheme
import com.glebkrep.simplebudget.feature.calculator.R
import com.glebkrep.simplebudget.feature.calculator.logic.CalculatorEvent

@Composable
internal fun CalculatorBadBillingDatePage(
    remainingBudget: String,
    onEvent: (CalculatorEvent) -> (Unit),
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("BadBillingDatePage"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val mangedToSaveUp = remainingBudget.toDouble() > 0
        val subHeaderText = if (mangedToSaveUp) {
            stringResource(R.string.feature_calculator_saved_good)
        } else {
            stringResource(R.string.feature_calculator_saved_bad)
        }
        SimpleBudgetViews.SimpleBudgetHeaderWithBackArrow(
            headerText = stringResource(R.string.feature_calculator_header_finish),
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
                text = remainingBudget,
                textStyle = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
            SimpleBudgetViews.SimpleBudgetText(
                text = subHeaderText,
                textStyle = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                maxLines = Int.MAX_VALUE,
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag("calculator:bad-billing:sub-header")
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            SimpleBudgetViews.SimpleBudgetCardButton(
                text = stringResource(R.string.feature_calculator_btn_change_billing_date),
                cardBackground = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = DefaultPadding.EXTRA_LARGE),
                onClick = {
                    onEvent.invoke(CalculatorEvent.OnSettingsClicked)
                },
                textColor = MaterialTheme.colorScheme.onPrimary
            )
        }

    }
}

@Composable
@Preview
private fun CalculatorBadBillingDateScreenPreview() {
    SimpleBudgetTheme {
        CalculatorBadBillingDatePage(
            remainingBudget = "1245.05",
            onEvent = {}
        )
    }
}

@Composable
@Preview
private fun CalculatorBadBillingDateNothingSavedScreenPreview() {
    SimpleBudgetTheme {
        CalculatorBadBillingDatePage(
            remainingBudget = "0",
            onEvent = {}
        )
    }
}

