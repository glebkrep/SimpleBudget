package com.glebkrep.simplebudget.feature.calculator.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.glebkrep.simplebudget.feature.calculator.CalculatorScreenRoute


const val CALCULATOR_ROUTE = "calculator_route"

fun NavGraphBuilder.calculatorScreen(
    navigateToSettings: () -> Unit,
) {
    composable(CALCULATOR_ROUTE) {
        CalculatorScreenRoute(
            needToNavigateToSettings = navigateToSettings,
        )
    }
}
