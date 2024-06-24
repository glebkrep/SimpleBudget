package com.glebkrep.simplebudget.core.domain.usecases

import com.glebkrep.simplebudget.core.common.Dispatcher
import com.glebkrep.simplebudget.core.common.SimpleBudgetDispatcher
import com.glebkrep.simplebudget.core.data.data.repositories.budgetData.BudgetRepository
import com.glebkrep.simplebudget.core.domain.models.BudgetDataOperations
import com.glebkrep.simplebudget.core.domain.usecases.internal.CreateUpdatedBudgetDataUseCase
import com.glebkrep.simplebudget.model.BudgetData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateBillingDateUseCase @Inject internal constructor(
    private val budgetRepository: BudgetRepository,
    private val createUpdatedBudgetDataUseCase: CreateUpdatedBudgetDataUseCase,
    @Dispatcher(SimpleBudgetDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        newBillingDate: Long
    ) = withContext(ioDispatcher) {
        val oldBudget = budgetRepository.getBudgetData().first()
        val newBudget = createUpdatedBudgetDataUseCase.invoke(
            BudgetDataOperations.NewBillingDate(newBillingDate),
            BudgetData(
                todayBudget = oldBudget.todayBudget,
                dailyBudget = oldBudget.dailyBudget,
                totalLeft = oldBudget.totalLeft,
                billingTimestamp = oldBudget.billingTimestamp,
                lastLoginTimestamp = oldBudget.lastLoginTimestamp,
                lastBillingUpdateTimestamp = oldBudget.lastBillingUpdateTimestamp
            )
        )
        budgetRepository.setBudgetData(newBudget)
    }
}
