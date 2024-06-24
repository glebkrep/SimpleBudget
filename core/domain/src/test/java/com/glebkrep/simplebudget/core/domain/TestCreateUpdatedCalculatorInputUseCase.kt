package com.glebkrep.simplebudget.core.domain

import com.glebkrep.simplebudget.core.domain.usecases.internal.CreateUpdatedCalculatorInputUseCase
import com.glebkrep.simplebudget.model.CalculatorButton
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class TestCreateUpdatedCalculatorInputUseCase {
    @Test
    fun `input test minus 1`() = runBlocking {
        val useCase = CreateUpdatedCalculatorInputUseCase()
        val currentInput = "+1"
        val button = CalculatorButton.MINUS

        val result = useCase.invoke(
            calculatorInput = currentInput,
            newButton = button
        )
        assertEquals("1", result)
    }

    @Test
    fun `input test minus 2`() = runBlocking {
        val useCase = CreateUpdatedCalculatorInputUseCase()
        val currentInput = "0"
        val button = CalculatorButton.MINUS

        val result = useCase.invoke(
            calculatorInput = currentInput,
            newButton = button
        )
        assertEquals("0", result)
    }

    @Test
    fun `input test plus 1`() = runBlocking {
        val useCase = CreateUpdatedCalculatorInputUseCase()
        val currentInput = "0"
        val button = CalculatorButton.PLUS

        val result = useCase.invoke(
            calculatorInput = currentInput,
            newButton = button
        )
        assertEquals("+0", result)
    }

    @Test
    fun `input test plus 2`() = runBlocking {
        val useCase = CreateUpdatedCalculatorInputUseCase()
        val currentInput = "+0"
        val button = CalculatorButton.PLUS

        val result = useCase.invoke(
            calculatorInput = currentInput,
            newButton = button
        )
        assertEquals("+0", result)
    }

    @Test
    fun `input test plus 3`() = runBlocking {
        val useCase = CreateUpdatedCalculatorInputUseCase()
        val currentInput = "13.5"
        val button = CalculatorButton.PLUS

        val result = useCase.invoke(
            calculatorInput = currentInput,
            newButton = button
        )
        assertEquals("+13.5", result)
    }

    @Test
    fun `input test back 1`() = runBlocking {
        val useCase = CreateUpdatedCalculatorInputUseCase()
        val currentInput = "13.5"
        val button = CalculatorButton.BACK

        val result = useCase.invoke(
            calculatorInput = currentInput,
            newButton = button
        )
        assertEquals("13.", result)
    }

    @Test
    fun `input test back 2`() = runBlocking {
        val useCase = CreateUpdatedCalculatorInputUseCase()
        val currentInput = "13"
        val button = CalculatorButton.BACK

        val result = useCase.invoke(
            calculatorInput = currentInput,
            newButton = button
        )
        assertEquals("1", result)
    }

    @Test
    fun `input test back 3`() = runBlocking {
        val useCase = CreateUpdatedCalculatorInputUseCase()
        val currentInput = "1"
        val button = CalculatorButton.BACK

        val result = useCase.invoke(
            calculatorInput = currentInput,
            newButton = button
        )
        assertEquals("0", result)
    }

    @Test
    fun `input test back 4`() = runBlocking {
        val useCase = CreateUpdatedCalculatorInputUseCase()
        val currentInput = "+1"
        val button = CalculatorButton.BACK

        val result = useCase.invoke(
            calculatorInput = currentInput,
            newButton = button
        )
        assertEquals("+0", result)
    }

    @Test
    fun `input test dot 1`() = runBlocking {
        val useCase = CreateUpdatedCalculatorInputUseCase()
        val currentInput = "+1"
        val button = CalculatorButton.DOT

        val result = useCase.invoke(
            calculatorInput = currentInput,
            newButton = button
        )
        assertEquals("+1.", result)
    }

    @Test
    fun `input test dot 2`() = runBlocking {
        val useCase = CreateUpdatedCalculatorInputUseCase()
        val currentInput = "+1."
        val button = CalculatorButton.DOT

        val result = useCase.invoke(
            calculatorInput = currentInput,
            newButton = button
        )
        assertEquals("+1.", result)
    }

    @Test
    fun `input test dot 3`() = runBlocking {
        val useCase = CreateUpdatedCalculatorInputUseCase()
        val currentInput = "1"
        val button = CalculatorButton.DOT

        val result = useCase.invoke(
            calculatorInput = currentInput,
            newButton = button
        )
        assertEquals("1.", result)
    }

    @Test
    fun `input test dot 4`() = runBlocking {
        val useCase = CreateUpdatedCalculatorInputUseCase()
        val currentInput = "0"
        val button = CalculatorButton.DOT

        val result = useCase.invoke(
            calculatorInput = currentInput,
            newButton = button
        )
        assertEquals("0.", result)
    }

    @Test
    fun `input test digit 1`() = runBlocking {
        val useCase = CreateUpdatedCalculatorInputUseCase()
        val currentInput = "0"
        val button = CalculatorButton.ONE

        val result = useCase.invoke(
            calculatorInput = currentInput,
            newButton = button
        )
        assertEquals("1", result)
    }

    @Test
    fun `input test digit 2`() = runBlocking {
        val useCase = CreateUpdatedCalculatorInputUseCase()
        val currentInput = "+0"
        val button = CalculatorButton.ONE

        val result = useCase.invoke(
            calculatorInput = currentInput,
            newButton = button
        )
        assertEquals("+1", result)
    }

    @Test
    fun `input test digit 3`() = runBlocking {
        val useCase = CreateUpdatedCalculatorInputUseCase()
        val currentInput = "0."
        val button = CalculatorButton.ONE

        val result = useCase.invoke(
            calculatorInput = currentInput,
            newButton = button
        )
        assertEquals("0.1", result)
    }

    @Test
    fun `input test digit 4`() = runBlocking {
        val useCase = CreateUpdatedCalculatorInputUseCase()
        val currentInput = "0.1"
        val button = CalculatorButton.ONE

        val result = useCase.invoke(
            calculatorInput = currentInput,
            newButton = button
        )
        assertEquals("0.11", result)
    }

    @Test
    fun `input test digit 5`() = runBlocking {
        val useCase = CreateUpdatedCalculatorInputUseCase()
        val currentInput = "0.11"
        val button = CalculatorButton.ONE

        val result = useCase.invoke(
            calculatorInput = currentInput,
            newButton = button
        )
        assertEquals("0.11", result)
    }
}
