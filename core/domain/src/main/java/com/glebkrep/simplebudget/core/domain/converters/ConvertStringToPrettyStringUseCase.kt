package com.glebkrep.simplebudget.core.domain.converters

import com.glebkrep.simplebudget.model.CalculatorButton
import java.math.BigDecimal
import javax.inject.Inject

class ConvertStringToPrettyStringUseCase @Inject constructor() {

    suspend operator fun invoke(input: String): String {
        return input.pretty
    }

    private val String.pretty: String
        get() {
            if (this.isEmpty()) return this
            val string = BigDecimal(this).toPlainString()
            if (string.contains(CalculatorButton.DOT.text)) {
                val split = string.split(CalculatorButton.DOT.text)
                var secondPart = split[1]
                if (secondPart.length > 2) {
                    secondPart = secondPart.slice(0..1)
                }
                if (secondPart.isEmpty() || secondPart.toInt() == 0) {
                    return split[0]
                }
                return "${split[0]}.${secondPart}"
            }
            return string
        }
}
