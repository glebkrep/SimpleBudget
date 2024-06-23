package com.glebkrep.simplebudget.feature.updatebudget

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
import com.glebkrep.simplebudget.feature.updatebudget.view.BudgetUpdatePage
import com.glebkrep.simplebudget.feature.updatebudget.vm.BudgetUpdateAction
import com.glebkrep.simplebudget.feature.updatebudget.vm.BudgetUpdateEvent
import com.glebkrep.simplebudget.feature.updatebudget.vm.BudgetUpdateState
import com.glebkrep.simplebudget.feature.updatebudget.vm.BudgetUpdateVM

@Composable
fun BudgetUpdateScreenRoute(
    goBack: () -> Unit,
    postSnackBar: (String) -> Unit,
    viewModel: BudgetUpdateVM = hiltViewModel()
) {
    val state by viewModel.state.observeAsState()
    val action by viewModel.action.observeAsState()

    LaunchedEffect(key1 = action) {
        val currentAction = action ?: return@LaunchedEffect
        when (currentAction) {
            is BudgetUpdateAction.GoBack -> goBack()
            is BudgetUpdateAction.PostSnackBarAndGoBack -> {
                postSnackBar(currentAction.message)
                goBack()
            }

            else -> Unit
        }
    }

    BudgetUpdateScreen(
        state = state,
        onEvent = {
            viewModel.handleEvent(it)
        }
    )
}

@Composable
internal fun BudgetUpdateScreen(
    state: BudgetUpdateState?,
    onEvent: (BudgetUpdateEvent) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (state) {
            is BudgetUpdateState.BudgetInput -> {
                BudgetUpdatePage(state) {
                    onEvent.invoke(it)
                }
            }

            null -> {

            }
        }
    }
}
