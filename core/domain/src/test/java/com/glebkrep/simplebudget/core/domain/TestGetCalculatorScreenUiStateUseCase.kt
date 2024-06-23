package com.glebkrep.simplebudget.core.domain

import com.glebkrep.simplebudget.core.data.data.models.CalculatorScreenState
import com.glebkrep.simplebudget.core.data.data.repositories.budgetData.BudgetRepository
import com.glebkrep.simplebudget.core.data.data.repositories.calculatorInput.CalculatorInputRepository
import com.glebkrep.simplebudget.core.data.data.repositories.preferences.PreferencesRepository
import com.glebkrep.simplebudget.core.data.data.repositories.recentTransactions.RecentTransactionsRepository
import com.glebkrep.simplebudget.core.database.recentTransaction.RecentTransactionEntity
import com.glebkrep.simplebudget.core.domain.converters.ConvertDoubleToPrettyDoubleUseCase
import com.glebkrep.simplebudget.core.domain.converters.ConvertStringToDoubleSmartUseCase
import com.glebkrep.simplebudget.core.domain.converters.ConvertStringToPrettyStringUseCase
import com.glebkrep.simplebudget.core.domain.converters.ConvertTimestampToDayNumberUseCase
import com.glebkrep.simplebudget.core.domain.converters.ConvertTimestampToPrettyDateUseCase
import com.glebkrep.simplebudget.model.AppPreferences
import com.glebkrep.simplebudget.model.BudgetData
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.math.BigDecimal
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class TestGetCalculatorScreenUiStateUseCase {

    @Suppress("LongParameterList", "SameParameterValue")
    private fun getGetCalculatorScreenUiStateUseCase(
        calculatorInput: String,
        todayBudget: Double,
        dailyBudget: Double,
        totalBudget: Double,
        billingTime: Instant,
        lastLoginTime: Instant
    ): GetCalculatorScreenUiStateUseCase {
        val budgetData = BudgetData(
            todayBudget = todayBudget,
            dailyBudget = dailyBudget,
            totalLeft = totalBudget,
            billingTimestamp = billingTime.toEpochMilli(),
            lastLoginTimestamp = lastLoginTime.toEpochMilli(),
            lastBillingUpdateTimestamp = System.currentTimeMillis()
        )
        val budgetRepository = mockk<BudgetRepository>()
        coEvery { budgetRepository.getBudgetData() } returns flow {
            emit(budgetData)
        }
        coJustRun { budgetRepository.setBudgetData(any()) }

        val calculatorInputRepository = mockk<CalculatorInputRepository>()
        coEvery { calculatorInputRepository.getCalculatorInput() } returns flow {
            emit(calculatorInput)
        }

        val recentTransactionRepository = mockk<RecentTransactionsRepository>()
        coEvery { recentTransactionRepository.getRecentTransactionsFlow() } returns flow {
            emit(
                listOf(
                    RecentTransactionEntity(
                        date = System.currentTimeMillis(),
                        sum = 1.0,
                        isPlusOperation = false,
                        comment = null
                    )
                )
            )
        }

        val preferenceRepository = mockk<PreferencesRepository>()
        coEvery { preferenceRepository.getPreferences() } returns flow {
            emit(AppPreferences())
        }

        return GetCalculatorScreenUiStateUseCase(
            budgetRepository = budgetRepository,
            calculatorInputRepository = calculatorInputRepository,
            recentTransactionsRepository = recentTransactionRepository,
            createBudgetUiStateUseCase = CreateBudgetUiStateUseCase(
                convertStringToPrettyStringUseCase = ConvertStringToPrettyStringUseCase(),
                convertTimestampToDayNumberUseCase = ConvertTimestampToDayNumberUseCase(),
                convertTimestampToPrettyDateUseCase = ConvertTimestampToPrettyDateUseCase()
            ),
            convertTimestampToDayNumberUseCase = ConvertTimestampToDayNumberUseCase(),
            convertStringToPrettyStringUseCase = ConvertStringToPrettyStringUseCase(),
            createUpdatedBudgetDataUseCase = CreateUpdatedBudgetDataUseCase(
                convertTimestampToDayNumberUseCase = ConvertTimestampToDayNumberUseCase(),
                convertDoubleToPrettyDoubleUseCase = ConvertDoubleToPrettyDoubleUseCase(),
                convertStringToDoubleSmartUseCase = ConvertStringToDoubleSmartUseCase()
            ),
            defaultDispatcher = Dispatchers.Default,
            preferencesRepository = preferenceRepository
        )
    }

    @Test
    fun `suggest increase daily or total 1`() = runBlocking {
        val currentTime = Instant.now()
        val billingTime = currentTime.plus(1, ChronoUnit.DAYS)
        val lastLoginTime = currentTime.minus(1, ChronoUnit.DAYS)

        val useCase = getGetCalculatorScreenUiStateUseCase(
            calculatorInput = "123",
            todayBudget = 5.0,
            dailyBudget = 10.0,
            totalBudget = 25.0,
            billingTime = billingTime,
            lastLoginTime = lastLoginTime
        )

        val result = useCase.invoke(
            currentTimestamp = currentTime.toEpochMilli()
        ).first()

        assertIs<CalculatorScreenState.AskedToUpdateDailyOrTodayBudget>(result)
        assertEquals("10", result.dailyFromTo.first)
        assertEquals("12.5", result.dailyFromTo.second)
        assertEquals("10", result.todayFromTo.first)
        assertEquals("15", result.todayFromTo.second)
        assertEquals("2", result.daysLeft)
        assertEquals("25", result.budgetLeft)
    }

    @Test
    fun `suggest increase daily or total 2`() = runBlocking {
        val currentTime = Instant.now()
        val billingTime = currentTime.plus(1, ChronoUnit.SECONDS)
        val lastLoginTime = currentTime.minus(1, ChronoUnit.DAYS)

        val useCase = getGetCalculatorScreenUiStateUseCase(
            calculatorInput = "123",
            todayBudget = 5.0,
            dailyBudget = 10.0,
            totalBudget = 25.0,
            billingTime = billingTime,
            lastLoginTime = lastLoginTime
        )

        val result = useCase.invoke(
            currentTimestamp = currentTime.toEpochMilli()
        ).first()

        assertIs<CalculatorScreenState.AskedToUpdateDailyOrTodayBudget>(result)
        assertEquals("10", result.dailyFromTo.first)
        assertEquals("25", result.dailyFromTo.second)
        assertEquals("10", result.todayFromTo.first)
        assertEquals("25", result.todayFromTo.second)
        assertEquals("1", result.daysLeft)
        assertEquals("25", result.budgetLeft)
    }

    @Test
    fun `suggest update billing date 1`() = runBlocking {
        val currentTime = Instant.now()
        val billingTime = currentTime.minus(1, ChronoUnit.DAYS)
        val lastLoginTime = currentTime.minus(2, ChronoUnit.DAYS)

        val useCase = getGetCalculatorScreenUiStateUseCase(
            calculatorInput = "123",
            todayBudget = 5.0,
            dailyBudget = 10.0,
            totalBudget = 25.0,
            billingTime = billingTime,
            lastLoginTime = lastLoginTime
        )

        val result = useCase.invoke(
            currentTimestamp = currentTime.toEpochMilli()
        ).first()

        println(result.toString())
        assertIs<CalculatorScreenState.BadBillingDate>(result)
        assertEquals("25", result.budgetLeft)
    }

    @Test
    fun `suggest update billing date 2`() = runBlocking {
        val currentTime = Instant.now()
        val billingTime = currentTime.minus(1, ChronoUnit.DAYS)
        @Suppress("UnnecessaryVariable", "RedundantSuppression") val lastLoginTime = currentTime

        val useCase = getGetCalculatorScreenUiStateUseCase(
            calculatorInput = "123",
            todayBudget = 5.0,
            dailyBudget = 10.0,
            totalBudget = 25.0,
            billingTime = billingTime,
            lastLoginTime = lastLoginTime
        )

        val result = useCase.invoke(
            currentTimestamp = currentTime.toEpochMilli()
        ).first()

        println(result.toString())
        assertIs<CalculatorScreenState.BadBillingDate>(result)
        assertEquals("25", result.budgetLeft)
    }

    @Test
    fun `nothing 1`() = runBlocking {
        val currentTime = Instant.now()
        val billingTime = currentTime.plus(1, ChronoUnit.DAYS)
        val lastLoginTime = currentTime.minus(1, ChronoUnit.MINUTES)

        val calculatorInput = "123"

        val useCase = getGetCalculatorScreenUiStateUseCase(
            calculatorInput = calculatorInput,
            todayBudget = 5.0,
            dailyBudget = 10.0,
            totalBudget = 25.0,
            billingTime = billingTime,
            lastLoginTime = lastLoginTime
        )

        val result = useCase.invoke(
            currentTimestamp = currentTime.toEpochMilli()
        ).first()

        println(result.toString())
        assertIs<CalculatorScreenState.Default>(result)
        assertEquals(calculatorInput, result.budgetUiState.currentInput)
    }

    @Test
    fun `nothing 2`() = runBlocking {
        val currentTime = Instant.now()
        val billingTime = currentTime.plus(1, ChronoUnit.DAYS)
        val lastLoginTime = currentTime.minus(1, ChronoUnit.MINUTES)

        val calculatorInput = "+10000000"

        val useCase = getGetCalculatorScreenUiStateUseCase(
            calculatorInput = calculatorInput,
            todayBudget = 5.0,
            dailyBudget = 10.0,
            totalBudget = 25.0,
            billingTime = billingTime,
            lastLoginTime = lastLoginTime
        )

        val result = useCase.invoke(
            currentTimestamp = currentTime.toEpochMilli()
        ).first()

        println(result.toString())
        assertIs<CalculatorScreenState.Default>(result)
        assertEquals(calculatorInput, result.budgetUiState.currentInput)
        assertEquals(
            BigDecimal(10000025.0).toPlainString(),
            result.budgetUiState.newMoneyLeft
        )
    }

    @Test
    fun `nothing 3`() = runBlocking {
        val currentTime = Instant.now()
        val billingTime = currentTime.plus(1, ChronoUnit.DAYS)
        val lastLoginTime = currentTime.minus(1, ChronoUnit.MINUTES)

        val calculatorInput = "100000000"

        val useCase = getGetCalculatorScreenUiStateUseCase(
            calculatorInput = calculatorInput,
            todayBudget = 5.0,
            dailyBudget = 10.0,
            totalBudget = 25.0,
            billingTime = billingTime,
            lastLoginTime = lastLoginTime
        )

        val result = useCase.invoke(
            currentTimestamp = currentTime.toEpochMilli()
        ).first()

        println(result.toString())
        assertIs<CalculatorScreenState.Default>(result)
        assertEquals(calculatorInput, result.budgetUiState.currentInput)
        assertEquals(
            "0",
            result.budgetUiState.newMoneyLeft
        )
    }

    @Test
    fun `force budget update 1`() = runBlocking {
        val currentTime = Instant.now()
        val billingTime = currentTime.plus(15, ChronoUnit.DAYS)
        val lastLoginTime = currentTime.plus(5, ChronoUnit.DAYS)

        val calculatorInput = "123"

        val useCase = getGetCalculatorScreenUiStateUseCase(
            calculatorInput = calculatorInput,
            todayBudget = 5.0,
            dailyBudget = 10.0,
            totalBudget = 25.0,
            billingTime = billingTime,
            lastLoginTime = lastLoginTime
        )

        val result = useCase.invoke(
            currentTimestamp = currentTime.toEpochMilli()
        ).first()

        println(result.toString())
        assertTrue(result == null)
    }

    @Test
    fun `suggest start new day 1`() = runBlocking {
        val currentTime = Instant.now()
        val billingTime = currentTime.plus(1, ChronoUnit.SECONDS)
        val lastLoginTime = currentTime.minus(1, ChronoUnit.DAYS)

        val useCase = getGetCalculatorScreenUiStateUseCase(
            calculatorInput = "123",
            todayBudget = 0.0,
            dailyBudget = 10.0,
            totalBudget = 25.0,
            billingTime = billingTime,
            lastLoginTime = lastLoginTime
        )

        val result = useCase.invoke(
            currentTimestamp = currentTime.toEpochMilli()
        ).first()

        assertIs<CalculatorScreenState.AskedToStartNewDay>(result)
        assertEquals("1", result.daysLeft)
        assertEquals("25", result.budgetLeft)
    }
}
