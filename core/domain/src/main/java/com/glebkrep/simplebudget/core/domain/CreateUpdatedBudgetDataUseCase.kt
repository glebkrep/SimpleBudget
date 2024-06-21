package com.glebkrep.simplebudget.core.domain

import com.glebkrep.simplebudget.core.data.data.models.BudgetDataOperations
import com.glebkrep.simplebudget.core.domain.converters.ConvertDoubleToPrettyDoubleUseCase
import com.glebkrep.simplebudget.core.domain.converters.ConvertStringToDoubleSmartUseCase
import com.glebkrep.simplebudget.core.domain.converters.ConvertTimestampToDayNumberUseCase
import com.glebkrep.simplebudget.model.BudgetData
import javax.inject.Inject

class CreateUpdatedBudgetDataUseCase @Inject constructor(
    private val convertTimestampToDayNumberUseCase: ConvertTimestampToDayNumberUseCase,
    private val convertDoubleToPrettyDoubleUseCase: ConvertDoubleToPrettyDoubleUseCase,
    private val convertStringToDoubleSmartUseCase: ConvertStringToDoubleSmartUseCase,
) {

    suspend operator fun invoke(
        operation: BudgetDataOperations,
        budgetData: BudgetData
    ): BudgetData {
        return when (operation) {
            is BudgetDataOperations.NewTotalBudget -> {
                budgetData.setNewBudget(convertStringToDoubleSmartUseCase(operation.newBudget))
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
                val calculatorInputDouble = convertStringToDoubleSmartUseCase(calculatorInput)
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

    private suspend fun BudgetData.transferToToday(): BudgetData {
        val daysToBilling =
            convertTimestampToDayNumberUseCase(this.billingTimestamp) - convertTimestampToDayNumberUseCase(
                System.currentTimeMillis()
            )
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

    private suspend fun BudgetData.handleNormalCalculatorInput(
        calculatorInput: Double
    ): BudgetData {
        val daysToBilling =
            convertTimestampToDayNumberUseCase(this.billingTimestamp) - convertTimestampToDayNumberUseCase(
                System.currentTimeMillis()
            )
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

    private suspend fun BudgetData.handlePlusCalculatorInput(
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

    private suspend fun BudgetData.transferLeftoverToDaily(): BudgetData {
        val newBudget = this.setNewBudget(this.totalLeft)
        return this.copy(
            lastLoginTimestamp = System.currentTimeMillis(),
            dailyBudget = newBudget.dailyBudget,
            todayBudget = newBudget.todayBudget,
        )
    }

    private suspend fun BudgetData.setNewBudget(budget: Double): BudgetData {
        val daysToBilling =
            convertTimestampToDayNumberUseCase(this.billingTimestamp) - convertTimestampToDayNumberUseCase(
                System.currentTimeMillis()
            )

        val newDailyBudget =
            convertDoubleToPrettyDoubleUseCase((budget / (daysToBilling + 1)))

        return this.copy(
            todayBudget = newDailyBudget,
            dailyBudget = newDailyBudget,
            totalLeft = convertDoubleToPrettyDoubleUseCase(budget),
            billingTimestamp = this.billingTimestamp,
            lastLoginTimestamp = System.currentTimeMillis(),
            lastBillingUpdateTimestamp = System.currentTimeMillis()
        )
    }

    private suspend fun BudgetData.setNewBillingDate(billingTimestamp: Long): BudgetData {
        val daysToBilling =
            convertTimestampToDayNumberUseCase(billingTimestamp) - convertTimestampToDayNumberUseCase(
                System.currentTimeMillis()
            )

        val newDailyBudget =
            convertDoubleToPrettyDoubleUseCase((this.totalLeft / (daysToBilling + 1)))

        return this.copy(
            todayBudget = newDailyBudget,
            dailyBudget = newDailyBudget,
            totalLeft = convertDoubleToPrettyDoubleUseCase(this.totalLeft),
            billingTimestamp = billingTimestamp,
            lastLoginTimestamp = System.currentTimeMillis(),
            lastBillingUpdateTimestamp = System.currentTimeMillis()
        )
    }
}
