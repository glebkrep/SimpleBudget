package com.glebkrep.simplebudget.core.domain.usecases

import com.glebkrep.simplebudget.core.common.Dispatcher
import com.glebkrep.simplebudget.core.common.SimpleBudgetDispatcher
import com.glebkrep.simplebudget.core.data.data.repositories.budgetData.BudgetRepository
import com.glebkrep.simplebudget.core.data.data.repositories.calculatorInput.CalculatorInputRepository
import com.glebkrep.simplebudget.core.domain.models.BudgetDataOperations
import com.glebkrep.simplebudget.core.domain.usecases.internal.CreateUpdatedBudgetDataUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateTotalBudgetWithCurrentCalculatorInputUseCase @Inject internal constructor(
    private val budgetDataRepository: BudgetRepository,
    private val calculatorInputRepository: CalculatorInputRepository,
    private val createUpdatedBudgetDataUseCase: CreateUpdatedBudgetDataUseCase,
    @Dispatcher(SimpleBudgetDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): Double = withContext(ioDispatcher) {
        val budgetData = budgetDataRepository.getBudgetData().first()
        val input = calculatorInputRepository.getCalculatorInput().first()
        val newBudget = createUpdatedBudgetDataUseCase.invoke(
            BudgetDataOperations.NewTotalBudget(input),
            budgetData
        )
        budgetDataRepository.setBudgetData(newBudget)
        return@withContext newBudget.totalLeft
    }
}
