package com.glebkrep.simplebudget.core.domain.usecases

import com.glebkrep.simplebudget.core.common.Dispatcher
import com.glebkrep.simplebudget.core.common.SimpleBudgetDispatcher
import com.glebkrep.simplebudget.core.data.data.repositories.calculatorInput.CalculatorInputRepository
import com.glebkrep.simplebudget.core.domain.usecases.internal.CreateUpdatedCalculatorInputUseCase
import com.glebkrep.simplebudget.model.CalculatorButton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateCurrentCalculatorInputWithKeyUseCase @Inject internal constructor(
    private val calculatorInputRepository: CalculatorInputRepository,
    private val createUpdatedCalculatorInputUseCase: CreateUpdatedCalculatorInputUseCase,
    @Dispatcher(SimpleBudgetDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        newButton: CalculatorButton
    ) = withContext(ioDispatcher) {
        val newInput = createUpdatedCalculatorInputUseCase(
            calculatorInput = calculatorInputRepository.getCalculatorInputSync(),
            newButton = newButton
        )
        calculatorInputRepository.setCalculatorInput(newInput)
    }
}
