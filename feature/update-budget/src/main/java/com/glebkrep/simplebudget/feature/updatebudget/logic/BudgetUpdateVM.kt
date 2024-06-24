package com.glebkrep.simplebudget.feature.updatebudget.logic

import androidx.lifecycle.viewModelScope
import com.glebkrep.simplebudget.core.domain.models.BudgetDataOperations
import com.glebkrep.simplebudget.core.domain.usecases.CreateUpdatedBudgetDataUseCase
import com.glebkrep.simplebudget.core.domain.usecases.CreateUpdatedCalculatorInputUseCase
import com.glebkrep.simplebudget.core.domain.toPrettyString
import com.glebkrep.simplebudget.core.domain.usecases.GetBudgetAndInputUseCase
import com.glebkrep.simplebudget.core.domain.usecases.UpdateBudgetDataUseCase
import com.glebkrep.simplebudget.core.domain.usecases.UpdateCalculatorInputUseCase
import com.glebkrep.simplebudget.core.ui.AbstractScreenVM
import com.glebkrep.simplebudget.model.BudgetData
import com.glebkrep.simplebudget.model.CalculatorButton
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetUpdateVM @Inject constructor(
    private val getBudgetAndInputUseCase: GetBudgetAndInputUseCase,
    private val createUpdatedCalculatorInputUseCase: CreateUpdatedCalculatorInputUseCase,
    private val createUpdatedBudgetDataUseCase: CreateUpdatedBudgetDataUseCase,
    private val updateBudgetDataUseCase: UpdateBudgetDataUseCase,
    private val updateCalculatorInputUseCase: UpdateCalculatorInputUseCase
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
                    when (event.key) {
                        CalculatorButton.ENTER -> {
                            val data = getBudgetAndInputUseCase().first()
                            val newBudget = createUpdatedBudgetDataUseCase.invoke(
                                BudgetDataOperations.NewTotalBudget(data.calculatorInput),
                                BudgetData(
                                    todayBudget = data.todayBudget,
                                    dailyBudget = data.dailyBudget,
                                    totalLeft = data.totalLeft,
                                    billingTimestamp = data.billingTimestamp,
                                    lastLoginTimestamp = data.lastLoginTimestamp,
                                    lastBillingUpdateTimestamp = data.lastBillingUpdateTimestamp
                                )
                            )
                            updateBudgetDataUseCase.invoke(newBudget)
                            postAction(
                                BudgetUpdateAction.PostSnackBarAndGoBack(
                                    "Budget updated: ${newBudget.totalLeft}"
                                )
                            )
                        }

                        else -> {
                            val newInput = createUpdatedCalculatorInputUseCase(
                                calculatorInput = getBudgetAndInputUseCase().first().calculatorInput,
                                newButton = event.key
                            )
                            updateCalculatorInputUseCase.invoke(newInput)
                        }
                    }
                }

                BudgetUpdateEvent.Back -> postAction(BudgetUpdateAction.GoBack)
            }
        }
    }
}
