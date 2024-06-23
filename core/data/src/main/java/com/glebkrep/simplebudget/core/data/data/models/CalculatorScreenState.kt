package com.glebkrep.simplebudget.core.data.data.models

sealed interface CalculatorScreenState {
    data class Default(val budgetUiState: BudgetUiState) : CalculatorScreenState
    data class AskedToUpdateDailyOrTodayBudget(
        val dailyFromTo: Pair<String, String>,
        val todayFromTo: Pair<String, String>,
        val budgetLeft: String,
        val daysLeft: String
    ) : CalculatorScreenState

    data class AskedToStartNewDay(val budgetLeft: String, val daysLeft: String) :
        CalculatorScreenState

    data class BadBillingDate(val budgetLeft: String) : CalculatorScreenState
}
