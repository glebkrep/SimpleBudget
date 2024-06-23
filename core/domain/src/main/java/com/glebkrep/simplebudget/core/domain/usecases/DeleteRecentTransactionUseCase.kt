package com.glebkrep.simplebudget.core.domain.usecases

import com.glebkrep.simplebudget.core.common.Dispatcher
import com.glebkrep.simplebudget.core.common.SimpleBudgetDispatcher
import com.glebkrep.simplebudget.core.data.data.models.BudgetDataOperations
import com.glebkrep.simplebudget.core.data.data.repositories.budgetData.BudgetRepository
import com.glebkrep.simplebudget.core.data.data.repositories.recentTransactions.RecentTransactionsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteRecentTransactionUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val recentTransactionsRepository: RecentTransactionsRepository,
    private val createUpdatedBudgetDataUseCase: CreateUpdatedBudgetDataUseCase,
    @Dispatcher(SimpleBudgetDispatcher.Default) private val defaultDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(transactionId: Int) = withContext(defaultDispatcher) {
        val transaction =
            recentTransactionsRepository.getTransactionById(transactionId) ?: return@withContext
        val currentBudget = budgetRepository.getBudgetData().first()
        if (transaction.date < currentBudget.lastBillingUpdateTimestamp) {
            recentTransactionsRepository.removeById(transactionId)
            return@withContext
        }
        val transactionSum =
            transaction.sum * if (transaction.isPlusOperation) 1 else -1
        val newBudgetData = createUpdatedBudgetDataUseCase.invoke(
            budgetData = currentBudget,
            operation = BudgetDataOperations.RevertTransaction(
                transactionSum
            )
        )
        budgetRepository.setBudgetData(newBudgetData)
        recentTransactionsRepository.removeById(transactionId)
    }
}
