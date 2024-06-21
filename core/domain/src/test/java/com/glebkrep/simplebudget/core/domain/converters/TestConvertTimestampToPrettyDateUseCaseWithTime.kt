package com.glebkrep.simplebudget.core.domain.converters

import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.time.Instant
import java.time.ZoneOffset
import kotlin.test.assertEquals

class TestConvertTimestampToPrettyDateUseCaseWithTime {

    @Test
    fun `10 seconds ago test`() = runBlocking {
        val convertTimestampToPrettyDateUseCase = ConvertTimestampToPrettyDateUseCase()
        val currentTimestamp = Instant.parse("2018-08-22T10:00:10Z").toEpochMilli()
        val needTime = true
        val timestamp = Instant.parse("2018-08-22T10:00:00Z").toEpochMilli()
        val assumedResult = "10:00"

        val result = convertTimestampToPrettyDateUseCase(
            currentTimestamp = currentTimestamp,
            timestamp = timestamp,
            needTime = needTime,
            timeZoneId = ZoneOffset.UTC
        )

        assertEquals(assumedResult, result)
    }

    @Test
    fun `yesterday test`() = runBlocking {
        val convertTimestampToPrettyDateUseCase = ConvertTimestampToPrettyDateUseCase()
        val currentTimestamp = Instant.parse("2018-08-23T10:00:00Z").toEpochMilli()
        val needTime = true
        val timestamp = Instant.parse("2018-08-22T08:00:00Z").toEpochMilli()
        val assumedResult = "08.22 08:00"

        val result = convertTimestampToPrettyDateUseCase(
            currentTimestamp = currentTimestamp,
            timestamp = timestamp,
            needTime = needTime,
            timeZoneId = ZoneOffset.UTC
        )

        assertEquals(assumedResult, result)
    }


    @Test
    fun `this year`() = runBlocking {
        val convertTimestampToPrettyDateUseCase = ConvertTimestampToPrettyDateUseCase()
        val currentTimestamp = Instant.parse("2018-08-23T10:00:00Z").toEpochMilli()
        val needTime = true
        val timestamp = Instant.parse("2018-01-22T08:00:00Z").toEpochMilli()
        val assumedResult = "01.22"

        val result = convertTimestampToPrettyDateUseCase(
            currentTimestamp = currentTimestamp,
            timestamp = timestamp,
            needTime = needTime,
            timeZoneId = ZoneOffset.UTC
        )

        assertEquals(assumedResult, result)
    }

    @Test
    fun `not this year`() = runBlocking {
        val convertTimestampToPrettyDateUseCase = ConvertTimestampToPrettyDateUseCase()
        val currentTimestamp = Instant.parse("2018-08-23T10:00:00Z").toEpochMilli()
        val needTime = true
        val timestamp = Instant.parse("2016-01-22T08:00:00Z").toEpochMilli()
        val assumedResult = "2016.01.22"

        val result = convertTimestampToPrettyDateUseCase(
            currentTimestamp = currentTimestamp,
            timestamp = timestamp,
            needTime = needTime,
            timeZoneId = ZoneOffset.UTC
        )

        assertEquals(assumedResult, result)
    }

}
