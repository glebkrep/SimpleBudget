package com.glebkrep.simplebudget.core.domain.usecases

import com.glebkrep.simplebudget.core.common.Dispatcher
import com.glebkrep.simplebudget.core.common.SimpleBudgetDispatcher
import com.glebkrep.simplebudget.core.data.data.repositories.budgetData.BudgetRepository
import com.glebkrep.simplebudget.core.data.data.repositories.calculatorInput.CalculatorInputRepository
import com.glebkrep.simplebudget.core.data.data.repositories.preferences.PreferencesRepository
import com.glebkrep.simplebudget.core.data.data.repositories.recentTransactions.RecentTransactionsRepository
import com.glebkrep.simplebudget.core.domain.dayDiffTo
import com.glebkrep.simplebudget.core.domain.models.BudgetDataOperations
import com.glebkrep.simplebudget.core.domain.models.CalculatorState
import com.glebkrep.simplebudget.core.domain.models.RecentTransaction
import com.glebkrep.simplebudget.core.domain.models.toRecentTransaction
import com.glebkrep.simplebudget.model.AppPreferences
import com.glebkrep.simplebudget.model.BudgetData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCalculatorStateUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val preferencesRepository: PreferencesRepository,
    private val calculatorInputRepository: CalculatorInputRepository,
    private val recentTransactionsRepository: RecentTransactionsRepository,
    private val createUpdatedBudgetDataUseCase: CreateUpdatedBudgetDataUseCase,
    @Dispatcher(SimpleBudgetDispatcher.Default) private val defaultDispatcher: CoroutineDispatcher
) {

    operator fun invoke(
        currentTimestamp: Long = System.currentTimeMillis()
    ): Flow<CalculatorState?> = combine(
        calculatorInputRepository.getCalculatorInput(),
        budgetRepository.getBudgetData(),
        preferencesRepository.getPreferences(),
        recentTransactionsRepository.getRecentTransactions()
            .map { recentTransactionsList -> recentTransactionsList.map { it.toRecentTransaction() } },
        recentTransactionsRepository.getTotalNumberOfRecentTransactionsFlow(),
    ) { calculatorInput, budgetData, preferences, recentTransactions, numberOfTransactions ->
        return@combine when {

            budgetData.billingTimestamp.dayDiffTo(currentTimestamp) > 0 -> {
                budgetData.suggestUpdateBillingDate()
            }

            currentTimestamp.dayDiffTo(budgetData.lastLoginTimestamp) > 0 -> {
                budgetData.updateBudgetNow()
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
                if (budgetData.todayBudget == 0.0) {
                    CalculatorState.NeedToStartNewDay(
                        _budgetData = budgetData,
                    )
                } else {
                    budgetData.suggestIncreaseDailyOrTotal()
                }
            }
        }
    }
        .flowOn(defaultDispatcher)

    private fun BudgetData.suggestUpdateBillingDate(): CalculatorState.InvalidBillingDate {
        return CalculatorState.InvalidBillingDate(
            _budgetData = this,
        )
    }

    private suspend fun BudgetData.updateBudgetNow() {
        val newBudgetData = createUpdatedBudgetDataUseCase(
            operation = BudgetDataOperations.NewTotalBudget(this.totalLeft.toString()),
            budgetData = this
        )
        budgetRepository.setBudgetData(newBudgetData)
    }

    private fun nothing(
        budgetData: BudgetData,
        calculatorInput: String,
        recentTransactions: List<RecentTransaction>,
        numberOfRecentTransactions: Int,
        preferences: AppPreferences
    ): CalculatorState.Default {
        val newBudgeData = createUpdatedBudgetDataUseCase(
            operation = BudgetDataOperations.HandleCalculatorInput(
                calculatorInput
            ),
            budgetData = budgetData
        )

        return CalculatorState.Default(
            _budgetData = budgetData,
            budgetDataPreview = if (newBudgeData == budgetData) null else newBudgeData,
            currentInput = calculatorInput,
            areCommentsEnabled = preferences.isCommentsEnabled,
            totalNumberOfRecentTransactions = numberOfRecentTransactions,
            recentTransactions = recentTransactions
        )
    }

    private fun BudgetData.suggestIncreaseDailyOrTotal(): CalculatorState.NeedToTransferTodayRemainder {
        val budgetDataIncreaseDaily = createUpdatedBudgetDataUseCase.invoke(
            operation = BudgetDataOperations.TransferLeftoverTodayToDaily,
            budgetData = this
        )
        val budgetDataIncreaseToday = createUpdatedBudgetDataUseCase.invoke(
            operation = BudgetDataOperations.TransferLeftoverTodayToToday,
            budgetData = this
        )
        return CalculatorState.NeedToTransferTodayRemainder(
            dailyOption = budgetDataIncreaseDaily,
            todayOption = budgetDataIncreaseToday,
            _budgetData = this
        )
    }

}
