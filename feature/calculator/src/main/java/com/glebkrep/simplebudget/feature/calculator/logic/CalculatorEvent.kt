package com.glebkrep.simplebudget.feature.calculator.logic

import com.glebkrep.simplebudget.model.CalculatorButton

sealed interface CalculatorEvent {
    data class KeyTap(val key: CalculatorButton) : CalculatorEvent
    data class CommitTransaction(val input: String, val comment: String?) : CalculatorEvent
    data object SelectIncreaseDaily : CalculatorEvent
    data object SelectIncreaseToday : CalculatorEvent
    data object SelectStartNextDay : CalculatorEvent
    data object OnSettingsClicked : CalculatorEvent
    data class DeleteRecent(val id: Int) : CalculatorEvent
}
