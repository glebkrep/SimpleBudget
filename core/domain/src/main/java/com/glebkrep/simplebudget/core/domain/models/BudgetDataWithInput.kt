package com.glebkrep.simplebudget.core.domain.models

data class BudgetDataWithInput(
    val todayBudget: Double,
    val dailyBudget: Double,
    val totalLeft: Double,
    val billingTimestamp: Long,
    val lastLoginTimestamp: Long,
    val lastBillingUpdateTimestamp: Long,
    val calculatorInput: String,
)
