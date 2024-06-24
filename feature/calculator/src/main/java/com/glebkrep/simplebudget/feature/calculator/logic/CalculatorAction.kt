package com.glebkrep.simplebudget.feature.calculator.logic

sealed interface CalculatorAction {
    data object None : CalculatorAction
    data object GoToSettings : CalculatorAction
    data object RecentDeleted : CalculatorAction
}
