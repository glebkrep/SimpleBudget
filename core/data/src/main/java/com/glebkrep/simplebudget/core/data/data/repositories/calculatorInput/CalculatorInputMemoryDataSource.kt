package com.glebkrep.simplebudget.core.data.data.repositories.calculatorInput

import com.glebkrep.simplebudget.core.common.Dispatcher
import com.glebkrep.simplebudget.core.common.SimpleBudgetDispatcher
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject


@ViewModelScoped
class CalculatorInputMemoryDataSource
@Inject constructor(
    @Dispatcher(SimpleBudgetDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) {

    private val calculatorData =
        MutableStateFlow(
            "0"
        )

    fun getInput(): Flow<String> {
        return calculatorData
    }

    suspend fun getInputSync(): String =
        withContext(ioDispatcher) {
            getInput().first()
        }


    suspend fun updateInput(calculatorInput: String) =
        withContext(ioDispatcher) {
            calculatorData.emit(calculatorInput)
        }

}
