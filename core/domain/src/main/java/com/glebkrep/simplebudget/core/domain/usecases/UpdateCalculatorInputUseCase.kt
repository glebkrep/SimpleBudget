package com.glebkrep.simplebudget.core.domain.usecases

import com.glebkrep.simplebudget.core.common.Dispatcher
import com.glebkrep.simplebudget.core.common.SimpleBudgetDispatcher
import com.glebkrep.simplebudget.core.data.data.repositories.calculatorInput.CalculatorInputRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateCalculatorInputUseCase @Inject constructor(
    private val calculatorInputRepository: CalculatorInputRepository,
    @Dispatcher(SimpleBudgetDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        input: String,
    ) = withContext(ioDispatcher) {
        calculatorInputRepository.setCalculatorInput(input)
    }
}
