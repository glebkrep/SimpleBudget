package com.glebkrep.simplebudget.core.data.data.models

import com.glebkrep.simplebudget.model.UiRecentTransaction
import kotlinx.collections.immutable.ImmutableList

data class BudgetUiState(
    val currentInput: String,

    val daysUntilBilling: String,

    val oldTodayBudget: String,
    val newTodayBudget: String?,

    val oldDailyBudget: String,
    val newDailyBudget: String?,

    val oldMoneyLeft: String,
    val newMoneyLeft: String?,

    val areCommentsEnabled: Boolean,

    val totalNumberOfRecentTransactions: Int,
    val recentTransactions: ImmutableList<UiRecentTransaction>,
)
