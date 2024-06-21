package com.glebkrep.simplebudget.core.domain.converters

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ConvertTimestampToPrettyDateUseCase @Inject constructor() {

    suspend operator fun invoke(
        timestamp: Long,
        needTime: Boolean,
        currentTimestamp: Long = System.currentTimeMillis(),
        timeZoneId: ZoneId = ZoneId.systemDefault()
    ): String {
        val eventTime = Instant.ofEpochMilli(timestamp)
            .atZone(timeZoneId)
        val currentTime = Instant.ofEpochMilli(currentTimestamp)
            .atZone(timeZoneId)

        val isSameDay =
            eventTime.toLocalDate().atStartOfDay() == currentTime.toLocalDate().atStartOfDay()
        val isThisMonth = eventTime.year == currentTime.year && eventTime.month == currentTime.month
        val isThisYear = eventTime.year == currentTime.year

        return when {
            isSameDay -> {
                eventTime.format(
                    DateTimeFormatter.ofPattern(
                        if (needTime)
                            "HH:mm"
                        else
                            "MM.dd",
                    )
                )
            }

            isThisMonth -> {
                eventTime.format(
                    DateTimeFormatter.ofPattern(
                        if (needTime)
                            "MM.dd HH:mm"
                        else
                            "MM.dd",
                    )
                )
            }

            isThisYear -> {
                eventTime.format(
                    DateTimeFormatter.ofPattern(
                        "MM.dd",
                    )
                )
            }

            else -> {
                eventTime.format(
                    DateTimeFormatter.ofPattern(
                        "yyyy.MM.dd",
                    )
                )
            }
        }
    }
}
