package com.glebkrep.simplebudget.core.domain.usecases

import com.glebkrep.simplebudget.core.data.data.models.BudgetDataOperations
import com.glebkrep.simplebudget.core.domain.dayDiffTo
import com.glebkrep.simplebudget.core.domain.rounded
import com.glebkrep.simplebudget.core.domain.smartToDouble
import com.glebkrep.simplebudget.model.BudgetData
import javax.inject.Inject

class CreateUpdatedBudgetDataUseCase @Inject constructor() {

    operator fun invoke(
        operation: BudgetDataOperations,
        budgetData: BudgetData
    ): BudgetData {
        return when (operation) {
            is BudgetDataOperations.NewTotalBudget -> {
                budgetData.setNewBudget(operation.newBudget.smartToDouble())
            }

            is BudgetDataOperations.NewBillingDate -> {
                budgetData.setNewBillingDate(operation.newBillingTimestamp)
            }

            BudgetDataOperations.TransferLeftoverTodayToDaily -> {
                budgetData.transferLeftoverToDaily()
            }

            BudgetDataOperations.TransferLeftoverTodayToToday -> {
                budgetData.transferToToday()
            }

            is BudgetDataOperations.HandleCalculatorInput -> {
                val calculatorInput = operation.calculatorInput
                val calculatorInputDouble = calculatorInput.smartToDouble()
                if (calculatorInput.startsWith("+")) {
                    budgetData.handlePlusCalculatorInput(calculatorInputDouble)
                } else {
                    budgetData.handleNormalCalculatorInput(calculatorInputDouble)
                }
            }

            is BudgetDataOperations.RevertTransaction -> {
                val changeAmount = operation.transactionSum
                if (changeAmount < 0) {
                    budgetData.handlePlusCalculatorInput(-changeAmount)
                } else {
                    budgetData.handleNormalCalculatorInput(
                        calculatorInput = changeAmount
                    )
                }
            }
        }
    }

    private fun BudgetData.transferToToday(): BudgetData {
        val daysToBilling = System.currentTimeMillis().dayDiffTo(this.billingTimestamp)
        val neededBudget = this.dailyBudget * daysToBilling
        val freeBudget = this.totalLeft - neededBudget
        val newDaily = if (daysToBilling == 0) {
            freeBudget
        } else this.dailyBudget
        return this.copy(
            todayBudget = freeBudget,
            dailyBudget = newDaily,
            lastLoginTimestamp = System.currentTimeMillis()
        )
    }

    private fun BudgetData.handleNormalCalculatorInput(
        calculatorInput: Double
    ): BudgetData {
        val daysToBilling = System.currentTimeMillis().dayDiffTo(this.billingTimestamp)
        return when {
            this.totalLeft <= calculatorInput -> {
                this.copy(
                    totalLeft = 0.0,
                    dailyBudget = 0.0,
                    todayBudget = 0.0
                )
            }

            daysToBilling == 0 -> {
                val newBudget = setNewBudget(this.totalLeft - calculatorInput)
                return this.copy(
                    todayBudget = newBudget.todayBudget,
                    dailyBudget = newBudget.dailyBudget,
                    totalLeft = newBudget.totalLeft
                )
            }

            this.todayBudget >= calculatorInput -> {
                this.copy(
                    todayBudget = this.todayBudget - calculatorInput,
                    totalLeft = this.totalLeft - calculatorInput
                )
            }

            this.totalLeft > calculatorInput -> {
                val totalLeft = this.totalLeft - calculatorInput
                val daily = totalLeft / daysToBilling
                this.copy(
                    todayBudget = 0.0,
                    dailyBudget = daily,
                    totalLeft = totalLeft
                )
            }

            else -> {
                error("Unexpected state")
            }
        }
    }

    private fun BudgetData.handlePlusCalculatorInput(
        calculatorInput: Double
    ): BudgetData {
        return if (this.dailyBudget < this.todayBudget + calculatorInput) {
            val newBudgetData = setNewBudget(this.totalLeft + calculatorInput)
            this.copy(
                totalLeft = newBudgetData.totalLeft,
                todayBudget = newBudgetData.todayBudget,
                dailyBudget = newBudgetData.dailyBudget
            )
        } else {
            this.copy(
                todayBudget = this.todayBudget + calculatorInput,
                totalLeft = this.totalLeft + calculatorInput
            )
        }
    }

    private fun BudgetData.transferLeftoverToDaily(): BudgetData {
        val newBudget = this.setNewBudget(this.totalLeft)
        return this.copy(
            lastLoginTimestamp = System.currentTimeMillis(),
            dailyBudget = newBudget.dailyBudget,
            todayBudget = newBudget.todayBudget,
        )
    }

    private fun BudgetData.setNewBudget(budget: Double): BudgetData {
        val daysToBilling = System.currentTimeMillis().dayDiffTo(this.billingTimestamp)

        val newDailyBudget = (budget / (daysToBilling + 1)).rounded

        return this.copy(
            todayBudget = newDailyBudget,
            dailyBudget = newDailyBudget,
            totalLeft = budget.rounded,
            billingTimestamp = this.billingTimestamp,
            lastLoginTimestamp = System.currentTimeMillis(),
            lastBillingUpdateTimestamp = System.currentTimeMillis()
        )
    }

    private fun BudgetData.setNewBillingDate(billingTimestamp: Long): BudgetData {
        val daysToBilling = System.currentTimeMillis().dayDiffTo(billingTimestamp)
        val newDailyBudget = (this.totalLeft / (daysToBilling + 1)).rounded

        return this.copy(
            todayBudget = newDailyBudget,
            dailyBudget = newDailyBudget,
            totalLeft = this.totalLeft.rounded,
            billingTimestamp = billingTimestamp,
            lastLoginTimestamp = System.currentTimeMillis(),
            lastBillingUpdateTimestamp = System.currentTimeMillis()
        )
    }
}
