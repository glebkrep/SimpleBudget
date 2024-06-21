package com.glebkrep.simplebudget.core.domain

import com.glebkrep.simplebudget.model.CalculatorButton
import javax.inject.Inject

class CreateUpdatedCalculatorInputUseCase @Inject constructor(
) {
    suspend operator fun invoke(
        calculatorInput: String,
        newButton: CalculatorButton
    ): String {
        return getNewCurrentInput(
            calculatorInput = calculatorInput,
            newButton = newButton
        )
    }

    private fun getNewCurrentInput(
        calculatorInput: String,
        newButton: CalculatorButton
    ): String {
        return when (newButton) {
            CalculatorButton.MINUS -> {
                calculatorInput.addMinus()
            }

            CalculatorButton.PLUS -> {
                calculatorInput.addPlus()
            }

            CalculatorButton.BACK -> {
                calculatorInput.addBack()
            }

            CalculatorButton.DOT -> {
                calculatorInput.addDot()
            }

            CalculatorButton.ZERO, CalculatorButton.ONE, CalculatorButton.TWO,
            CalculatorButton.THREE, CalculatorButton.FOUR, CalculatorButton.FIVE,
            CalculatorButton.SIX, CalculatorButton.SEVEN, CalculatorButton.EIGHT,
            CalculatorButton.NINE -> {
                calculatorInput.addDigit(newButton)
            }

            else -> {
                throw UnsupportedOperationException("Button not supported")
            }
        }
    }

    private fun String.addMinus() = this.replace("+", "")
    private fun String.addPlus() = "+${this}".replace("++", "+")
    private fun String.addBack() = when (val newString = this.dropLast(1)) {
        "+" -> "+0"
        "" -> "0"
        else -> newString
    }

    private fun String.addDot() = when {
        this.contains(CalculatorButton.DOT.text) -> {
            this
        }

        this.isEmpty() -> {
            "0${CalculatorButton.DOT.text}"
        }

        else -> {
            "$this${CalculatorButton.DOT.text}"
        }
    }

    private fun String.addDigit(newButton: CalculatorButton): String {
        val containsDot = this.contains(CalculatorButton.DOT.text)
        val afterDotLength = this.split(CalculatorButton.DOT.text).getOrNull(1)?.length ?: 0

        if (containsDot && afterDotLength >= 2) {
            return this
        }

        return when (this) {
            "0" -> {
                newButton.text
            }

            "+0" -> {
                "+${newButton.text}"
            }

            else -> {
                "${this}${newButton.text}"
            }
        }
    }

}
