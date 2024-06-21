package com.glebkrep.simplebudget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.glebkrep.simplebudget.core.ui.components.views.SimpleBudgetSnackBar
import com.glebkrep.simplebudget.core.ui.theme.SimpleBudgetTheme
import com.glebkrep.simplebudget.feature.calculator.navigation.CALCULATOR_ROUTE
import com.glebkrep.simplebudget.feature.calculator.navigation.calculatorScreen
import com.glebkrep.simplebudget.feature.preferences.navigation.navigateToPreferences
import com.glebkrep.simplebudget.feature.preferences.navigation.preferencesScreen
import com.glebkrep.simplebudget.feature.updatebilling.navigation.billingDateUpdateScreen
import com.glebkrep.simplebudget.feature.updatebilling.navigation.navigateToBillingDateUpdate
import com.glebkrep.simplebudget.feature.updatebudget.navigation.budgetUpdateScreen
import com.glebkrep.simplebudget.feature.updatebudget.navigation.navigateToBudgetUpdate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Suppress("LongMethod")
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleBudgetTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val coroutineScope = rememberCoroutineScope()
                    val snackBarHostState = remember { SnackbarHostState() }

                    Scaffold(
                        snackbarHost = {
                            SnackbarHost(snackBarHostState, snackbar = {
                                SimpleBudgetSnackBar(
                                    snackbarData = it
                                )
                            })
                        },
                        modifier = Modifier
                    ) { padding ->
                        val mainNavController = rememberNavController()
                        NavHost(
                            navController = mainNavController,
                            startDestination = CALCULATOR_ROUTE,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                        ) {
                            calculatorScreen(
                                navigateToSettings = {
                                    mainNavController.navigateToPreferences()
                                }
                            )
                            preferencesScreen(
                                navigateToBillingUpdate = {
                                    mainNavController.navigateToBillingDateUpdate()
                                },
                                navigateToBudgetUpdate = {
                                    mainNavController.navigateToBudgetUpdate()
                                },
                                goBack = {
                                    mainNavController.popBackStack()
                                }
                            )
                            billingDateUpdateScreen(
                                goBack = {
                                    mainNavController.popBackStack()
                                },
                                postSnackBar = {
                                    coroutineScope.launch {
                                        snackBarHostState.showSnackbar(
                                            message = it
                                        )
                                    }
                                })

                            budgetUpdateScreen(
                                goBack = {
                                    mainNavController.popBackStack()
                                }, postSnackBar = {
                                    coroutineScope.launch {
                                        snackBarHostState.showSnackbar(
                                            message = it
                                        )
                                    }
                                })

                        }
                    }
                }
            }
        }
    }
}
