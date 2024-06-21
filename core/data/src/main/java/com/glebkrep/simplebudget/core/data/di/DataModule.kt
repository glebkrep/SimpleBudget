package com.glebkrep.simplebudget.core.data.di

import com.glebkrep.simplebudget.core.data.data.repositories.budgetData.BudgetRepository
import com.glebkrep.simplebudget.core.data.data.repositories.budgetData.DefaultBudgetRepository
import com.glebkrep.simplebudget.core.data.data.repositories.calculatorInput.CalculatorInputRepository
import com.glebkrep.simplebudget.core.data.data.repositories.calculatorInput.DefaultCalculatorInputRepository
import com.glebkrep.simplebudget.core.data.data.repositories.preferences.DefaultPreferencesRepository
import com.glebkrep.simplebudget.core.data.data.repositories.preferences.PreferencesRepository
import com.glebkrep.simplebudget.core.data.data.repositories.recentTransactions.DefaultRecentTransactionsRepository
import com.glebkrep.simplebudget.core.data.data.repositories.recentTransactions.RecentTransactionsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataModule {
    @Binds
    internal abstract fun bindsBudgetRepository(
        budgetRepository: DefaultBudgetRepository,
    ): BudgetRepository

    @Binds
    internal abstract fun bindsPreferencesRepository(
        preferencesRepository: DefaultPreferencesRepository,
    ): PreferencesRepository

    @Binds
    internal abstract fun bindsCalculatorInputRepository(
        calculatorInputRepository: DefaultCalculatorInputRepository,
    ): CalculatorInputRepository

    @Binds
    internal abstract fun bindsRecentTransactionsRepository(
        recentTransactionsRepository: DefaultRecentTransactionsRepository,
    ): RecentTransactionsRepository
}
