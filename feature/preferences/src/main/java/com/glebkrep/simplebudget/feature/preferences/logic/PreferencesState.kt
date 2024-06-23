package com.glebkrep.simplebudget.feature.preferences.logic

sealed interface PreferencesState {
    data class Display(
        val isCommentsEnabled: Boolean,
        val currentBudgetPretty: String,
        val currentBillingDatePretty: String
    ) : PreferencesState
}
