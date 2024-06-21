package com.glebkrep.simplebudget.core.domain.converters

import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class TestConvertDoubleToPrettyDoubleUseCase {

    @Test
    fun `test 1`() = runBlocking {
        val useCase = ConvertDoubleToPrettyDoubleUseCase()
        val input = 27942.42

        val result = useCase.invoke(input)

        assertEquals(27942.42, result)
    }

    @Test
    fun `test 2`() = runBlocking {
        val useCase = ConvertDoubleToPrettyDoubleUseCase()
        val input = 27942.41

        val result = useCase.invoke(input)

        assertEquals(27942.41, result)
    }

    @Test
    fun `test 3`() = runBlocking {
        val useCase = ConvertDoubleToPrettyDoubleUseCase()
        val input = 27942.49

        val result = useCase.invoke(input)

        assertEquals(27942.49, result)
    }

    @Test
    fun `test 4`() = runBlocking {
        val useCase = ConvertDoubleToPrettyDoubleUseCase()
        val input = 0.33333

        val result = useCase.invoke(input)

        assertEquals(0.33, result)
    }

    @Test
    fun `test 5`() = runBlocking {
        val useCase = ConvertDoubleToPrettyDoubleUseCase()
        val input = 0.42

        val result = useCase.invoke(input)

        assertEquals(0.42, result)
    }

    @Test
    fun `test 6`() = runBlocking {
        val useCase = ConvertDoubleToPrettyDoubleUseCase()
        val input = 0.01

        val result = useCase.invoke(input)

        assertEquals(0.01, result)
    }

    @Test
    fun `test 7`() = runBlocking {
        val useCase = ConvertDoubleToPrettyDoubleUseCase()
        val input = 0.02

        val result = useCase.invoke(input)

        assertEquals(0.02, result)
    }

    @Test
    fun `test 8`() = runBlocking {
        val useCase = ConvertDoubleToPrettyDoubleUseCase()
        val input = 0.9999

        val result = useCase.invoke(input)

        assertEquals(1.0, result)
    }

    @Test
    fun `test 9`() = runBlocking {
        val useCase = ConvertDoubleToPrettyDoubleUseCase()
        val input = 12.5555

        val result = useCase.invoke(input)

        assertEquals(12.56, result)
    }

    @Test
    fun `test 10`() = runBlocking {
        val useCase = ConvertDoubleToPrettyDoubleUseCase()
        val input = 100000000.0

        val result = useCase.invoke(input)

        assertEquals(100000000.0, result)
    }
}
