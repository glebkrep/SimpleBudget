package com.glebkrep.simplebudget.core.domain.extensions

import com.glebkrep.simplebudget.core.domain.smartToDouble
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class TestStringSmartToDouble {
    @Test
    fun `test 1`() = runBlocking {
        assertEquals(27942.42, "27942.42".smartToDouble())
        assertNotEquals(27942.41, "27942.42".smartToDouble())
    }

    @Test
    fun `test 2`() = runBlocking {
        assertEquals(100000000.0, "100000000".smartToDouble())
    }
}
