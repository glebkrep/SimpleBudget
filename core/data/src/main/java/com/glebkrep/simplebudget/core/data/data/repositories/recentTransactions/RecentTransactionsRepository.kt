package com.glebkrep.simplebudget.core.data.data.repositories.recentTransactions

import com.glebkrep.simplebudget.core.database.recentTransaction.RecentTransactionEntity
import kotlinx.coroutines.flow.Flow

interface RecentTransactionsRepository {
    fun getRecentTransactions(): Flow<List<RecentTransactionEntity>>

    suspend fun getTransactionById(id: Int): RecentTransactionEntity?

    suspend fun addRecent(recentTransaction: RecentTransactionEntity): Long

    suspend fun removeLatest()

    suspend fun removeById(id: Int)

}
