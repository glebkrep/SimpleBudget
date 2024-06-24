package com.glebkrep.simplebudget.core.data.data.repositories.budgetData

import com.glebkrep.simplebudget.core.common.Dispatcher
import com.glebkrep.simplebudget.core.common.SimpleBudgetDispatcher
import com.glebkrep.simplebudget.core.datastore.BudgetDataStoreDataSource
import com.glebkrep.simplebudget.model.BudgetData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class DefaultBudgetRepository @Inject constructor(
    private val budgetDataStoreDataSource: BudgetDataStoreDataSource,
    @Suppress("UnusedPrivateProperty")
    @Dispatcher(SimpleBudgetDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : BudgetRepository {
    override fun getBudgetData(): Flow<BudgetData> =
        budgetDataStoreDataSource.getBudgetData().flowOn(ioDispatcher)

    override suspend fun setBudgetData(budgetData: BudgetData): Unit = withContext(ioDispatcher) {
        budgetDataStoreDataSource.setBudgetData(
            budgetData
        )
    }

}
