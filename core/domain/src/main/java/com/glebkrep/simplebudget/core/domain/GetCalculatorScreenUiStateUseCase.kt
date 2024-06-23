package com.glebkrep.simplebudget.core.domain

import com.glebkrep.simplebudget.core.common.Dispatcher
import com.glebkrep.simplebudget.core.common.SimpleBudgetDispatcher
import com.glebkrep.simplebudget.core.data.data.models.BudgetDataOperations
import com.glebkrep.simplebudget.core.data.data.models.CalculatorScreenState
import com.glebkrep.simplebudget.core.data.data.repositories.budgetData.BudgetRepository
import com.glebkrep.simplebudget.core.data.data.repositories.calculatorInput.CalculatorInputRepository
import com.glebkrep.simplebudget.core.data.data.repositories.preferences.PreferencesRepository
import com.glebkrep.simplebudget.core.data.data.repositories.recentTransactions.RecentTransactionsRepository
import com.glebkrep.simplebudget.core.database.recentTransaction.RecentTransactionEntity
import com.glebkrep.simplebudget.core.domain.converters.ConvertStringToPrettyStringUseCase
import com.glebkrep.simplebudget.core.domain.converters.ConvertTimestampToDayNumberUseCase
import com.glebkrep.simplebudget.model.AppPreferences
import com.glebkrep.simplebudget.model.BudgetData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCalculatorScreenUiStateUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val preferencesRepository: PreferencesRepository,
    private val calculatorInputRepository: CalculatorInputRepository,
    private val recentTransactionsRepository: RecentTransactionsRepository,
    private val createBudgetUiStateUseCase: CreateBudgetUiStateUseCase,
    private val convertTimestampToDayNumberUseCase: ConvertTimestampToDayNumberUseCase,
    private val convertStringToPrettyStringUseCase: ConvertStringToPrettyStringUseCase,
    private val createUpdatedBudgetDataUseCase: CreateUpdatedBudgetDataUseCase,
    @Dispatcher(SimpleBudgetDispatcher.Default) private val defaultDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(
        currentTimestamp: Long = System.currentTimeMillis()
    ): Flow<CalculatorScreenState?> = withContext(defaultDispatcher) {
        return@withContext combine(
            calculatorInputRepository.getCalculatorInput(),
            budgetRepository.getBudgetData(),
            preferencesRepository.getPreferences(),
            recentTransactionsRepository.getRecentTransactionsFlow()
        ) { calculatorInput, budgetData, preferences, recentTransactions ->
            val todayDay = convertTimestampToDayNumberUseCase(currentTimestamp)
            val lastRunDay = convertTimestampToDayNumberUseCase(budgetData.lastLoginTimestamp)
            val billingDay = convertTimestampToDayNumberUseCase(budgetData.billingTimestamp)

            return@combine when {
                todayDay > billingDay -> {
                    suggestUpdateBillingDate(budgetData = budgetData)
                }

                todayDay < lastRunDay -> {
                    forceBudgetUpdate(budgetData = budgetData)
                    null
                }

                todayDay == lastRunDay -> {
                    nothing(
                        budgetData = budgetData,
                        calculatorInput = calculatorInput,
                        recentTransactions = recentTransactions,
                        preferences = preferences
                    )
                }

                else -> {
                    suggestIncreaseDailyOrTotal(
                        budgetData = budgetData,
                        currentTimestamp = currentTimestamp
                    )
                }
            }
        }
    }

    private fun suggestUpdateBillingDate(budgetData: BudgetData): CalculatorScreenState.BadBillingDate {
        return CalculatorScreenState.BadBillingDate(
            convertStringToPrettyStringUseCase(
                budgetData.totalLeft.toString()
            )
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
            preferences = preferences
        )
        return CalculatorScreenState.Default(
            newUiState
        )
    }

    private fun suggestIncreaseDailyOrTotal(
        budgetData: BudgetData,
        currentTimestamp: Long
    ): CalculatorScreenState.AskedToUpdateDailyOrTodayBudget {
        val daysLeft =
            convertTimestampToDayNumberUseCase(budgetData.billingTimestamp) -
                    convertTimestampToDayNumberUseCase(currentTimestamp) + 1
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
                first = convertStringToPrettyStringUseCase(budgetData.dailyBudget.toString()),
                second = convertStringToPrettyStringUseCase(budgetDataIncreaseDaily.dailyBudget.toString())
            ),
            todayFromTo = Pair(
                first = convertStringToPrettyStringUseCase(budgetData.dailyBudget.toString()),
                second = convertStringToPrettyStringUseCase(budgetDataIncreaseToday.todayBudget.toString())
            ),
            budgetLeft = convertStringToPrettyStringUseCase(budgetData.totalLeft.toString()),
            daysLeft = (daysLeft).toString()
        )
    }

}
