package com.glebkrep.simplebudget.core.datastore

import androidx.datastore.core.DataStore
import com.glebkrep.simplebudget.BudgetDataProto
import com.glebkrep.simplebudget.core.common.Dispatcher
import com.glebkrep.simplebudget.core.common.SimpleBudgetDispatcher
import com.glebkrep.simplebudget.model.BudgetData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BudgetDataStoreDataSource @Inject constructor(
    private val budgetDataDatastore: DataStore<BudgetDataProto>,
    @Dispatcher(SimpleBudgetDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) {

    fun getBudgetData(): Flow<BudgetData> =
        budgetDataDatastore.data.map {
            if (it.billingTimestamp == 0.0) {
                firstTimeDataInit()
            }
            return@map BudgetData(
                todayBudget = it.todayBudget,
                dailyBudget = it.dailyBudget,
                totalLeft = it.totalLeft,
                billingTimestamp = it.billingTimestamp.toLong(),
                lastBillingUpdateTimestamp = it.lastBillingUpdateTimestamp.toLong(),
                lastLoginTimestamp = it.lastLoginTimestamp.toLong()
            )
        }.flowOn(ioDispatcher)

    suspend fun setBudgetData(budgetData: BudgetData) =
        withContext(ioDispatcher) {
            budgetDataDatastore.updateData { prefs ->
                prefs.toBuilder().apply {
                    setTodayBudget(budgetData.todayBudget)
                    setDailyBudget(budgetData.dailyBudget)
                    setTotalLeft(budgetData.totalLeft)
                    setBillingTimestamp(budgetData.billingTimestamp.toDouble())
                    setLastLoginTimestamp(budgetData.lastLoginTimestamp.toDouble())
                    setLastBillingUpdateTimestamp(budgetData.lastBillingUpdateTimestamp.toDouble())
                }.build()
            }
        }

    private suspend fun firstTimeDataInit() {
        withContext(ioDispatcher) {
            val defaultTotalLeft = DEFAULT_TOTAL_LEFT
            val defaultDaily = defaultTotalLeft / (DEFAULT_DAYS_TO_BILLING_DATE + 1)
            val defaultTimestamp =
                System.currentTimeMillis() + DEFAULT_DAYS_TO_BILLING_DATE.daysToMillis()
            setBudgetData(
                BudgetData(
                    todayBudget = defaultDaily,
                    dailyBudget = defaultDaily,
                    totalLeft = defaultTotalLeft,
                    billingTimestamp = defaultTimestamp,
                    lastLoginTimestamp = System.currentTimeMillis(),
                    lastBillingUpdateTimestamp = System.currentTimeMillis()
                )
            )
        }
    }

    @Suppress("MagicNumber")
    private fun Int.daysToMillis() = this * 24 * 60 * 60 * 1000L

    private companion object {
        private const val DEFAULT_TOTAL_LEFT = 100.0
        private const val DEFAULT_DAYS_TO_BILLING_DATE = 4
    }
}
