package com.glebkrep.simplebudget.core.domain.usecases

import com.glebkrep.simplebudget.core.common.Dispatcher
import com.glebkrep.simplebudget.core.common.SimpleBudgetDispatcher
import com.glebkrep.simplebudget.core.data.data.repositories.budgetData.BudgetRepository
import com.glebkrep.simplebudget.core.data.data.repositories.calculatorInput.CalculatorInputRepository
import com.glebkrep.simplebudget.core.domain.models.BudgetDataWithInput
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetBudgetAndInputUseCase @Inject internal constructor(
    private val budgetRepository: BudgetRepository,
    private val inputRepository: CalculatorInputRepository,
    @Dispatcher(SimpleBudgetDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) {
    operator fun invoke(): Flow<BudgetDataWithInput> = budgetRepository.getBudgetData()
        .combine(inputRepository.getCalculatorInput()) { budgetData, calculatorInput ->
            BudgetDataWithInput(
                todayBudget = budgetData.todayBudget,
                dailyBudget = budgetData.dailyBudget,
                totalLeft = budgetData.totalLeft,
                billingTimestamp = budgetData.billingTimestamp,
                lastLoginTimestamp = budgetData.lastLoginTimestamp,
                lastBillingUpdateTimestamp = budgetData.lastBillingUpdateTimestamp,
                calculatorInput = calculatorInput
            )
        }
        .flowOn(ioDispatcher)
}
