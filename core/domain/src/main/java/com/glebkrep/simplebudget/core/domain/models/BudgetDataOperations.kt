package com.glebkrep.simplebudget.core.domain.models

sealed interface BudgetDataOperations {
    data class NewBillingDate(val newBillingTimestamp: Long) : BudgetDataOperations
    data class NewTotalBudget(val newBudget: String) : BudgetDataOperations
    data object TransferLeftoverTodayToDaily : BudgetDataOperations
    data object TransferLeftoverTodayToToday : BudgetDataOperations
    data class HandleCalculatorInput(val calculatorInput: String) : BudgetDataOperations
    data class RevertTransaction(val transactionSum: Double) : BudgetDataOperations
}
