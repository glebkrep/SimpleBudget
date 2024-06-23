package com.glebkrep.simplebudget.feature.updatebudget.logic

import com.glebkrep.simplebudget.model.CalculatorButton

sealed class BudgetUpdateEvent {
    data class KeyTap(val key: CalculatorButton) : BudgetUpdateEvent()
    data object Back : BudgetUpdateEvent()
}