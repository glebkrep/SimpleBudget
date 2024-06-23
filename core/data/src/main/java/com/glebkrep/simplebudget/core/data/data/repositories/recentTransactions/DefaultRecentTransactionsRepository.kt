package com.glebkrep.simplebudget.core.data.data.repositories.recentTransactions

import com.glebkrep.simplebudget.core.common.Dispatcher
import com.glebkrep.simplebudget.core.common.SimpleBudgetDispatcher
import com.glebkrep.simplebudget.core.database.recentTransaction.RecentTransactionEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class DefaultRecentTransactionsRepository
@Inject constructor(
    private val localDataSource: RecentTransactionsLocalDataSource,
    @Dispatcher(SimpleBudgetDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
) : RecentTransactionsRepository {

    override suspend fun getRecentTransactionsFlow(): Flow<List<RecentTransactionEntity>> =
        withContext(ioDispatcher) {
            localDataSource.getAllItemsFlow()
        }

    override suspend fun getTotalNumberOfRecentTransactionsFlow(): Flow<Int> =
        withContext(ioDispatcher) {
            localDataSource.getTotalNumberOfItems()
        }


    override suspend fun getTransactionById(id: Int): RecentTransactionEntity? =
        withContext(ioDispatcher) {
            localDataSource.getById(id)
        }


    override suspend fun addRecent(recentTransaction: RecentTransactionEntity) =
        withContext(ioDispatcher) {
            localDataSource.insert(recentTransaction)
        }

    override suspend fun removeLatest() = withContext(ioDispatcher) {
        localDataSource.removeLatest()
    }

    override suspend fun removeById(id: Int) = withContext(ioDispatcher) {
        localDataSource.deleteById(id)
    }
}
