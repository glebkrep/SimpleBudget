package com.glebkrep.simplebudget.core.domain

import com.glebkrep.simplebudget.core.domain.models.BudgetDataOperations
import com.glebkrep.simplebudget.core.domain.usecases.internal.CreateUpdatedBudgetDataUseCase
import com.glebkrep.simplebudget.model.BudgetData
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.test.assertEquals

@Suppress("LargeClass")
class TestCreateUpdatedBudgetDataUseCase {

    @Test
    fun `new total budget 1`() = runBlocking {
        val useCase = CreateUpdatedBudgetDataUseCase()
        val currentTime = Instant.now()
        val billingTimeStamp = currentTime
            .plus(1, ChronoUnit.DAYS)
        val budgetData = BudgetData(
            todayBudget = 0.0,
            dailyBudget = 0.0,
            totalLeft = 0.0,
            billingTimestamp = billingTimeStamp.toEpochMilli(),
            lastLoginTimestamp = 0L,
            lastBillingUpdateTimestamp = 0L
        )
        val operation = BudgetDataOperations.NewTotalBudget("100")

        val result = useCase.invoke(
            operation = operation,
            budgetData = budgetData
        )

        assertEquals(100.0, result.totalLeft)
        assertEquals(50.0, result.dailyBudget)
        assertEquals(50.0, result.todayBudget)
    }

    @Test
    fun `new total budget 2`() = runBlocking {
        val useCase = CreateUpdatedBudgetDataUseCase()
        val currentTime = Instant.now()
        val billingTimeStamp = currentTime
            .plus(30, ChronoUnit.DAYS)
        val budgetData = BudgetData(
            todayBudget = 0.0,
            dailyBudget = 0.0,
            totalLeft = 0.0,
            billingTimestamp = billingTimeStamp.toEpochMilli(),
            lastLoginTimestamp = 0L,
            lastBillingUpdateTimestamp = 0L
        )
        val operation = BudgetDataOperations.NewTotalBudget("31")

        val result = useCase.invoke(
            operation = operation,
            budgetData = budgetData
        )

        assertEquals(31.0, result.totalLeft)
        assertEquals(1.0, result.dailyBudget)
        assertEquals(1.0, result.todayBudget)
    }

    @Test
    fun `new total budget 3`() = runBlocking {
        val useCase = CreateUpdatedBudgetDataUseCase()
        val currentTime = Instant.now()
        // todo: rewrite so that this test doesn't depend on the current timezone
        val billingTimeStamp = currentTime
            .plus(1, ChronoUnit.MILLIS)
        val budgetData = BudgetData(
            todayBudget = 0.0,
            dailyBudget = 0.0,
            totalLeft = 0.0,
            billingTimestamp = billingTimeStamp.toEpochMilli(),
            lastLoginTimestamp = 0L,
            lastBillingUpdateTimestamp = 0L
        )
        val operation = BudgetDataOperations.NewTotalBudget("1")

        val result = useCase.invoke(
            operation = operation,
            budgetData = budgetData
        )

        assertEquals(1.0, result.totalLeft)
        assertEquals(1.0, result.dailyBudget)
        assertEquals(1.0, result.todayBudget)
    }

    @Test
    fun `new total budget 4`() = runBlocking {
        val useCase = CreateUpdatedBudgetDataUseCase()
        val currentTime = Instant.now()
        // todo: rewrite so that this test doesn't depend on the current timezone
        val billingTimeStamp = currentTime
            .plus(1, ChronoUnit.MILLIS)
        val budgetData = BudgetData(
            todayBudget = 0.0,
            dailyBudget = 0.0,
            totalLeft = 0.0,
            billingTimestamp = billingTimeStamp.toEpochMilli(),
            lastLoginTimestamp = 0L,
            lastBillingUpdateTimestamp = 0L
        )
        val operation = BudgetDataOperations.NewTotalBudget("27942.42")

        val result = useCase.invoke(
            operation = operation,
            budgetData = budgetData
        )

        assertEquals(27942.42, result.totalLeft)
        assertEquals(27942.42, result.dailyBudget)
        assertEquals(27942.42, result.todayBudget)
    }


    @Test
    fun `new billing date 1`() = runBlocking {
        val useCase = CreateUpdatedBudgetDataUseCase()
        val currentTime = Instant.now()
        val billingTimeStamp = currentTime
            .plus(30, ChronoUnit.DAYS)
        val budgetData = BudgetData(
            todayBudget = 9999.0,
            dailyBudget = 9999.0,
            totalLeft = 31.0,
            billingTimestamp = billingTimeStamp.toEpochMilli(),
            lastLoginTimestamp = 0L,
            lastBillingUpdateTimestamp = 0L
        )
        val operation = BudgetDataOperations.NewBillingDate(billingTimeStamp.toEpochMilli())

        val result = useCase.invoke(
            operation = operation,
            budgetData = budgetData
        )

        assertEquals(31.0, result.totalLeft)
        assertEquals(1.0, result.dailyBudget)
        assertEquals(1.0, result.todayBudget)
    }


    @Test
    fun `transfer leftover to today 1`() = runBlocking {
        val useCase = CreateUpdatedBudgetDataUseCase()
        val currentTime = Instant.now()
        val billingTimeStamp = currentTime
            .plus(2, ChronoUnit.DAYS)
        val budgetData = BudgetData(
            todayBudget = 7.0,
            dailyBudget = 10.0,
            totalLeft = 107.0,
            billingTimestamp = billingTimeStamp.toEpochMilli(),
            lastLoginTimestamp = 0L,
            lastBillingUpdateTimestamp = 0L
        )
        val operation = BudgetDataOperations.TransferLeftoverTodayToToday

        val result = useCase.invoke(
            operation = operation,
            budgetData = budgetData
        )

        assertEquals(107.0, result.totalLeft)
        assertEquals(10.0, result.dailyBudget)
        assertEquals(87.0, result.todayBudget)
    }

    @Test
    fun `transfer leftover to today 2`() = runBlocking {
        val useCase = CreateUpdatedBudgetDataUseCase()
        val currentTime = Instant.now()
        val billingTimeStamp = currentTime
            .plus(2, ChronoUnit.DAYS)
        val budgetData = BudgetData(
            todayBudget = 7.0,
            dailyBudget = 10.0,
            totalLeft = 37.0,
            billingTimestamp = billingTimeStamp.toEpochMilli(),
            lastLoginTimestamp = 0L,
            lastBillingUpdateTimestamp = 0L
        )
        val operation = BudgetDataOperations.TransferLeftoverTodayToToday

        val result = useCase.invoke(
            operation = operation,
            budgetData = budgetData
        )

        assertEquals(37.0, result.totalLeft)
        assertEquals(10.0, result.dailyBudget)
        assertEquals(17.0, result.todayBudget)
    }

    @Test
    fun `transfer leftover to today 3`() = runBlocking {
        val useCase = CreateUpdatedBudgetDataUseCase()
        val currentTime = Instant.now()
        val billingTimeStamp = currentTime
            .plus(0, ChronoUnit.DAYS)
        val budgetData = BudgetData(
            todayBudget = 10.0,
            dailyBudget = 10.0,
            totalLeft = 20.0,
            billingTimestamp = billingTimeStamp.toEpochMilli(),
            lastLoginTimestamp = 0L,
            lastBillingUpdateTimestamp = 0L
        )
        val operation = BudgetDataOperations.TransferLeftoverTodayToToday

        val result = useCase.invoke(
            operation = operation,
            budgetData = budgetData
        )

        assertEquals(20.0, result.totalLeft)
        assertEquals(20.0, result.dailyBudget)
        assertEquals(20.0, result.todayBudget)
    }


    @Test
    fun `transfer leftover to daily 1`() = runBlocking {
        val useCase = CreateUpdatedBudgetDataUseCase()
        val currentTime = Instant.now()
        val billingTimeStamp = currentTime
            .plus(2, ChronoUnit.DAYS)
        val budgetData = BudgetData(
            todayBudget = 7.0,
            dailyBudget = 10.0,
            totalLeft = 37.0,
            billingTimestamp = billingTimeStamp.toEpochMilli(),
            lastLoginTimestamp = 0L,
            lastBillingUpdateTimestamp = 0L
        )
        val operation = BudgetDataOperations.TransferLeftoverTodayToDaily

        val result = useCase.invoke(
            operation = operation,
            budgetData = budgetData
        )

        assertEquals(37.0, result.totalLeft)
        assertEquals(12.33, result.dailyBudget)
        assertEquals(12.33, result.todayBudget)
    }

    @Test
    fun `transfer leftover to daily 2`() = runBlocking {
        val useCase = CreateUpdatedBudgetDataUseCase()
        val currentTime = Instant.now()
        val billingTimeStamp = currentTime
            .plus(30, ChronoUnit.DAYS)
        val budgetData = BudgetData(
            todayBudget = 0.50,
            dailyBudget = 1010.11,
            totalLeft = 100000.50,
            billingTimestamp = billingTimeStamp.toEpochMilli(),
            lastLoginTimestamp = 0L,
            lastBillingUpdateTimestamp = 0L
        )
        val operation = BudgetDataOperations.TransferLeftoverTodayToDaily

        val result = useCase.invoke(
            operation = operation,
            budgetData = budgetData
        )

        assertEquals(100000.50, result.totalLeft)
        assertEquals(3225.82, result.dailyBudget)
        assertEquals(3225.82, result.todayBudget)
    }

    @Test
    fun `transfer leftover to daily 3`() = runBlocking {
        val useCase = CreateUpdatedBudgetDataUseCase()
        val currentTime = Instant.now()
        val billingTimeStamp = currentTime
            .plus(2, ChronoUnit.DAYS)
        val budgetData = BudgetData(
            todayBudget = 10.0,
            dailyBudget = 10.0,
            totalLeft = 40.0,
            billingTimestamp = billingTimeStamp.toEpochMilli(),
            lastLoginTimestamp = 0L,
            lastBillingUpdateTimestamp = 0L
        )
        val operation = BudgetDataOperations.TransferLeftoverTodayToDaily

        val result = useCase.invoke(
            operation = operation,
            budgetData = budgetData
        )

        assertEquals(40.0, result.totalLeft)
        assertEquals(13.33, result.dailyBudget)
        assertEquals(13.33, result.todayBudget)
    }

    @Test
    fun `transfer leftover to daily 4`() = runBlocking {
        val useCase = CreateUpdatedBudgetDataUseCase()
        val currentTime = Instant.now()
        val billingTimeStamp = currentTime
            .plus(0, ChronoUnit.DAYS)
        val budgetData = BudgetData(
            todayBudget = 10.0,
            dailyBudget = 10.0,
            totalLeft = 20.0,
            billingTimestamp = billingTimeStamp.toEpochMilli(),
            lastLoginTimestamp = 0L,
            lastBillingUpdateTimestamp = 0L
        )
        val operation = BudgetDataOperations.TransferLeftoverTodayToDaily

        val result = useCase.invoke(
            operation = operation,
            budgetData = budgetData
        )

        assertEquals(20.0, result.totalLeft)
        assertEquals(20.0, result.dailyBudget)
        assertEquals(20.0, result.todayBudget)
    }

    @Test
    fun `handle calculator input plus 1`() = runBlocking {
        val useCase = CreateUpdatedBudgetDataUseCase()
        val currentTime = Instant.now()
        val billingTimeStamp = currentTime
            .plus(2, ChronoUnit.DAYS)
        val budgetData = BudgetData(
            todayBudget = 0.0,
            dailyBudget = 10.0,
            totalLeft = 20.0,
            billingTimestamp = billingTimeStamp.toEpochMilli(),
            lastLoginTimestamp = 0L,
            lastBillingUpdateTimestamp = 0L
        )
        val calculatorInput = "+10"
        val operation = BudgetDataOperations.HandleCalculatorInput(calculatorInput)

        val result = useCase.invoke(
            operation = operation,
            budgetData = budgetData
        )

        assertEquals(30.0, result.totalLeft)
        assertEquals(10.0, result.dailyBudget)
        assertEquals(10.0, result.todayBudget)
    }

    @Test
    fun `handle calculator input plus 2`() = runBlocking {
        val useCase = CreateUpdatedBudgetDataUseCase()
        val currentTime = Instant.now()
        val billingTimeStamp = currentTime
            .plus(2, ChronoUnit.DAYS)
        val budgetData = BudgetData(
            todayBudget = 8.0,
            dailyBudget = 10.0,
            totalLeft = 28.0,
            billingTimestamp = billingTimeStamp.toEpochMilli(),
            lastLoginTimestamp = 0L,
            lastBillingUpdateTimestamp = 0L
        )
        val calculatorInput = "+12"
        val operation = BudgetDataOperations.HandleCalculatorInput(calculatorInput)

        val result = useCase.invoke(
            operation = operation,
            budgetData = budgetData
        )

        assertEquals(40.0, result.totalLeft)
        assertEquals(13.33, result.dailyBudget)
        assertEquals(13.33, result.todayBudget)
    }

    @Test
    fun `handle calculator input plus 3`() = runBlocking {
        val useCase = CreateUpdatedBudgetDataUseCase()
        val currentTime = Instant.now()
        val billingTimeStamp = currentTime
            .plus(2, ChronoUnit.DAYS)
        val budgetData = BudgetData(
            todayBudget = 0.0,
            dailyBudget = 10.0,
            totalLeft = 20.0,
            billingTimestamp = billingTimeStamp.toEpochMilli(),
            lastLoginTimestamp = 0L,
            lastBillingUpdateTimestamp = 0L
        )
        val calculatorInput = "+9"
        val operation = BudgetDataOperations.HandleCalculatorInput(calculatorInput)

        val result = useCase.invoke(
            operation = operation,
            budgetData = budgetData
        )

        assertEquals(29.0, result.totalLeft)
        assertEquals(10.0, result.dailyBudget)
        assertEquals(9.0, result.todayBudget)
    }

    @Test
    fun `handle calculator input plus 4`() = runBlocking {
        val useCase = CreateUpdatedBudgetDataUseCase()
        val currentTime = Instant.now()
        val billingTimeStamp = currentTime
            .plus(2, ChronoUnit.DAYS)
        val budgetData = BudgetData(
            todayBudget = 0.0,
            dailyBudget = 10.0,
            totalLeft = 20.0,
            billingTimestamp = billingTimeStamp.toEpochMilli(),
            lastLoginTimestamp = 0L,
            lastBillingUpdateTimestamp = 0L
        )
        val calculatorInput = "+0"
        val operation = BudgetDataOperations.HandleCalculatorInput(calculatorInput)

        val result = useCase.invoke(
            operation = operation,
            budgetData = budgetData
        )

        assertEquals(20.0, result.totalLeft)
        assertEquals(10.0, result.dailyBudget)
        assertEquals(0.0, result.todayBudget)
    }

    @Test
    fun `handle calculator input minus 1`() = runBlocking {
        val useCase = CreateUpdatedBudgetDataUseCase()
        val currentTime = Instant.now()
        val billingTimeStamp = currentTime
            .plus(2, ChronoUnit.DAYS)
        val budgetData = BudgetData(
            todayBudget = 10.0,
            dailyBudget = 10.0,
            totalLeft = 30.0,
            billingTimestamp = billingTimeStamp.toEpochMilli(),
            lastLoginTimestamp = 0L,
            lastBillingUpdateTimestamp = 0L
        )
        val calculatorInput = "10"
        val operation = BudgetDataOperations.HandleCalculatorInput(calculatorInput)

        val result = useCase.invoke(
            operation = operation,
            budgetData = budgetData
        )

        assertEquals(20.0, result.totalLeft)
        assertEquals(10.0, result.dailyBudget)
        assertEquals(0.0, result.todayBudget)
    }

    @Test
    fun `handle calculator input minus 2`() = runBlocking {
        val useCase = CreateUpdatedBudgetDataUseCase()
        val currentTime = Instant.now()
        val billingTimeStamp = currentTime
            .plus(2, ChronoUnit.DAYS)
        val budgetData = BudgetData(
            todayBudget = 10.0,
            dailyBudget = 10.0,
            totalLeft = 30.0,
            billingTimestamp = billingTimeStamp.toEpochMilli(),
            lastLoginTimestamp = 0L,
            lastBillingUpdateTimestamp = 0L
        )
        val calculatorInput = "9"
        val operation = BudgetDataOperations.HandleCalculatorInput(calculatorInput)

        val result = useCase.invoke(
            operation = operation,
            budgetData = budgetData
        )

        assertEquals(21.0, result.totalLeft)
        assertEquals(10.0, result.dailyBudget)
        assertEquals(1.0, result.todayBudget)
    }

    @Test
    fun `handle calculator input minus 3`() = runBlocking {
        val useCase = CreateUpdatedBudgetDataUseCase()
        val currentTime = Instant.now()
        val billingTimeStamp = currentTime
            .plus(2, ChronoUnit.DAYS)
        val budgetData = BudgetData(
            todayBudget = 10.0,
            dailyBudget = 10.0,
            totalLeft = 30.0,
            billingTimestamp = billingTimeStamp.toEpochMilli(),
            lastLoginTimestamp = 0L,
            lastBillingUpdateTimestamp = 0L
        )
        val calculatorInput = "20"
        val operation = BudgetDataOperations.HandleCalculatorInput(calculatorInput)

        val result = useCase.invoke(
            operation = operation,
            budgetData = budgetData
        )

        assertEquals(10.0, result.totalLeft)
        assertEquals(5.0, result.dailyBudget)
        assertEquals(0.0, result.todayBudget)
    }

    @Test
    fun `handle calculator input minus 4`() = runBlocking {
        val useCase = CreateUpdatedBudgetDataUseCase()
        val currentTime = Instant.now()
        val billingTimeStamp = currentTime
            .plus(2, ChronoUnit.DAYS)
        val budgetData = BudgetData(
            todayBudget = 10.0,
            dailyBudget = 10.0,
            totalLeft = 30.0,
            billingTimestamp = billingTimeStamp.toEpochMilli(),
            lastLoginTimestamp = 0L,
            lastBillingUpdateTimestamp = 0L
        )
        val calculatorInput = "0"
        val operation = BudgetDataOperations.HandleCalculatorInput(calculatorInput)

        val result = useCase.invoke(
            operation = operation,
            budgetData = budgetData
        )

        assertEquals(30.0, result.totalLeft)
        assertEquals(10.0, result.dailyBudget)
        assertEquals(10.0, result.todayBudget)
    }

    @Test
    fun `handle calculator input minus 5`() = runBlocking {
        val useCase = CreateUpdatedBudgetDataUseCase()
        val currentTime = Instant.now()
        val billingTimeStamp = currentTime
            .plus(0, ChronoUnit.DAYS)
        val budgetData = BudgetData(
            todayBudget = 10.0,
            dailyBudget = 10.0,
            totalLeft = 10.0,
            billingTimestamp = billingTimeStamp.toEpochMilli(),
            lastLoginTimestamp = 0L,
            lastBillingUpdateTimestamp = 0L
        )
        val calculatorInput = "15"
        val operation = BudgetDataOperations.HandleCalculatorInput(calculatorInput)

        val result = useCase.invoke(
            operation = operation,
            budgetData = budgetData
        )

        assertEquals(0.0, result.totalLeft)
        assertEquals(0.0, result.dailyBudget)
        assertEquals(0.0, result.todayBudget)
    }

    @Test
    fun `handle calculator input minus 6`() = runBlocking {
        val useCase = CreateUpdatedBudgetDataUseCase()
        val currentTime = Instant.now()
        val billingTimeStamp = currentTime
            .plus(0, ChronoUnit.DAYS)
        val budgetData = BudgetData(
            todayBudget = 10.0,
            dailyBudget = 10.0,
            totalLeft = 10.0,
            billingTimestamp = billingTimeStamp.toEpochMilli(),
            lastLoginTimestamp = 0L,
            lastBillingUpdateTimestamp = 0L
        )
        val calculatorInput = "5"
        val operation = BudgetDataOperations.HandleCalculatorInput(calculatorInput)

        val result = useCase.invoke(
            operation = operation,
            budgetData = budgetData
        )

        assertEquals(5.0, result.totalLeft)
        assertEquals(5.0, result.dailyBudget)
        assertEquals(5.0, result.todayBudget)
    }
}
