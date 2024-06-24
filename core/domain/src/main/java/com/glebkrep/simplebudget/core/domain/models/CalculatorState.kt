package com.glebkrep.simplebudget.core.domain.models

import com.glebkrep.simplebudget.model.BudgetData

sealed class CalculatorState(val budgetData: BudgetData) {
    @Suppress("ConstructorParameterNaming")
    data class InvalidBillingDate(
        private val _budgetData: BudgetData,
    ) : CalculatorState(budgetData = _budgetData)

    @Suppress("ConstructorParameterNaming")
    data class NeedToTransferTodayRemainder(
        private val _budgetData: BudgetData,
        val todayOption: BudgetData,
        val dailyOption: BudgetData
    ) : CalculatorState(budgetData = _budgetData)

    @Suppress("ConstructorParameterNaming")
    data class NeedToStartNewDay(
        private val _budgetData: BudgetData,
    ) : CalculatorState(budgetData = _budgetData)

    @Suppress("ConstructorParameterNaming")
    data class Default(
        private val _budgetData: BudgetData,
        val budgetDataPreview: BudgetData?,
        val currentInput: String,
        val areCommentsEnabled: Boolean,
        val totalNumberOfRecentTransactions: Int,
        val recentTransactions: List<RecentTransaction>,
    ) : CalculatorState(budgetData = _budgetData)
}
