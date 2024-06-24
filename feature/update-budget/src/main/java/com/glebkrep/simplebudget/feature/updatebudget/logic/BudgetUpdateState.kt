package com.glebkrep.simplebudget.feature.updatebudget.logic

sealed interface BudgetUpdateState {
    data class BudgetInput(
        val currentBudget: String,
        val currentInput: String,
    ) : BudgetUpdateState
}
