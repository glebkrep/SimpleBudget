package com.glebkrep.simplebudget.feature.updatebilling.vm

sealed class BillingDateUpdateEvent {
    data class OnNewBillingDateDate(val newVal: Long) : BillingDateUpdateEvent()
    data object Back : BillingDateUpdateEvent()

}
