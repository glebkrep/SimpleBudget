package com.glebkrep.simplebudget.feature.preferences.vm

sealed class PreferencesEvent {
    data class EnableCommentsTweak(val newVal: Boolean) : PreferencesEvent()
    data object ToBillingDatePicker : PreferencesEvent()
    data object ToBudgetPicker : PreferencesEvent()
    data object Back : PreferencesEvent()


}
