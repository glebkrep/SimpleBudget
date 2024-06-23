package com.glebkrep.simplebudget.feature.preferences.logic

import androidx.lifecycle.viewModelScope
import com.glebkrep.simplebudget.core.data.data.repositories.budgetData.BudgetRepository
import com.glebkrep.simplebudget.core.data.data.repositories.preferences.PreferencesRepository
import com.glebkrep.simplebudget.core.domain.toPrettyDate
import com.glebkrep.simplebudget.core.domain.toPrettyString
import com.glebkrep.simplebudget.core.ui.AbstractScreenVM
import com.glebkrep.simplebudget.model.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesVM @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val preferencesRepository: PreferencesRepository,
) :
    AbstractScreenVM<PreferencesEvent, PreferencesState, PreferencesAction>(PreferencesAction.None) {

    init {
        viewModelScope.launch {
            combine(
                budgetRepository.getBudgetData(),
                preferencesRepository.getPreferences()
            ) { budgetData, preferences ->

                val newState =
                    PreferencesState.Display(
                        isCommentsEnabled = preferences.isCommentsEnabled,
                        currentBillingDatePretty = budgetData.billingTimestamp.toPrettyDate(needTime = false),
                        currentBudgetPretty = budgetData.totalLeft.toPrettyString()
                    )
                postState(newState)
            }.collect()
        }
    }

    override fun handleEvent(event: PreferencesEvent) {
        viewModelScope.launch {
            when (event) {
                is PreferencesEvent.EnableCommentsTweak -> {
                    preferencesRepository.setPreferences(AppPreferences(isCommentsEnabled = event.newVal))
                }

                is PreferencesEvent.ToBillingDatePicker -> {
                    postAction(PreferencesAction.GoToBillingUpdate)
                }

                is PreferencesEvent.ToBudgetPicker -> {
                    postAction(PreferencesAction.GoToBudgetUpdate)
                }

                PreferencesEvent.Back -> {
                    postAction(PreferencesAction.GoBack)
                }
            }
        }
    }
}
