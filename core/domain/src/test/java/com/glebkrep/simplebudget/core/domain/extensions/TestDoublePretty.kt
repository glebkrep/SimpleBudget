package com.glebkrep.simplebudget.core.domain.extensions

import com.glebkrep.simplebudget.core.domain.rounded
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class TestDoublePretty {

    @Test
    fun `test 1`() = runBlocking {
        assertEquals(27942.42, 27942.42.rounded)
    }

    @Test
    fun `test 2`() = runBlocking {
        assertEquals(27942.41, 27942.41.rounded)
    }

    @Test
    fun `test 3`() = runBlocking {
        assertEquals(27942.49, 27942.49.rounded)
    }

    @Test
    fun `test 4`() = runBlocking {
        assertEquals(0.33, 0.33333.rounded)
    }

    @Test
    fun `test 5`() = runBlocking {
        assertEquals(0.42, 0.42.rounded)
    }

    @Test
    fun `test 6`() = runBlocking {
        assertEquals(0.01, 0.01.rounded)
    }

    @Test
    fun `test 7`() = runBlocking {
        assertEquals(0.02, 0.02.rounded)
    }

    @Test
    fun `test 8`() = runBlocking {
        assertEquals(1.0, 0.9999.rounded)
    }

    @Test
    fun `test 9`() = runBlocking {
        assertEquals(12.56, 12.5555.rounded)
    }

    @Test
    fun `test 10`() = runBlocking {
        assertEquals(100000000.0, 100000000.0.rounded)
    }
}
