package com.glebkrep.simplebudget.core.domain.converters

import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

class ConvertDoubleToPrettyDoubleUseCase @Inject constructor() {

    operator fun invoke(float: Double): Double {
        return float.pretty
    }

    private val Double.pretty: Double
        get() {
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.HALF_UP
            return df.format(this).toDouble()
        }
}
