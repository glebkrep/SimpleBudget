package com.glebkrep.simplebudget.model

data class BudgetData(
    val todayBudget: Double,
    val dailyBudget: Double,
    val totalLeft: Double,
    val billingTimestamp: Long,
    val lastLoginTimestamp: Long,
    val lastBillingUpdateTimestamp: Long
)
