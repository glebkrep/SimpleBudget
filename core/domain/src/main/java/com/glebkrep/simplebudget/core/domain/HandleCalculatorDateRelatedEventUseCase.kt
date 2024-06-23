package com.glebkrep.simplebudget.core.domain

import com.glebkrep.simplebudget.core.common.Dispatcher
import com.glebkrep.simplebudget.core.common.SimpleBudgetDispatcher
import com.glebkrep.simplebudget.core.data.data.models.BudgetDataOperations
import com.glebkrep.simplebudget.core.data.data.models.CalculatorEvent
import com.glebkrep.simplebudget.core.data.data.repositories.budgetData.BudgetRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HandleCalculatorDateRelatedEventUseCase @Inject constructor(
    private val budgetDataRepository: BudgetRepository,
    private val createUpdatedBudgetDataUseCase: CreateUpdatedBudgetDataUseCase,
    @Dispatcher(SimpleBudgetDispatcher.Default) private val defaultDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(calculatorEvent: CalculatorEvent) = withContext(defaultDispatcher) {
        val currentBudgetData = budgetDataRepository.getBudgetData().first()
        val newBudgetData = when (calculatorEvent) {
            CalculatorEvent.SelectIncreaseDaily -> {
                createUpdatedBudgetDataUseCase(
                    budgetData = currentBudgetData,
                    operation = BudgetDataOperations.TransferLeftoverTodayToDaily
                )
            }

            CalculatorEvent.SelectIncreaseToday -> {
                createUpdatedBudgetDataUseCase(
                    budgetData = currentBudgetData,
                    operation = BudgetDataOperations.TransferLeftoverTodayToToday
                )
            }

            CalculatorEvent.SelectStartNextDay -> {
                createUpdatedBudgetDataUseCase(
                    budgetData = currentBudgetData,
                    operation = BudgetDataOperations.TransferLeftoverTodayToToday
                )
            }

            else -> {
                throw UnsupportedOperationException("Unsupported operation: $calculatorEvent")
            }
        }
        budgetDataRepository.setBudgetData(newBudgetData)
    }

}
