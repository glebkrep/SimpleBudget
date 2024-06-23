package com.glebkrep.simplebudget.core.domain.converters

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class GetDayDiffFromTimestampUseCase @Inject constructor() {
    operator fun invoke(
        firstTimestamp: Long,
        secondTimestamp: Long,
        timeZoneId: ZoneId = ZoneId.systemDefault()
    ): Int {
        val firstZonedDateTime =
            ZonedDateTime.ofInstant(Instant.ofEpochMilli(firstTimestamp), timeZoneId)
                .truncatedTo(ChronoUnit.DAYS)
        val secondZonedDateTime =
            ZonedDateTime.ofInstant(Instant.ofEpochMilli(secondTimestamp), timeZoneId)
                .truncatedTo(ChronoUnit.DAYS)

        return ChronoUnit.DAYS.between(firstZonedDateTime, secondZonedDateTime).toInt()
    }
}
