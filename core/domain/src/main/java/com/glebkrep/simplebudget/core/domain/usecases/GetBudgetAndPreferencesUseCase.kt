package com.glebkrep.simplebudget.core.domain.usecases

import com.glebkrep.simplebudget.core.common.Dispatcher
import com.glebkrep.simplebudget.core.common.SimpleBudgetDispatcher
import com.glebkrep.simplebudget.core.data.data.repositories.budgetData.BudgetRepository
import com.glebkrep.simplebudget.core.data.data.repositories.preferences.PreferencesRepository
import com.glebkrep.simplebudget.core.domain.models.BudgetDataWithPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetBudgetAndPreferencesUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val preferencesRepository: PreferencesRepository,
    @Dispatcher(SimpleBudgetDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) {
    operator fun invoke(): Flow<BudgetDataWithPreferences> = budgetRepository.getBudgetData()
        .combine(preferencesRepository.getPreferences()) { budgetData, preferences ->
            BudgetDataWithPreferences(
                todayBudget = budgetData.todayBudget,
                dailyBudget = budgetData.dailyBudget,
                totalLeft = budgetData.totalLeft,
                billingTimestamp = budgetData.billingTimestamp,
                lastLoginTimestamp = budgetData.lastLoginTimestamp,
                lastBillingUpdateTimestamp = budgetData.lastBillingUpdateTimestamp,
                isCommentsEnabled = preferences.isCommentsEnabled
            )
        }
        .flowOn(ioDispatcher)
}
