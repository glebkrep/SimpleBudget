package com.glebkrep.simplebudget.core.domain.usecases

import com.glebkrep.simplebudget.core.common.Dispatcher
import com.glebkrep.simplebudget.core.common.SimpleBudgetDispatcher
import com.glebkrep.simplebudget.core.data.data.models.BudgetDataOperations
import com.glebkrep.simplebudget.core.data.data.models.CalculatorScreenState
import com.glebkrep.simplebudget.core.data.data.repositories.budgetData.BudgetRepository
import com.glebkrep.simplebudget.core.data.data.repositories.calculatorInput.CalculatorInputRepository
import com.glebkrep.simplebudget.core.data.data.repositories.preferences.PreferencesRepository
import com.glebkrep.simplebudget.core.data.data.repositories.recentTransactions.RecentTransactionsRepository
import com.glebkrep.simplebudget.core.database.recentTransaction.RecentTransactionEntity
import com.glebkrep.simplebudget.core.domain.dayDiffTo
import com.glebkrep.simplebudget.core.domain.toPrettyString
import com.glebkrep.simplebudget.model.AppPreferences
import com.glebkrep.simplebudget.model.BudgetData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCalculatorScreenUiStateUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val preferencesRepository: PreferencesRepository,
    private val calculatorInputRepository: CalculatorInputRepository,
    private val recentTransactionsRepository: RecentTransactionsRepository,
    private val createBudgetUiStateUseCase: CreateBudgetUiStateUseCase,
    private val createUpdatedBudgetDataUseCase: CreateUpdatedBudgetDataUseCase,
    @Dispatcher(SimpleBudgetDispatcher.Default) private val defaultDispatcher: CoroutineDispatcher
) {

    operator fun invoke(
        currentTimestamp: Long = System.currentTimeMillis()
    ): Flow<CalculatorScreenState?> = combine(
        calculatorInputRepository.getCalculatorInput(),
        budgetRepository.getBudgetData(),
        preferencesRepository.getPreferences(),
        recentTransactionsRepository.getRecentTransactions(),
        recentTransactionsRepository.getTotalNumberOfRecentTransactionsFlow(),
    ) { calculatorInput, budgetData, preferences, recentTransactions, numberOfTransactions ->
        return@combine when {

            budgetData.billingTimestamp.dayDiffTo(currentTimestamp) > 0 -> {
                suggestUpdateBillingDate(budgetData = budgetData)
            }

            currentTimestamp.dayDiffTo(budgetData.lastLoginTimestamp) > 0 -> {
                forceBudgetUpdate(budgetData = budgetData)
                null
            }

            currentTimestamp.dayDiffTo(budgetData.lastLoginTimestamp) == 0 -> {
                nothing(
                    budgetData = budgetData,
                    calculatorInput = calculatorInput,
                    recentTransactions = recentTransactions,
                    preferences = preferences,
                    numberOfRecentTransactions = numberOfTransactions
                )
            }

            else -> {
                suggestIncreaseDailyOrTotal(
                    budgetData = budgetData,
                    currentTimestamp = currentTimestamp
                )
            }
        }
    }.flowOn(defaultDispatcher)

    private fun suggestUpdateBillingDate(budgetData: BudgetData): CalculatorScreenState.BadBillingDate {
        return CalculatorScreenState.BadBillingDate(
            budgetData.totalLeft.toPrettyString(),
        )
    }

    private suspend fun forceBudgetUpdate(budgetData: BudgetData) {
        val newBudgetData = createUpdatedBudgetDataUseCase(
            operation = BudgetDataOperations.NewTotalBudget(budgetData.totalLeft.toString()),
            budgetData = budgetData
        )
        budgetRepository.setBudgetData(newBudgetData)
    }

    private fun nothing(
        budgetData: BudgetData,
        calculatorInput: String,
        recentTransactions: List<RecentTransactionEntity>,
        numberOfRecentTransactions: Int,
        preferences: AppPreferences
    ): CalculatorScreenState.Default {
        val newBudgeData = createUpdatedBudgetDataUseCase(
            operation = BudgetDataOperations.HandleCalculatorInput(
                calculatorInput
            ),
            budgetData = budgetData
        )
        val newUiState = createBudgetUiStateUseCase(
            oldBudgetData = budgetData,
            calculatorInput = calculatorInput,
            recentTransaction = recentTransactions,
            newBudgetData = newBudgeData,
            preferences = preferences,
            numberOfRecentTransactions = numberOfRecentTransactions
        )
        return CalculatorScreenState.Default(
            newUiState
        )
    }

    private fun suggestIncreaseDailyOrTotal(
        budgetData: BudgetData,
        currentTimestamp: Long
    ): CalculatorScreenState {
        val daysLeft = currentTimestamp.dayDiffTo(budgetData.billingTimestamp) + 1
        if (budgetData.todayBudget == 0.0) {
            return CalculatorScreenState.AskedToStartNewDay(
                budgetLeft = budgetData.totalLeft.toPrettyString(),
                daysLeft = daysLeft.toString()
            )
        }
        val budgetDataIncreaseDaily = createUpdatedBudgetDataUseCase.invoke(
            operation = BudgetDataOperations.TransferLeftoverTodayToDaily,
            budgetData = budgetData
        )
        val budgetDataIncreaseToday = createUpdatedBudgetDataUseCase.invoke(
            operation = BudgetDataOperations.TransferLeftoverTodayToToday,
            budgetData = budgetData
        )
        return CalculatorScreenState.AskedToUpdateDailyOrTodayBudget(
            dailyFromTo = Pair(
                first = budgetData.dailyBudget.toPrettyString(),
                second = budgetDataIncreaseDaily.dailyBudget.toPrettyString()
            ),
            todayFromTo = Pair(
                first = budgetData.dailyBudget.toPrettyString(),
                second = budgetDataIncreaseToday.todayBudget.toPrettyString()
            ),
            budgetLeft = budgetData.totalLeft.toPrettyString(),
            daysLeft = (daysLeft).toString()
        )
    }

}
