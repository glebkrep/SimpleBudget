package com.glebkrep.simplebudget.core.domain

import com.glebkrep.simplebudget.model.CalculatorButton
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * Returns the difference in days between two timestamps each truncated to days
 */
fun Long.dayDiffTo(to: Long, timeZoneId: ZoneId = ZoneId.systemDefault()): Int {
    val firstZonedDateTime =
        ZonedDateTime.ofInstant(Instant.ofEpochMilli(this), timeZoneId)
            .truncatedTo(ChronoUnit.DAYS)
    val secondZonedDateTime =
        ZonedDateTime.ofInstant(Instant.ofEpochMilli(to), timeZoneId)
            .truncatedTo(ChronoUnit.DAYS)

    return ChronoUnit.DAYS.between(firstZonedDateTime, secondZonedDateTime).toInt()
}

fun Long.toPrettyDate(
    needTime: Boolean = false,
    currentTimestamp: Long = System.currentTimeMillis(),
    timeZoneId: ZoneId = ZoneId.systemDefault()
): String {
    val eventTime = Instant.ofEpochMilli(this)
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

fun Double.toPrettyString(): String {
    val plainString = BigDecimal(this).toPlainString()
    if (!plainString.contains(CalculatorButton.DOT.text)) return plainString

    val split = plainString.split(CalculatorButton.DOT.text)
    var secondPart = split[1]
    if (secondPart.length > 2) {
        secondPart = secondPart.slice(0..1)
    }
    return if (secondPart.isEmpty() || secondPart.toInt() == 0) {
        split[0]
    } else {
        "${split[0]}.${secondPart}"
    }
}

fun String.smartToDouble(): Double {
    if (this.isEmpty()) return 0.0
    return this.replace("+", "").toDouble()
}

val Double.rounded: Double
    get() {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.HALF_UP
        return df.format(this).toDouble()
    }

