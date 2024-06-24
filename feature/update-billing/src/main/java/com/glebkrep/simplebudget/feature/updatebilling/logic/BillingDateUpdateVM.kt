package com.glebkrep.simplebudget.feature.updatebilling.logic

import androidx.lifecycle.viewModelScope
import com.glebkrep.simplebudget.core.domain.models.BudgetDataOperations
import com.glebkrep.simplebudget.core.domain.usecases.CreateUpdatedBudgetDataUseCase
import com.glebkrep.simplebudget.core.domain.usecases.GetBudgetAndPreferencesUseCase
import com.glebkrep.simplebudget.core.domain.usecases.UpdateBudgetDataUseCase
import com.glebkrep.simplebudget.core.ui.AbstractScreenVM
import com.glebkrep.simplebudget.model.BudgetData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.Year
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class BillingDateUpdateVM @Inject constructor(
    private val createUpdatedBudgetDataUseCase: CreateUpdatedBudgetDataUseCase,
    private val getBudgetAndPreferencesUseCase: GetBudgetAndPreferencesUseCase,
    private val updateBudgetDataUseCase: UpdateBudgetDataUseCase,
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
                    val oldBudget = getBudgetAndPreferencesUseCase().first()
                    val newBudget = createUpdatedBudgetDataUseCase.invoke(
                        BudgetDataOperations.NewBillingDate(event.newVal),
                        BudgetData(
                            todayBudget = oldBudget.todayBudget,
                            dailyBudget = oldBudget.dailyBudget,
                            totalLeft = oldBudget.totalLeft,
                            billingTimestamp = oldBudget.billingTimestamp,
                            lastLoginTimestamp = oldBudget.lastLoginTimestamp,
                            lastBillingUpdateTimestamp = oldBudget.lastBillingUpdateTimestamp
                        )
                    )
                    updateBudgetDataUseCase(newBudget)
                    postAction(BillingDateUpdateAction.PostSnackBarAndGoBack("Billing date updated!"))
                }

                BillingDateUpdateEvent.Back -> {
                    postAction(BillingDateUpdateAction.GoBack)
                }
            }
        }
    }
}
