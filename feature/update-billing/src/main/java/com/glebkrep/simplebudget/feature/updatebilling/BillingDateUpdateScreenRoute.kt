package com.glebkrep.simplebudget.feature.updatebilling

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
import com.glebkrep.simplebudget.feature.updatebilling.view.BillingDateUpdatePage
import com.glebkrep.simplebudget.feature.updatebilling.vm.BillingDateUpdateAction
import com.glebkrep.simplebudget.feature.updatebilling.vm.BillingDateUpdateEvent
import com.glebkrep.simplebudget.feature.updatebilling.vm.BillingDateUpdateState
import com.glebkrep.simplebudget.feature.updatebilling.vm.BillingDateUpdateVM

@Composable
fun BillingDateUpdateScreen(
    goBack: () -> Unit,
    postSnackBar: (String) -> Unit,
    viewModel: BillingDateUpdateVM = hiltViewModel()
) {
    val state by viewModel.state.observeAsState()
    val action by viewModel.action.observeAsState()

    LaunchedEffect(key1 = action) {
        when (val currentAction = action) {
            is BillingDateUpdateAction.GoBack -> goBack()

            is BillingDateUpdateAction.PostSnackBarAndGoBack -> {
                postSnackBar(currentAction.message)
                goBack()
            }

            else -> Unit
        }
    }

    BillingDateUpdateScreen(
        state = state,
        onEvent = {
            viewModel.handleEvent(it)
        }
    )
}

@Composable
internal fun BillingDateUpdateScreen(
    state: BillingDateUpdateState?,
    onEvent: (BillingDateUpdateEvent) -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (state) {
            is BillingDateUpdateState.DatePicker -> {
                BillingDateUpdatePage(
                    state = state,
                    onEvent = { onEvent.invoke(it) })
            }

            null -> {

            }
        }
    }
}
