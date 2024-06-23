package com.glebkrep.simplebudget.feature.preferences

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.glebkrep.simplebudget.feature.preferences.view.PreferencesMainPage
import com.glebkrep.simplebudget.feature.preferences.logic.PreferencesAction
import com.glebkrep.simplebudget.feature.preferences.logic.PreferencesEvent
import com.glebkrep.simplebudget.feature.preferences.logic.PreferencesState
import com.glebkrep.simplebudget.feature.preferences.logic.PreferencesVM

@Composable
fun PreferencesScreenRoute(
    goBack: () -> Unit,
    goToBillingUpdate: () -> Unit,
    goToBudgetUpdate: () -> Unit,
    viewModel: PreferencesVM = hiltViewModel()
) {
    val state by viewModel.state.observeAsState()
    val action by viewModel.action.observeAsState()

    LaunchedEffect(key1 = action) {
        when (action) {
            PreferencesAction.GoBack -> goBack()
            PreferencesAction.GoToBillingUpdate -> goToBillingUpdate()
            PreferencesAction.GoToBudgetUpdate -> goToBudgetUpdate()
            else -> {}
        }
    }

    PreferencesScreen(
        state = state,
        onEvent = { viewModel.handleEvent(it) }
    )
}

@Composable
internal fun PreferencesScreen(
    state: PreferencesState?,
    onEvent: (PreferencesEvent) -> (Unit),
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (state) {
            is PreferencesState.Display -> {
                PreferencesMainPage(
                    state = state,
                    onNewEvent = { onEvent.invoke(it) }
                )
            }

            else -> {}
        }
    }
}
