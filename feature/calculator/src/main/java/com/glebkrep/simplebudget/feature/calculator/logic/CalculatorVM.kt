package com.glebkrep.simplebudget.feature.calculator.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.glebkrep.simplebudget.core.domain.models.BudgetDataOperations
import com.glebkrep.simplebudget.core.domain.usecases.GetCalculatorStateUseCase
import com.glebkrep.simplebudget.core.domain.usecases.HandleCalculatorDateRelatedEventUseCase
import com.glebkrep.simplebudget.core.domain.usecases.HandleCalculatorInputUseCase
import com.glebkrep.simplebudget.core.domain.usecases.DeleteRecentTransactionUseCase
import com.glebkrep.simplebudget.core.ui.AbstractScreenVM
import com.glebkrep.simplebudget.model.CalculatorButton
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalculatorVM @Inject constructor(
    private val getCalculatorStateUseCase: GetCalculatorStateUseCase,
    private val handleCalculatorInputUseCase: HandleCalculatorInputUseCase,
    private val removeRecentTransaction: DeleteRecentTransactionUseCase,
    private val handleCalculatorDateRelatedEventUseCase: HandleCalculatorDateRelatedEventUseCase,
) :
    AbstractScreenVM<CalculatorEvent, CalculatorScreenState, CalculatorAction>(
        CalculatorAction.None
    ) {
    private val _diffAnimationState: MutableLiveData<DiffAnimationState> = MutableLiveData()
    val diffAnimationState: LiveData<DiffAnimationState> = _diffAnimationState

    init {
        viewModelScope.launch {
            val diffCalculator = DiffCalculator()
            getCalculatorStateUseCase()
                .mapToCalculatorScreenState()
                .collect { data ->
                    data?.let {
                        if (it is CalculatorScreenState.Default) {
                            postAnimationState(
                                diffCalculator.getDiff(
                                    totalBudget = it.oldMoneyLeft,
                                    dailyBudget = it.oldDailyBudget,
                                    todayBudget = it.oldTodayBudget
                                )
                            )
                        }
                        postState(it)
                    }
                }
        }
    }

    private var dropStateJob: Job? = null
    private suspend fun postAnimationState(diffAnimationState: DiffAnimationState) {
        dropStateJob?.cancel()
        _diffAnimationState.postValue(diffAnimationState)
        dropStateJob = viewModelScope.launch {
            delay(DiffCalculator.DIFF_EXPIRY_TIME)
            _diffAnimationState.postValue(DiffAnimationState())
        }
    }


    override fun handleEvent(event: CalculatorEvent) {
        viewModelScope.launch {
            when (event) {
                is CalculatorEvent.KeyTap -> {
                    handleCalculatorInputUseCase(event.key)
                }

                is CalculatorEvent.CommitTransaction -> {
                    handleCalculatorInputUseCase(CalculatorButton.SAVE_CHANGE, event.comment)
                }

                is CalculatorEvent.SelectIncreaseDaily -> {
                    handleCalculatorDateRelatedEventUseCase(
                        operation = BudgetDataOperations.TransferLeftoverTodayToDaily
                    )
                }

                is CalculatorEvent.SelectIncreaseToday,
                is CalculatorEvent.SelectStartNextDay -> {
                    handleCalculatorDateRelatedEventUseCase(
                        operation = BudgetDataOperations.TransferLeftoverTodayToToday
                    )
                }

                CalculatorEvent.OnSettingsClicked -> {
                    postAction(CalculatorAction.GoToSettings)
                }

                is CalculatorEvent.DeleteRecent -> {
                    postAction(CalculatorAction.RecentDeleted)
                    removeRecentTransaction(event.id)
                }
            }
        }
    }
}
