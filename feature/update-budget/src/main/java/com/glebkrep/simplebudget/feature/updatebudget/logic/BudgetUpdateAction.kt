package com.glebkrep.simplebudget.feature.updatebudget.logic

sealed interface BudgetUpdateAction {
    data object None : BudgetUpdateAction
    data object GoBack : BudgetUpdateAction
    data class PostSnackBarAndGoBack(val message: String) : BudgetUpdateAction
}
