package com.glebkrep.simplebudget.core.domain.extensions

import com.glebkrep.simplebudget.core.domain.dayDiffTo
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.test.assertEquals

class TestLongDayDiffTo {
    @Test
    fun `same day 1`() = runBlocking {
        val start = ZonedDateTime.parse("2007-01-02T02:00:01+03:00[Europe/Moscow]")
            .toEpochSecond() * 1000
        val end = ZonedDateTime.parse("2007-01-02T04:00:01+03:00[Europe/Moscow]")
            .toEpochSecond() * 1000

        assertEquals(0, start.dayDiffTo(end, ZoneId.of("Europe/Moscow")))
    }

    @Test
    fun `same day 2`() = runBlocking {
        val start = ZonedDateTime.parse("2007-01-02T04:00:01+03:00[Europe/Moscow]")
            .toEpochSecond() * 1000
        val end = ZonedDateTime.parse("2007-01-02T06:00:01+03:00[Europe/Moscow]")
            .toEpochSecond() * 1000

        assertEquals(0, start.dayDiffTo(end, ZoneId.of("Europe/Moscow")))
    }

    @Test
    fun `different day 1`() = runBlocking {
        val start = ZonedDateTime.parse("2007-01-01T23:00:01+03:00[Europe/Moscow]")
            .toEpochSecond() * 1000
        val end = ZonedDateTime.parse("2007-01-02T00:00:01+03:00[Europe/Moscow]")
            .toEpochSecond() * 1000

        assertEquals(1, start.dayDiffTo(end, ZoneId.of("Europe/Moscow")))
    }

    @Test
    fun `different day 2`() = runBlocking {
        val start = ZonedDateTime.parse("2007-01-01T23:00:01+03:00[Europe/Moscow]")
            .toEpochSecond() * 1000
        val end = ZonedDateTime.parse("2007-01-02T23:00:01+03:00[Europe/Moscow]")
            .toEpochSecond() * 1000

        assertEquals(1, start.dayDiffTo(end, ZoneId.of("Europe/Moscow")))
    }

    @Test
    fun `year diff`() = runBlocking {
        val start = ZonedDateTime.parse("2007-01-01T23:00:01+03:00[Europe/Moscow]")
            .toEpochSecond() * 1000
        val end = ZonedDateTime.parse("2008-01-01T23:00:01+03:00[Europe/Moscow]")
            .toEpochSecond() * 1000

        assertEquals(365, start.dayDiffTo(end, ZoneId.of("Europe/Moscow")))
    }
}
