package com.glebkrep.simplebudget.core.domain.converters

import javax.inject.Inject

class ConvertStringToDoubleSmartUseCase @Inject constructor() {

    suspend operator fun invoke(input: String): Double {
        return input.smartToDouble()
    }

    private fun String.smartToDouble(): Double {
        if (this.isEmpty()) return 0.0
        return this.replace("+", "").toDouble()
    }
}
