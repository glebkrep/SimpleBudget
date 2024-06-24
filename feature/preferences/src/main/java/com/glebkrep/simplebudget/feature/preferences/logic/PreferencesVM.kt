package com.glebkrep.simplebudget.feature.preferences.logic

import androidx.lifecycle.viewModelScope
import com.glebkrep.simplebudget.core.domain.toPrettyDate
import com.glebkrep.simplebudget.core.domain.toPrettyString
import com.glebkrep.simplebudget.core.domain.usecases.GetBudgetAndPreferencesUseCase
import com.glebkrep.simplebudget.core.domain.usecases.UpdatePreferencesUseCase
import com.glebkrep.simplebudget.core.ui.AbstractScreenVM
import com.glebkrep.simplebudget.model.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesVM @Inject constructor(
    private val getBudgetAndPreferencesUseCase: GetBudgetAndPreferencesUseCase,
    private val updatePreferencesUseCase: UpdatePreferencesUseCase,
) :
    AbstractScreenVM<PreferencesEvent, PreferencesState, PreferencesAction>(PreferencesAction.None) {

    init {
        viewModelScope.launch {
            getBudgetAndPreferencesUseCase().collect {
                val newState =
                    PreferencesState.Display(
                        isCommentsEnabled = it.isCommentsEnabled,
                        currentBillingDatePretty = it.billingTimestamp.toPrettyDate(needTime = false),
                        currentBudgetPretty = it.totalLeft.toPrettyString()
                    )
                postState(newState)
            }
        }
    }

    override fun handleEvent(event: PreferencesEvent) {
        viewModelScope.launch {
            when (event) {
                is PreferencesEvent.EnableCommentsTweak -> {
                    updatePreferencesUseCase(AppPreferences(isCommentsEnabled = event.newVal))
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
