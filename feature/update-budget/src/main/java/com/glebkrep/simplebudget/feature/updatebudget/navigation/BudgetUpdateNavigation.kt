package com.glebkrep.simplebudget.feature.updatebudget.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.glebkrep.simplebudget.feature.updatebudget.BudgetUpdateScreenRoute


const val BUDGET_UPDATE_ROUTE = "update_budget_route"

fun NavController.navigateToBudgetUpdate() {
    val newRoute = BUDGET_UPDATE_ROUTE
    navigate(newRoute) {
    }
}

fun NavGraphBuilder.budgetUpdateScreen(
    goBack: () -> Unit,
    postSnackBar: (String) -> Unit
) {
    composable(BUDGET_UPDATE_ROUTE) {
        BudgetUpdateScreenRoute(
            goBack = goBack,
            postSnackBar = postSnackBar
        )
    }
}
