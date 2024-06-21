package com.glebkrep.simplebudget.core.data.data.models

import com.glebkrep.simplebudget.model.CalculatorButton

sealed class CalculatorEvent {
    data class KeyTap(val key: CalculatorButton) : CalculatorEvent()
    data class CommitTransaction(val input: String, val comment: String?) : CalculatorEvent()
    data object SelectIncreaseDaily : CalculatorEvent()
    data object SelectIncreaseToday : CalculatorEvent()
    data object OnSettingsClicked : CalculatorEvent()


    data class DeleteRecent(val id: Int) : CalculatorEvent()
}
