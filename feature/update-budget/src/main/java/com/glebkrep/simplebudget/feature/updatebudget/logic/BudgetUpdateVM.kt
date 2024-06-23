package com.glebkrep.simplebudget.feature.updatebudget.logic

import androidx.lifecycle.viewModelScope
import com.glebkrep.simplebudget.core.data.data.models.BudgetDataOperations
import com.glebkrep.simplebudget.core.data.data.repositories.budgetData.BudgetRepository
import com.glebkrep.simplebudget.core.data.data.repositories.calculatorInput.CalculatorInputRepository
import com.glebkrep.simplebudget.core.domain.usecases.CreateUpdatedBudgetDataUseCase
import com.glebkrep.simplebudget.core.domain.usecases.CreateUpdatedCalculatorInputUseCase
import com.glebkrep.simplebudget.core.domain.toPrettyString
import com.glebkrep.simplebudget.core.ui.AbstractScreenVM
import com.glebkrep.simplebudget.model.CalculatorButton
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetUpdateVM @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val calculatorInputRepository: CalculatorInputRepository,
    private val createUpdatedCalculatorInputUseCase: CreateUpdatedCalculatorInputUseCase,
    private val createUpdatedBudgetDataUseCase: CreateUpdatedBudgetDataUseCase,
) :
    AbstractScreenVM<BudgetUpdateEvent, BudgetUpdateState, BudgetUpdateAction>(BudgetUpdateAction.None) {

    init {
        viewModelScope.launch {
            combine(
                budgetRepository.getBudgetData(),
                calculatorInputRepository.getCalculatorInput()
            ) { budgetData, calculatorInput ->
                val oldBudget = budgetData.totalLeft.toPrettyString()
                postState(
                    BudgetUpdateState.BudgetInput(
                        currentInput = calculatorInput,
                        currentBudget = oldBudget,
                    )
                )
            }.collect()
        }
    }

    override fun handleEvent(event: BudgetUpdateEvent) {
        viewModelScope.launch {
            when (event) {
                is BudgetUpdateEvent.KeyTap -> {
                    when (event.key) {
                        CalculatorButton.ENTER -> {
                            val oldBudget = budgetRepository.getBudgetData().first()
                            val newInput = calculatorInputRepository.getCalculatorInputSync()
                            val newBudget = createUpdatedBudgetDataUseCase.invoke(
                                BudgetDataOperations.NewTotalBudget(newInput),
                                oldBudget
                            )
                            budgetRepository.setBudgetData(newBudget)
                            postAction(
                                BudgetUpdateAction.PostSnackBarAndGoBack(
                                    "Budget updated: ${newBudget.totalLeft}"
                                )
                            )
                        }

                        else -> {
                            val newInput = createUpdatedCalculatorInputUseCase(
                                calculatorInput = calculatorInputRepository.getCalculatorInputSync(),
                                newButton = event.key
                            )
                            calculatorInputRepository.setCalculatorInput(newInput)
                        }
                    }
                }

                BudgetUpdateEvent.Back -> postAction(BudgetUpdateAction.GoBack)
            }
        }
    }
}
