package com.glebkrep.simplebudget.feature.calculator.logic

import kotlinx.collections.immutable.ImmutableList

sealed interface CalculatorScreenState {
    data class Default(
        val currentInput: String,

        val daysUntilBilling: String,

        val oldTodayBudget: String,
        val newTodayBudget: String?,

        val oldDailyBudget: String,
        val newDailyBudget: String?,

        val oldMoneyLeft: String,
        val newMoneyLeft: String?,

        val areCommentsEnabled: Boolean,

        val totalNumberOfRecentTransactions: Int,
        val recentTransactions: ImmutableList<UiRecentTransaction>,
    ) : CalculatorScreenState

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
