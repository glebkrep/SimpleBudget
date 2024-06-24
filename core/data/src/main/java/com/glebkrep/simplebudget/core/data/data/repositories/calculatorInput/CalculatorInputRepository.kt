package com.glebkrep.simplebudget.core.data.data.repositories.calculatorInput

import kotlinx.coroutines.flow.Flow

interface CalculatorInputRepository {
    fun getCalculatorInput(): Flow<String>
    suspend fun getCalculatorInputSync(): String
    suspend fun setCalculatorInput(calculatorInput: String)
}
