package com.glebkrep.simplebudget.feature.preferences.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.glebkrep.simplebudget.feature.preferences.PreferencesScreenRoute

const val PREFERENCES_ROUTE = "preferences_route"

fun NavController.navigateToPreferences() {
    val newRoute = PREFERENCES_ROUTE
    navigate(newRoute) {
//        navOptions()
    }
}

fun NavGraphBuilder.preferencesScreen(
    navigateToBillingUpdate: () -> Unit,
    navigateToBudgetUpdate: () -> Unit,
    goBack: () -> Unit,
) {
    composable(PREFERENCES_ROUTE) {
        PreferencesScreenRoute(
            goBack = goBack,
            goToBillingUpdate = navigateToBillingUpdate,
            goToBudgetUpdate = navigateToBudgetUpdate,
        )
    }
}
