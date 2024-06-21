package com.glebkrep.simplebudget.core.data.data.repositories.calculatorInput

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DefaultCalculatorInputRepository
@Inject constructor(private val calculatorInputMemoryDataSource: CalculatorInputMemoryDataSource) :
    CalculatorInputRepository {
    override suspend fun getCalculatorInput(): Flow<String> {
        return calculatorInputMemoryDataSource.getInput()
    }

    override suspend fun getCalculatorInputSync(): String {
        return calculatorInputMemoryDataSource.getInputSync()
    }

    override suspend fun setCalculatorInput(calculatorInput: String) {
        calculatorInputMemoryDataSource.updateInput(calculatorInput)
    }
}
