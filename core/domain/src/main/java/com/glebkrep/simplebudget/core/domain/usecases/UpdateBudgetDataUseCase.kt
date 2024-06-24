package com.glebkrep.simplebudget.core.domain.usecases

import com.glebkrep.simplebudget.core.common.Dispatcher
import com.glebkrep.simplebudget.core.common.SimpleBudgetDispatcher
import com.glebkrep.simplebudget.core.data.data.repositories.budgetData.BudgetRepository
import com.glebkrep.simplebudget.model.BudgetData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateBudgetDataUseCase @Inject constructor(
    private val budgetDataRepository: BudgetRepository,
    @Dispatcher(SimpleBudgetDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        budgetData: BudgetData
    ) = withContext(ioDispatcher) {
        budgetDataRepository.setBudgetData(budgetData)
    }
}
