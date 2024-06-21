package com.glebkrep.simplebudget.feature.updatebilling.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.glebkrep.simplebudget.feature.updatebilling.BillingDateUpdateScreen

const val BILLING_UPDATE_ROUTE = "update_billing_route"

fun NavController.navigateToBillingDateUpdate() {
    val newRoute = BILLING_UPDATE_ROUTE
    navigate(newRoute) {
    }
}

fun NavGraphBuilder.billingDateUpdateScreen(
    goBack: () -> Unit,
    postSnackBar: (String) -> Unit
) {
    composable(BILLING_UPDATE_ROUTE) {
        BillingDateUpdateScreen(
            goBack = goBack,
            postSnackBar = postSnackBar
        )
    }
}
