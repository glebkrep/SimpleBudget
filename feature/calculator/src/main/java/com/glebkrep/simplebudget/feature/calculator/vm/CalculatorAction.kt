package com.glebkrep.simplebudget.feature.calculator.vm

sealed class CalculatorAction {
    data object None : CalculatorAction()
    data object GoToSettings : CalculatorAction()
    data object RecentDeleted : CalculatorAction()
}
