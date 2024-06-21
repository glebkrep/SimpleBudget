package com.glebkrep.simplebudget.feature.updatebudget.vm

sealed class BudgetUpdateAction {
    data object None : BudgetUpdateAction()
    data object GoBack : BudgetUpdateAction()
    data class PostSnackBarAndGoBack(val message: String) : BudgetUpdateAction()
}
