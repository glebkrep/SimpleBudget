package com.glebkrep.simplebudget.core.domain

import com.glebkrep.simplebudget.core.data.data.repositories.budgetData.BudgetRepository
import com.glebkrep.simplebudget.core.data.data.repositories.calculatorInput.CalculatorInputRepository
import com.glebkrep.simplebudget.core.data.data.repositories.preferences.PreferencesRepository
import com.glebkrep.simplebudget.core.data.data.repositories.recentTransactions.RecentTransactionsRepository
import com.glebkrep.simplebudget.core.domain.models.CalculatorState
import com.glebkrep.simplebudget.core.domain.models.RecentTransaction
import com.glebkrep.simplebudget.core.domain.models.toEntity
import com.glebkrep.simplebudget.core.domain.usecases.CreateUpdatedBudgetDataUseCase
import com.glebkrep.simplebudget.core.domain.usecases.GetCalculatorStateUseCase
import com.glebkrep.simplebudget.model.AppPreferences
import com.glebkrep.simplebudget.model.BudgetData
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
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

class TestGetCalculatorStateUseCase {

    @Suppress("LongParameterList", "SameParameterValue")
    private fun getGetCalculatorScreenUiStateUseCase(
        calculatorInput: String,
        todayBudget: Double,
        dailyBudget: Double,
        totalBudget: Double,
        billingTime: Instant,
        lastLoginTime: Instant
    ): GetCalculatorStateUseCase {
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
        coEvery { recentTransactionRepository.getRecentTransactions() } returns flow {
            emit(
                listOf(
                    RecentTransaction(
                        date = System.currentTimeMillis(),
                        sum = 1.0,
                        isPlusOperation = false,
                        comment = null
                    ).toEntity()
                )
            )
        }
        coEvery { recentTransactionRepository.getTotalNumberOfRecentTransactionsFlow() } returns flow {
            emit(1)
        }

        val preferenceRepository = mockk<PreferencesRepository>()
        coEvery { preferenceRepository.getPreferences() } returns flow {
            emit(AppPreferences(isCommentsEnabled = true))
        }

        return GetCalculatorStateUseCase(
            budgetRepository = budgetRepository,
            calculatorInputRepository = calculatorInputRepository,
            recentTransactionsRepository = recentTransactionRepository,
            createUpdatedBudgetDataUseCase = CreateUpdatedBudgetDataUseCase(),
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

        assertIs<CalculatorState.NeedToTransferTodayRemainder>(result)
        assertEquals("10", result.budgetData.dailyBudget.toPrettyString())
        assertEquals("12.5", result.dailyOption.dailyBudget.toPrettyString())
        assertEquals("10", result.budgetData.dailyBudget.toPrettyString())
        assertEquals("15", result.todayOption.todayBudget.toPrettyString())
        assertEquals(
            "2",
            (currentTime.toEpochMilli()
                .dayDiffTo(result.budgetData.billingTimestamp) + 1).toString()
        )
        assertEquals("25", result.budgetData.totalLeft.toPrettyString())
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

        assertIs<CalculatorState.NeedToTransferTodayRemainder>(
            result
        )
        assertEquals("10", result.budgetData.dailyBudget.toPrettyString())
        assertEquals("25", result.dailyOption.dailyBudget.toPrettyString())
        assertEquals("10", result.budgetData.dailyBudget.toPrettyString())
        assertEquals("25", result.todayOption.todayBudget.toPrettyString())
        assertEquals(
            "1",
            (currentTime.toEpochMilli()
                .dayDiffTo(result.budgetData.billingTimestamp) + 1).toString()
        )
        assertEquals("25", result.budgetData.totalLeft.toPrettyString())
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
        assertIs<CalculatorState.InvalidBillingDate>(
            result
        )
        assertEquals("25", result.budgetData.totalLeft.toPrettyString())
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
        assertIs<CalculatorState.InvalidBillingDate>(
            result
        )
        assertEquals("25", result.budgetData.totalLeft.toPrettyString())
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
        assertIs<CalculatorState.Default>(
            result
        )
        assertEquals(calculatorInput, result.currentInput)
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
        assertIs<CalculatorState.Default>(
            result
        )
        assertEquals(calculatorInput, result.currentInput)
        assertEquals(
            BigDecimal(10000025.0).toPlainString(),
            result.budgetDataPreview?.totalLeft?.toPrettyString()
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
        assertIs<CalculatorState.Default>(
            result
        )
        assertEquals(calculatorInput, result.currentInput)
        assertEquals(
            "0",
            result.budgetDataPreview?.totalLeft?.toPrettyString()
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

        assertIs<CalculatorState.NeedToStartNewDay>(
            result
        )
        assertEquals(
            "1",
            (currentTime.toEpochMilli()
                .dayDiffTo(result.budgetData.billingTimestamp) + 1).toString()
        )
        assertEquals("25", result.budgetData.totalLeft.toPrettyString())
    }
}
