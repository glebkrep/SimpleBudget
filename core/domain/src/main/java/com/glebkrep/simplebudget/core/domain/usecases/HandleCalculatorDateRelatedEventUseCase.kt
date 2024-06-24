package com.glebkrep.simplebudget.core.domain.usecases

import com.glebkrep.simplebudget.core.common.Dispatcher
import com.glebkrep.simplebudget.core.common.SimpleBudgetDispatcher
import com.glebkrep.simplebudget.core.data.data.repositories.budgetData.BudgetRepository
import com.glebkrep.simplebudget.core.domain.models.BudgetDataOperations
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HandleCalculatorDateRelatedEventUseCase @Inject constructor(
    private val budgetDataRepository: BudgetRepository,
    private val createUpdatedBudgetDataUseCase: CreateUpdatedBudgetDataUseCase,
    @Dispatcher(SimpleBudgetDispatcher.Default) private val defaultDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(operation: BudgetDataOperations) = withContext(defaultDispatcher) {
        val currentBudgetData = budgetDataRepository.getBudgetData().first()
        val newBudgetData = when (operation) {
            BudgetDataOperations.TransferLeftoverTodayToDaily, BudgetDataOperations.TransferLeftoverTodayToToday -> {
                createUpdatedBudgetDataUseCase(
                    budgetData = currentBudgetData,
                    operation = operation
                )
            }

            else -> {
                throw UnsupportedOperationException("Unsupported operation: $operation")
            }
        }
        budgetDataRepository.setBudgetData(newBudgetData)
    }

}
