package com.glebkrep.simplebudget.feature.preferences.logic

sealed class PreferencesState {
    data class Display(
        val isCommentsEnabled: Boolean,
        val currentBudgetPretty: String,
        val currentBillingDatePretty: String
    ) : PreferencesState()
}
