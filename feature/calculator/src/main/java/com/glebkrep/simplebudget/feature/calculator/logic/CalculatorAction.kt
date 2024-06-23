package com.glebkrep.simplebudget.feature.calculator.logic

sealed class CalculatorAction {
    data object None : CalculatorAction()
    data object GoToSettings : CalculatorAction()
    data object RecentDeleted : CalculatorAction()
}
