package com.glebkrep.simplebudget.feature.updatebilling.view

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SimpleBudgetDatePicker(
    state: DatePickerState,
    modifier: Modifier = Modifier,
) {
    DatePicker(
        state = state, showModeToggle = false,
        modifier = modifier,
        title = null,
        headline = null
    )
}
