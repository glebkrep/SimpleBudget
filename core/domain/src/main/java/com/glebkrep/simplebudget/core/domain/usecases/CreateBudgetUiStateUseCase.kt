package com.glebkrep.simplebudget.core.domain.usecases

import com.glebkrep.simplebudget.core.data.data.models.BudgetUiState
import com.glebkrep.simplebudget.core.database.recentTransaction.RecentTransactionEntity
import com.glebkrep.simplebudget.core.domain.dayDiffTo
import com.glebkrep.simplebudget.core.domain.toPrettyDate
import com.glebkrep.simplebudget.core.domain.toPrettyString
import com.glebkrep.simplebudget.model.AppPreferences
import com.glebkrep.simplebudget.model.BudgetData
import com.glebkrep.simplebudget.model.UiRecentTransaction
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject


class CreateBudgetUiStateUseCase @Inject constructor() {

    @Suppress("LongParameterList")
    operator fun invoke(
        oldBudgetData: BudgetData,
        calculatorInput: String,
        recentTransaction: List<RecentTransactionEntity>,
        newBudgetData: BudgetData,
        numberOfRecentTransactions: Int,
        preferences: AppPreferences
    ): BudgetUiState {
        val daysToBilling = System.currentTimeMillis().dayDiffTo(newBudgetData.billingTimestamp) + 1
        val isDailyBudgetUpdated = oldBudgetData.dailyBudget != newBudgetData.dailyBudget
        val isTodayBudgetUpdated = oldBudgetData.todayBudget != newBudgetData.todayBudget
        val isTotalBudgetUpdated = oldBudgetData.totalLeft != newBudgetData.totalLeft

        return BudgetUiState(
            currentInput = calculatorInput,
            oldTodayBudget = oldBudgetData.todayBudget.toPrettyString(),
            newTodayBudget = if (isTodayBudgetUpdated)
                newBudgetData.todayBudget.toPrettyString()
            else null,

            oldDailyBudget = oldBudgetData.dailyBudget.toPrettyString(),
            newDailyBudget = if (isDailyBudgetUpdated)
                newBudgetData.dailyBudget.toPrettyString()
            else null,

            oldMoneyLeft = oldBudgetData.totalLeft.toPrettyString(),
            newMoneyLeft = if (isTotalBudgetUpdated)
                newBudgetData.totalLeft.toPrettyString()
            else null,

            daysUntilBilling = daysToBilling.toString(),
            recentTransactions = recentTransaction.map {
                recentTransactionToUiRecent(
                    it,
                    oldBudgetData.lastBillingUpdateTimestamp
                )
            }.toImmutableList(),
            areCommentsEnabled = preferences.isCommentsEnabled,
            totalNumberOfRecentTransactions = numberOfRecentTransactions
        )
    }

    private fun recentTransactionToUiRecent(
        recentTransaction: RecentTransactionEntity,
        lastBillingUpdateTimestamp: Long
    ): UiRecentTransaction {
        return UiRecentTransaction(
            id = recentTransaction.uid,
            prettyValue = "${if (recentTransaction.isPlusOperation) "+" else ""}${
                recentTransaction.sum.toPrettyString()
            }",
            prettyDate = recentTransaction.date.toPrettyDate(needTime = true),
            comment = recentTransaction.comment,
            isInBillingPeriod = lastBillingUpdateTimestamp < recentTransaction.date,
            isPlusOperation = recentTransaction.isPlusOperation
        )
    }

}
