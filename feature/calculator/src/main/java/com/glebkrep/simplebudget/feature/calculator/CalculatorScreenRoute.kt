package com.glebkrep.simplebudget.feature.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import com.glebkrep.simplebudget.core.data.data.models.CalculatorEvent
import com.glebkrep.simplebudget.core.data.data.models.CalculatorScreenState
import com.glebkrep.simplebudget.core.ui.components.ComposeUtils.performClickVibration
import com.glebkrep.simplebudget.feature.calculator.view.CalculatorBadBillingDatePage
import com.glebkrep.simplebudget.feature.calculator.view.CalculatorChooseIncreasePage
import com.glebkrep.simplebudget.feature.calculator.view.CalculatorMainPage
import com.glebkrep.simplebudget.feature.calculator.vm.CalculatorAction
import com.glebkrep.simplebudget.feature.calculator.vm.CalculatorVM
import com.glebkrep.simplebudget.feature.calculator.vm.DiffAnimationState

@Composable
fun CalculatorScreenRoute(
    needToNavigateToSettings: () -> (Unit),
    viewModel: CalculatorVM = hiltViewModel()
) {
    val state by viewModel.state.observeAsState()
    val action by viewModel.action.observeAsState()
    val hapticFeedback = LocalHapticFeedback.current
    val animationData by viewModel.diffAnimationState.observeAsState()

    LaunchedEffect(key1 = action) {
        when (action) {
            is CalculatorAction.GoToSettings -> {
                needToNavigateToSettings.invoke()
            }

            is CalculatorAction.RecentDeleted -> {
                hapticFeedback.performClickVibration()
            }

            else -> {}
        }
    }
    CalculatorScreen(
        state = state,
        animationData = animationData,
        oneEvent = {
            viewModel.handleEvent(it)
        }
    )
}

@Composable
internal fun CalculatorScreen(
    state: CalculatorScreenState?,
    animationData: DiffAnimationState?,
    oneEvent: (CalculatorEvent) -> (Unit),
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("calculator:bad-billing")
    ) {
        when (state) {
            is CalculatorScreenState.Default -> {
                CalculatorMainPage(
                    budgetUiState = state.budgetUiState,
                    diffAnimationState = animationData,
                ) {
                    oneEvent.invoke(it)
                }
            }

            is CalculatorScreenState.AskedToUpdateDailyOrTodayBudget -> {
                CalculatorChooseIncreasePage(
                    budgetLeft = state.budgetLeft,
                    dailyFromTo = state.dailyFromTo,
                    todayFromTo = state.todayFromTo,
                    daysLeft = state.daysLeft,
                ) {
                    oneEvent.invoke(it)
                }
            }

            is CalculatorScreenState.BadBillingDate -> {
                CalculatorBadBillingDatePage(
                    remainingBudget = state.budgetLeft,
                ) {
                    oneEvent.invoke(it)
                }
            }

            else -> {}
        }
    }
}


