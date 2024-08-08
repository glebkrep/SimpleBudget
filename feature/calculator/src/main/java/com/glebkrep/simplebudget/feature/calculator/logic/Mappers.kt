package com.glebkrep.simplebudget.feature.calculator.logic

import com.glebkrep.simplebudget.core.domain.dayDiffTo
import com.glebkrep.simplebudget.core.domain.toPrettyDate
import com.glebkrep.simplebudget.core.domain.toPrettyString
import com.glebkrep.simplebudget.core.domain.models.CalculatorState
import com.glebkrep.simplebudget.core.domain.models.RecentTransaction
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


fun Flow<CalculatorState?>.mapToCalculatorScreenState(): Flow<CalculatorScreenState?> {
    return this.map {
        it?.toCalculatorScreenState()
    }
}

private fun CalculatorState.toCalculatorScreenState(): CalculatorScreenState {
    return when (this) {
        is CalculatorState.InvalidBillingDate -> {
            CalculatorScreenState.BadBillingDate(
                budgetLeft = budgetData.totalLeft.toPrettyString(),
            )
        }

        is CalculatorState.NeedToTransferTodayRemainder -> {
            CalculatorScreenState.AskedToUpdateDailyOrTodayBudget(
                dailyFromTo = Pair(
                    budgetData.dailyBudget.toPrettyString(),
                    dailyOption.dailyBudget.toPrettyString()
                ),
                todayFromTo = Pair(
                    budgetData.todayBudget.toPrettyString(),
                    todayOption.todayBudget.toPrettyString()
                ),
                budgetLeft = budgetData.totalLeft.toPrettyString(),
                daysLeft = (System.currentTimeMillis()
                    .dayDiffTo(budgetData.billingTimestamp) + 1).toString()
            )
        }

        is CalculatorState.NeedToStartNewDay -> {
            CalculatorScreenState.AskedToStartNewDay(
                budgetLeft = budgetData.totalLeft.toPrettyString(),
                daysLeft = (System.currentTimeMillis()
                    .dayDiffTo(budgetData.billingTimestamp) + 1).toString()
            )
        }

        is CalculatorState.Default -> {
            CalculatorScreenState.Default(
                currentInput = currentInput,
                daysUntilBilling = (System.currentTimeMillis()
                    .dayDiffTo(budgetData.billingTimestamp) + 1).toString(),
                oldTodayBudget = budgetData.todayBudget.toPrettyString(),
                newTodayBudget = budgetDataPreview?.todayBudget?.toPrettyString(),
                oldDailyBudget = budgetData.dailyBudget.toPrettyString(),
                newDailyBudget = budgetDataPreview?.dailyBudget?.toPrettyString(),
                oldMoneyLeft = budgetData.totalLeft.toPrettyString(),
                newMoneyLeft = budgetDataPreview?.totalLeft?.toPrettyString(),
                areCommentsEnabled = areCommentsEnabled,
                totalNumberOfRecentTransactions = recentTransactions.size,
                recentTransactions = recentTransactions.map {
                    it.toUiRecentTransaction(budgetData.lastBillingUpdateTimestamp)
                }.toImmutableList()
            )
        }
    }
}

fun RecentTransaction.toUiRecentTransaction(lastBillingUpdateTimestamp: Long): UiRecentTransaction {
    return UiRecentTransaction(
        id = this.uid,
        prettyValue = "${if (this.isPlusOperation) "+" else ""}${
            this.sum.toPrettyString()
        }",
        prettyDate = this.date.toPrettyDate(needTime = true),
        comment = this.comment,
        isInBillingPeriod = lastBillingUpdateTimestamp < this.date,
        isPlusOperation = this.isPlusOperation
    )
}
