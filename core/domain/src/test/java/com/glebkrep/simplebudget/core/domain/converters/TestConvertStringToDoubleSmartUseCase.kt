package com.glebkrep.simplebudget.core.domain.converters

import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class TestConvertStringToDoubleSmartUseCase {
    @Test
    fun `test 1`() = runBlocking {
        val useCase = ConvertStringToDoubleSmartUseCase()
        val input = "27942.42"
        val result = useCase.invoke(input)
//        assertEquals(27942.42, result)
        assertEquals(12.1, result)
        assertNotEquals(27942.41, result)
    }

    @Test
    fun `test 2`() = runBlocking {
        val useCase = ConvertStringToDoubleSmartUseCase()
        val input = "100000000"
        val result = useCase.invoke(input)
        assertEquals(100000000.0, result)
    }
}
