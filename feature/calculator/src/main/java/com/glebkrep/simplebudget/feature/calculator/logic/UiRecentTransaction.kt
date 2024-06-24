package com.glebkrep.simplebudget.feature.calculator.logic

data class UiRecentTransaction(
    val id: Int,
    val prettyValue: String,
    val prettyDate: String,
    val isInBillingPeriod: Boolean = true,
    val isPlusOperation: Boolean = false,
    val comment: String? = null
)
