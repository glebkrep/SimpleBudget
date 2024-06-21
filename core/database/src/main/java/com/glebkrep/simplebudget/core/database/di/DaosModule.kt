package com.glebkrep.simplebudget.core.database.di

import com.glebkrep.simplebudget.core.database.recentTransaction.RecentTransactionDao
import com.glebkrep.simplebudget.core.database.SimpleBudgetRoomDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {
    @Provides
    @Singleton
    fun providesRecentTransactionDao(
        database: SimpleBudgetRoomDB,
    ): RecentTransactionDao = database.recentTransactionDao()
}
