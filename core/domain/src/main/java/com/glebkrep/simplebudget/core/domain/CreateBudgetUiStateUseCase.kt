package com.glebkrep.simplebudget.core.domain

import com.glebkrep.simplebudget.core.data.data.models.BudgetUiState
import com.glebkrep.simplebudget.core.database.recentTransaction.RecentTransactionEntity
import com.glebkrep.simplebudget.core.domain.converters.ConvertStringToPrettyStringUseCase
import com.glebkrep.simplebudget.core.domain.converters.ConvertTimestampToDayNumberUseCase
import com.glebkrep.simplebudget.core.domain.converters.ConvertTimestampToPrettyDateUseCase
import com.glebkrep.simplebudget.model.AppPreferences
import com.glebkrep.simplebudget.model.BudgetData
import com.glebkrep.simplebudget.model.UiRecentTransaction
import javax.inject.Inject


class CreateBudgetUiStateUseCase @Inject constructor(
    private val convertTimestampToDayNumberUseCase: ConvertTimestampToDayNumberUseCase,
    private val convertStringToPrettyStringUseCase: ConvertStringToPrettyStringUseCase,
    private val convertTimestampToPrettyDateUseCase: ConvertTimestampToPrettyDateUseCase,
) {

    @Suppress("LongParameterList")
    suspend operator fun invoke(
        oldBudgetData: BudgetData,
        calculatorInput: String,
        recentTransaction: List<RecentTransactionEntity>,
        newBudgetData: BudgetData,
        numberOfRecentTransactions: Int,
        preferences: AppPreferences
    ): BudgetUiState {
        val daysToBilling =
            convertTimestampToDayNumberUseCase(newBudgetData.billingTimestamp) - convertTimestampToDayNumberUseCase(
                System.currentTimeMillis()
            ) + 1
        val isDailyBudgetUpdated = oldBudgetData.dailyBudget != newBudgetData.dailyBudget
        val isTodayBudgetUpdated = oldBudgetData.todayBudget != newBudgetData.todayBudget
        val isTotalBudgetUpdated = oldBudgetData.totalLeft != newBudgetData.totalLeft

        return BudgetUiState(
            currentInput = calculatorInput,

            oldTodayBudget = convertStringToPrettyStringUseCase(oldBudgetData.todayBudget.toString()),
            newTodayBudget = if (isTodayBudgetUpdated) convertStringToPrettyStringUseCase(
                newBudgetData.todayBudget.toString()
            ) else null,

            oldDailyBudget = convertStringToPrettyStringUseCase(oldBudgetData.dailyBudget.toString()),
            newDailyBudget = if (isDailyBudgetUpdated) convertStringToPrettyStringUseCase(
                newBudgetData.dailyBudget.toString()
            ) else null,

            oldMoneyLeft = convertStringToPrettyStringUseCase(oldBudgetData.totalLeft.toString()),
            newMoneyLeft = if (isTotalBudgetUpdated) convertStringToPrettyStringUseCase(
                newBudgetData.totalLeft.toString()
            ) else null,

            daysUntilBilling = convertStringToPrettyStringUseCase(daysToBilling.toString()),
            recentTransactions = recentTransaction.map {
                recentTransactionToUiRecent(
                    it,
                    oldBudgetData.lastBillingUpdateTimestamp
                )
            },
            areCommentsEnabled = preferences.isCommentsEnabled ?: true,
            totalNumberOfRecentTransactions = numberOfRecentTransactions
        )
    }

    private suspend fun recentTransactionToUiRecent(
        recentTransaction: RecentTransactionEntity,
        lastBillingUpdateTimestamp: Long
    ): UiRecentTransaction {
        return UiRecentTransaction(
            id = recentTransaction.uid,
            prettyValue = "${if (recentTransaction.isPlusOperation) "+" else ""}${
                convertStringToPrettyStringUseCase(
                    recentTransaction.sum.toString()
                )
            }",
            prettyDate = convertTimestampToPrettyDateUseCase(
                timestamp = recentTransaction.date,
                needTime = true
            ),
            comment = recentTransaction.comment,
            isInBillingPeriod = lastBillingUpdateTimestamp < recentTransaction.date,
            isPlusOperation = recentTransaction.isPlusOperation
        )
    }

}
