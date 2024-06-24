package com.glebkrep.simplebudget.feature.updatebudget.logic

import androidx.lifecycle.viewModelScope
import com.glebkrep.simplebudget.core.domain.toPrettyString
import com.glebkrep.simplebudget.core.domain.usecases.GetBudgetAndInputUseCase
import com.glebkrep.simplebudget.core.domain.usecases.UpdateTotalBudgetWithCurrentCalculatorInputUseCase
import com.glebkrep.simplebudget.core.domain.usecases.UpdateCurrentCalculatorInputWithKeyUseCase
import com.glebkrep.simplebudget.core.ui.AbstractScreenVM
import com.glebkrep.simplebudget.model.CalculatorButton
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetUpdateVM @Inject constructor(
    private val getBudgetAndInputUseCase: GetBudgetAndInputUseCase,
    private val updateTotalBudgetWithCurrentCalculatorInputUseCase: UpdateTotalBudgetWithCurrentCalculatorInputUseCase,
    private val updateCurrentCalculatorInputWithKeyUseCase: UpdateCurrentCalculatorInputWithKeyUseCase
) :
    AbstractScreenVM<BudgetUpdateEvent, BudgetUpdateState, BudgetUpdateAction>(BudgetUpdateAction.None) {

    init {
        viewModelScope.launch {
            getBudgetAndInputUseCase().collect {
                val oldBudget = it.totalLeft.toPrettyString()
                postState(
                    BudgetUpdateState.BudgetInput(
                        currentInput = it.calculatorInput,
                        currentBudget = oldBudget,
                    )
                )
            }
        }
    }

    override fun handleEvent(event: BudgetUpdateEvent) {
        viewModelScope.launch {
            when (event) {
                is BudgetUpdateEvent.KeyTap -> {
                    when (event.calculatorButton) {
                        CalculatorButton.ENTER -> {
                            val newTotalLeft =
                                updateTotalBudgetWithCurrentCalculatorInputUseCase.invoke()
                            postAction(
                                BudgetUpdateAction.PostSnackBarAndGoBack(
                                    "Budget updated: $newTotalLeft"
                                )
                            )
                        }

                        else -> {
                            updateCurrentCalculatorInputWithKeyUseCase.invoke(event.calculatorButton)
                        }
                    }
                }

                BudgetUpdateEvent.Back -> {
                    postAction(BudgetUpdateAction.GoBack)
                }
            }
        }
    }
}
