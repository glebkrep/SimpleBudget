package com.glebkrep.simplebudget.core.domain.converters

import javax.inject.Inject

class ConvertTimestampToDayNumberUseCase @Inject constructor() {
    suspend operator fun invoke(timestamp: Long): Int {
        return (timestamp / DAY_IN_MILLIS).toInt()
    }

    private companion object {
        private const val DAY_IN_MILLIS: Long = 1000L * 60 * 60 * 24
    }
}
