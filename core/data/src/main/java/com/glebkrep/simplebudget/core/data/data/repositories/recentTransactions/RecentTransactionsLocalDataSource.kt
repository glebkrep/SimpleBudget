package com.glebkrep.simplebudget.core.data.data.repositories.recentTransactions

import com.glebkrep.simplebudget.core.common.Dispatcher
import com.glebkrep.simplebudget.core.common.SimpleBudgetDispatcher
import com.glebkrep.simplebudget.core.database.recentTransaction.RecentTransactionDao
import com.glebkrep.simplebudget.core.database.recentTransaction.RecentTransactionEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject


class RecentTransactionsLocalDataSource
@Inject constructor(
    @Dispatcher(SimpleBudgetDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
    private val recentTransactionDao: RecentTransactionDao,
) {
    suspend fun insert(recentTransaction: RecentTransactionEntity): Long =
        withContext(ioDispatcher) {
            recentTransactionDao.insert(recentTransaction)
        }

    suspend fun removeLatest() = withContext(ioDispatcher) {
        recentTransactionDao.deleteLatest()
    }

    suspend fun deleteById(id: Int) = withContext(ioDispatcher) {
        recentTransactionDao.deleteById(id)
    }

    fun getAllItemsFlow(): Flow<List<RecentTransactionEntity>> =
        recentTransactionDao.getAllLatestFirstFlow().flowOn(ioDispatcher)

    fun getTotalNumberOfItems(): Flow<Int> = recentTransactionDao.getTotalNumberOfItems()

    suspend fun getById(id: Int): RecentTransactionEntity? = withContext(ioDispatcher) {
        recentTransactionDao.getById(id)
    }

}
