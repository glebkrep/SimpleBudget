package com.glebkrep.simplebudget.feature.updatebudget.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.glebkrep.simplebudget.core.ui.components.views.CalculatorView
import com.glebkrep.simplebudget.core.ui.components.views.SimpleBudgetViews
import com.glebkrep.simplebudget.core.ui.theme.DefaultPadding
import com.glebkrep.simplebudget.core.ui.theme.SimpleBudgetTheme
import com.glebkrep.simplebudget.feature.update_budget.R
import com.glebkrep.simplebudget.feature.updatebudget.vm.BudgetUpdateEvent
import com.glebkrep.simplebudget.feature.updatebudget.vm.BudgetUpdateState
import com.glebkrep.simplebudget.model.CalculatorButton

@Composable
fun BudgetUpdatePage(
    state: BudgetUpdateState.BudgetInput,
    onEvent: (BudgetUpdateEvent) -> (Unit)
) {
    ConstraintLayout(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val (calculatorButtons, header, currentBudgetBlock, divider) = createRefs()
        val bottomLine = createGuidelineFromBottom(0.4f)

        SimpleBudgetViews.SimpleBudgetHeaderWithBackArrow(
            headerText = stringResource(R.string.feature_update_budget_header),
            modifier = Modifier.constrainAs(header) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            },
            onBackClick = { onEvent.invoke(BudgetUpdateEvent.Back) }
        )
        Column(modifier = Modifier.constrainAs(currentBudgetBlock) {
            top.linkTo(header.bottom, DefaultPadding.LARGE)
            start.linkTo(parent.start, DefaultPadding.LARGE)
            end.linkTo(parent.end, DefaultPadding.LARGE)
            width = Dimension.fillToConstraints
        }) {
            SimpleBudgetViews.SimpleBudgetText(
                text = state.currentBudget,
                modifier = Modifier,
                textStyle = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground,
                disablePadding = true
            )
            SimpleBudgetViews.SimpleBudgetText(
                text = stringResource(R.string.feature_update_budget_current_budget),
                modifier = Modifier,
                disablePadding = true,
                textStyle = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            SimpleBudgetViews.SimpleBudgetText(
                text = stringResource(R.string.feature_update_budget_btn_save),
                modifier = Modifier.padding(top = DefaultPadding.LARGE),
                textStyle = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        HorizontalDivider(modifier = Modifier.constrainAs(divider) {
            top.linkTo(currentBudgetBlock.bottom, DefaultPadding.LARGE)
            start.linkTo(parent.start, DefaultPadding.LARGE)
            end.linkTo(parent.end, DefaultPadding.LARGE)
            width = Dimension.fillToConstraints
        })

        CalculatorView(
            calculatorInput = state.currentInput,
            isEnterAllowed = true,
            areCommentsAllowed = false,
            isSignChangeAllowed = false,
            onCommitTransaction = { _, _ ->
                onEvent.invoke(
                    BudgetUpdateEvent.KeyTap(
                        CalculatorButton.ENTER
                    )
                )
            },
            onKeyTap = { onEvent.invoke(BudgetUpdateEvent.KeyTap(it)) },
            modifier = Modifier.constrainAs(calculatorButtons) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(bottomLine)
                height = Dimension.fillToConstraints
                width = Dimension.fillToConstraints
            }
        )

    }
}

@Composable
@Preview
fun BudgetUpdatePagePreview() {
    SimpleBudgetTheme {
        BudgetUpdatePage(state = BudgetUpdateState.BudgetInput(
            currentInput = "123.33",
            currentBudget = "24504.4"
        ),
            onEvent = {})

    }
}
