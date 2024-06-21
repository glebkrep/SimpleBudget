package com.glebkrep.simplebudget.feature.updatebilling.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.glebkrep.simplebudget.core.ui.components.views.CalculatorButtonView
import com.glebkrep.simplebudget.core.ui.components.views.SimpleBudgetViews
import com.glebkrep.simplebudget.core.ui.theme.DefaultPadding
import com.glebkrep.simplebudget.core.ui.theme.SimpleBudgetTheme
import com.glebkrep.simplebudget.feature.update_billing.R
import com.glebkrep.simplebudget.feature.updatebilling.vm.BillingDateUpdateEvent
import com.glebkrep.simplebudget.feature.updatebilling.vm.BillingDateUpdateState
import com.glebkrep.simplebudget.model.CalculatorButton
import java.time.Instant
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillingDateUpdatePage(
    state: BillingDateUpdateState.DatePicker,
    onEvent: (BillingDateUpdateEvent) -> (Unit)
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = state.currentBillingDate,
        initialDisplayedMonthMillis = null,
        yearRange = state.yearRange,
        initialDisplayMode = DisplayMode.Picker,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return state.dateValidator(utcTimeMillis)
            }
        }
    )
    val selectedTime = datePickerState.selectedDateMillis
    Column(
        Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SimpleBudgetViews.SimpleBudgetHeaderWithBackArrow(
            headerText = stringResource(R.string.feature_update_billing_update_billing_date),
            modifier = Modifier.fillMaxWidth(), onBackClick = {
                onEvent.invoke(BillingDateUpdateEvent.Back)
            })
        SimpleBudgetDatePicker(
            state = datePickerState,
            modifier = Modifier.padding(horizontal = DefaultPadding.BIG_PADDING)
        )
        CalculatorButtonView(
            key = CalculatorButton.SAVE_CHANGE,
            isButtonEnabled = true,
            modifier = Modifier
                .padding(horizontal = DefaultPadding.BIG_PADDING)
                .height(64.dp),
            onClick = {
                if (selectedTime != null) {
                    if (selectedTime != state.currentBillingDate) {
                        onEvent.invoke(BillingDateUpdateEvent.OnNewBillingDateDate(selectedTime))
                    } else {
                        onEvent.invoke(BillingDateUpdateEvent.Back)
                    }
                }
            }
        )
    }
}

@Composable
@Preview
fun BillingDateUpdatePagePreview() {
    SimpleBudgetTheme {
        BillingDateUpdatePage(
            state = BillingDateUpdateState.DatePicker(
                currentBillingDate = Instant.now().plus(5, ChronoUnit.DAYS).toEpochMilli(),
                dateValidator = { true },
                yearRange = 2024..2025,
            ),
            onEvent = {}
        )
    }
}
