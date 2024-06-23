package com.glebkrep.simplebudget.core.domain.usecases

import com.glebkrep.simplebudget.core.common.Dispatcher
import com.glebkrep.simplebudget.core.common.SimpleBudgetDispatcher
import com.glebkrep.simplebudget.core.data.data.models.BudgetDataOperations
import com.glebkrep.simplebudget.core.data.data.repositories.budgetData.BudgetRepository
import com.glebkrep.simplebudget.core.data.data.repositories.calculatorInput.CalculatorInputRepository
import com.glebkrep.simplebudget.core.data.data.repositories.recentTransactions.RecentTransactionsRepository
import com.glebkrep.simplebudget.core.database.recentTransaction.RecentTransactionEntity
import com.glebkrep.simplebudget.core.domain.smartToDouble
import com.glebkrep.simplebudget.model.CalculatorButton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject


class HandleCalculatorInputUseCase @Inject constructor(
    private val calculatorInputRepository: CalculatorInputRepository,
    private val budgetRepository: BudgetRepository,
    private val recentTransactionsRepository: RecentTransactionsRepository,
    private val createUpdatedBudgetDataUseCase: CreateUpdatedBudgetDataUseCase,
    private val createUpdatedCalculatorInputUseCase: CreateUpdatedCalculatorInputUseCase,
    @Dispatcher(SimpleBudgetDispatcher.Default) private val defaultDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        calculatorButton: CalculatorButton,
        comment: String? = null
    ) =
        withContext(defaultDispatcher) {
            val currentInput = calculatorInputRepository.getCalculatorInputSync()
            handleNewButtonInput(
                currentInput = currentInput,
                newButton = calculatorButton,
                comment = comment
            )
        }

    private suspend fun handleNewButtonInput(
        currentInput: String,
        newButton: CalculatorButton,
        comment: String?
    ) {
        val newCalculatorInput = when (newButton) {
            CalculatorButton.SAVE_CHANGE -> {
                commitInput(currentInput, comment)
                "0"
            }

            else -> {
                createUpdatedCalculatorInputUseCase(
                    currentInput,
                    newButton
                )
            }
        }
        calculatorInputRepository.setCalculatorInput(newCalculatorInput)
    }

    private suspend fun commitInput(
        currentInput: String,
        comment: String?
    ) {
        val currentBudgetData = budgetRepository.getBudgetData().first()
        recentTransactionsRepository.addRecent(
            RecentTransactionEntity(
                date = System.currentTimeMillis(),
                sum = currentInput.smartToDouble(),
                isPlusOperation = currentInput.contains("+"),
                comment = comment
            )
        )
        val newBudgetData = createUpdatedBudgetDataUseCase(
            operation = BudgetDataOperations.HandleCalculatorInput(
                currentInput
            ),
            budgetData = currentBudgetData
        )
        budgetRepository.setBudgetData(
            newBudgetData
        )
    }

}
