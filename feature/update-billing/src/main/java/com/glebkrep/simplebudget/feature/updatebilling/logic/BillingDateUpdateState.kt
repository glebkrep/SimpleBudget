package com.glebkrep.simplebudget.feature.updatebilling.logic

sealed interface BillingDateUpdateState {
    data class DatePicker(
        val currentBillingDate: Long,
        val dateValidator: (Long) -> (Boolean),
        val yearRange: IntRange
    ) : BillingDateUpdateState
}
