package com.glebkrep.simplebudget.feature.updatebudget.logic

sealed class BudgetUpdateState {
    data class BudgetInput(
        val currentBudget: String,
        val currentInput: String,
    ) : BudgetUpdateState()
}
