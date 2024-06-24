package com.glebkrep.simplebudget.feature.updatebilling.logic

import androidx.lifecycle.viewModelScope
import com.glebkrep.simplebudget.core.domain.usecases.GetBudgetAndPreferencesUseCase
import com.glebkrep.simplebudget.core.domain.usecases.UpdateBillingDateUseCase
import com.glebkrep.simplebudget.core.ui.AbstractScreenVM
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.Year
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class BillingDateUpdateVM @Inject constructor(
    private val getBudgetAndPreferencesUseCase: GetBudgetAndPreferencesUseCase,
    private val updateBillingDateUseCase: UpdateBillingDateUseCase,
) :
    AbstractScreenVM<BillingDateUpdateEvent, BillingDateUpdateState, BillingDateUpdateAction>(
        BillingDateUpdateAction.None
    ) {

    init {
        viewModelScope.launch {
            getBudgetAndPreferencesUseCase().collect {
                val currentYear: Int = Year.now().value
                postState(
                    BillingDateUpdateState.DatePicker(
                        currentBillingDate = it.billingTimestamp,
                        dateValidator = ::isDateValid,
                        yearRange = currentYear..currentYear + 1
                    )
                )
            }
        }

    }

    private fun isDateValid(time: Long): Boolean {
        val instant = Instant.ofEpochMilli(time).plus(1, ChronoUnit.DAYS)
        val isBefore = instant.isBefore(Instant.now())
        return !isBefore
    }

    override fun handleEvent(event: BillingDateUpdateEvent) {
        viewModelScope.launch {
            when (event) {
                is BillingDateUpdateEvent.OnNewBillingDateDate -> {
                    updateBillingDateUseCase.invoke(event.newVal)
                    postAction(BillingDateUpdateAction.PostSnackBarAndGoBack("Billing date updated!"))
                }

                BillingDateUpdateEvent.Back -> {
                    postAction(BillingDateUpdateAction.GoBack)
                }
            }
        }
    }
}
