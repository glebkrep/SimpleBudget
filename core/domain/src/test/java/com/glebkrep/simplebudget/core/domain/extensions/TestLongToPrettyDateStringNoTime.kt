package com.glebkrep.simplebudget.core.domain.extensions

import com.glebkrep.simplebudget.core.domain.toPrettyDate
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.time.Instant
import java.time.ZoneOffset
import kotlin.test.assertEquals

class TestLongToPrettyDateStringNoTime {

    @Test
    fun `10 seconds ago test`() = runBlocking {
        val currentTimestamp = Instant.parse("2018-08-22T10:00:10Z").toEpochMilli()
        val needTime = false
        val timestamp = Instant.parse("2018-08-22T10:00:00Z").toEpochMilli()
        val expectedResult = "08.22"

        val result = timestamp.toPrettyDate(
            needTime = needTime,
            currentTimestamp = currentTimestamp,
            timeZoneId = ZoneOffset.UTC
        )

        assertEquals(expectedResult, result)
    }

    @Test
    fun `yesterday test`() = runBlocking {
        val currentTimestamp = Instant.parse("2018-08-23T10:00:00Z").toEpochMilli()
        val needTime = false
        val timestamp = Instant.parse("2018-08-22T08:00:00Z").toEpochMilli()
        val expectedResult = "08.22"

        val result = timestamp.toPrettyDate(
            needTime = needTime,
            currentTimestamp = currentTimestamp,
            timeZoneId = ZoneOffset.UTC
        )

        assertEquals(expectedResult, result)
    }


    @Test
    fun `this year`() = runBlocking {
        val currentTimestamp = Instant.parse("2018-08-23T10:00:00Z").toEpochMilli()
        val needTime = false
        val timestamp = Instant.parse("2018-01-22T08:00:00Z").toEpochMilli()
        val expectedResult = "01.22"

        val result = timestamp.toPrettyDate(
            needTime = needTime,
            currentTimestamp = currentTimestamp,
            timeZoneId = ZoneOffset.UTC
        )

        assertEquals(expectedResult, result)
    }

    @Test
    fun `not this year`() = runBlocking {
        val currentTimestamp = Instant.parse("2018-08-23T10:00:00Z").toEpochMilli()
        val needTime = false
        val timestamp = Instant.parse("2016-01-22T08:00:00Z").toEpochMilli()
        val expectedResult = "2016.01.22"

        val result = timestamp.toPrettyDate(
            needTime = needTime,
            currentTimestamp = currentTimestamp,
            timeZoneId = ZoneOffset.UTC
        )

        assertEquals(expectedResult, result)
    }

}
