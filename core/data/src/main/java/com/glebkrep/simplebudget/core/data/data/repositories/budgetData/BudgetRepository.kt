package com.glebkrep.simplebudget.core.data.data.repositories.budgetData

import com.glebkrep.simplebudget.model.BudgetData
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    suspend fun getBudgetData(): Flow<BudgetData>
    suspend fun setBudgetData(budgetData: BudgetData)
}
