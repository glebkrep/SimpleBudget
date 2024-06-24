package com.glebkrep.simplebudget.feature.updatebilling.logic

sealed interface BillingDateUpdateEvent {
    data class OnNewBillingDateDate(val newVal: Long) : BillingDateUpdateEvent
    data object Back : BillingDateUpdateEvent
}
