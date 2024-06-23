package com.glebkrep.simplebudget.core.domain.converters

import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.test.assertEquals

class TestGetDayDiffFromTimestampUseCase {
    @Test
    fun `same day 1`() = runBlocking {
        val useCase = GetDayDiffFromTimestampUseCase()

        val start = ZonedDateTime.parse("2007-01-02T02:00:01+03:00[Europe/Moscow]")
            .toEpochSecond() * 1000
        val end = ZonedDateTime.parse("2007-01-02T04:00:01+03:00[Europe/Moscow]")
            .toEpochSecond() * 1000

        val result = useCase.invoke(start, end, ZoneId.of("Europe/Moscow"))
        assertEquals(0, result)
    }

    @Test
    fun `same day 2`() = runBlocking {
        val useCase = GetDayDiffFromTimestampUseCase()

        val start = ZonedDateTime.parse("2007-01-02T04:00:01+03:00[Europe/Moscow]")
            .toEpochSecond() * 1000
        val end = ZonedDateTime.parse("2007-01-02T06:00:01+03:00[Europe/Moscow]")
            .toEpochSecond() * 1000

        val result = useCase.invoke(start, end, ZoneId.of("Europe/Moscow"))
        assertEquals(0, result)
    }

    @Test
    fun `different day 1`() = runBlocking {
        val useCase = GetDayDiffFromTimestampUseCase()

        val start = ZonedDateTime.parse("2007-01-01T23:00:01+03:00[Europe/Moscow]")
            .toEpochSecond() * 1000
        val end = ZonedDateTime.parse("2007-01-02T00:00:01+03:00[Europe/Moscow]")
            .toEpochSecond() * 1000

        val result = useCase.invoke(start, end, ZoneId.of("Europe/Moscow"))
        assertEquals(expected = 1, actual = result)
    }

    @Test
    fun `different day 2`() = runBlocking {
        val useCase = GetDayDiffFromTimestampUseCase()

        val start = ZonedDateTime.parse("2007-01-01T23:00:01+03:00[Europe/Moscow]")
            .toEpochSecond() * 1000
        val end = ZonedDateTime.parse("2007-01-02T23:00:01+03:00[Europe/Moscow]")
            .toEpochSecond() * 1000

        val result = useCase.invoke(start, end, ZoneId.of("Europe/Moscow"))
        assertEquals(expected = 1, actual = result)
    }

    @Test
    fun `year diff`() = runBlocking {
        val useCase = GetDayDiffFromTimestampUseCase()

        val start = ZonedDateTime.parse("2007-01-01T23:00:01+03:00[Europe/Moscow]")
            .toEpochSecond() * 1000
        val end = ZonedDateTime.parse("2008-01-01T23:00:01+03:00[Europe/Moscow]")
            .toEpochSecond() * 1000

        val result = useCase.invoke(start, end, ZoneId.of("Europe/Moscow"))
        assertEquals(expected = 365, actual = result)
    }
}
