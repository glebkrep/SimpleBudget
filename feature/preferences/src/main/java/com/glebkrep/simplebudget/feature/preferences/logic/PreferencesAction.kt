package com.glebkrep.simplebudget.feature.preferences.logic

sealed interface PreferencesAction {
    data object None : PreferencesAction
    data object GoBack : PreferencesAction
    data object GoToBillingUpdate : PreferencesAction
    data object GoToBudgetUpdate : PreferencesAction
}
