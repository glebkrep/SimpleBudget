package com.glebkrep.simplebudget.feature.preferences.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.glebkrep.simplebudget.core.ui.components.clickableWithVibrationAndSound
import com.glebkrep.simplebudget.core.ui.components.views.SimpleBudgetViews
import com.glebkrep.simplebudget.core.ui.theme.DefaultPadding
import com.glebkrep.simplebudget.core.ui.theme.SimpleBudgetTheme
import com.glebkrep.simplebudget.feature.preferences.R
import com.glebkrep.simplebudget.feature.preferences.vm.PreferencesEvent
import com.glebkrep.simplebudget.feature.preferences.vm.PreferencesState

@Composable
fun PreferencesMainPage(
    state: PreferencesState.Display,
    onNewEvent: (PreferencesEvent) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        SimpleBudgetViews.SimpleBudgetHeaderWithBackArrow(
            headerText = stringResource(id = R.string.feature_preferences_header),
            modifier = Modifier.fillMaxWidth(),
            onBackClick = { onNewEvent.invoke(PreferencesEvent.Back) })
        BoxItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = DefaultPadding.BIG_PADDING,
                    vertical = DefaultPadding.BIGGER_PADDING
                ),
            bigText = state.currentBillingDatePretty,
            smallText = stringResource(R.string.feature_preferences_btn_change_billing),
            onClick = {
                onNewEvent(PreferencesEvent.ToBillingDatePicker)
            })

        BoxItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = DefaultPadding.BIG_PADDING,
                    vertical = DefaultPadding.BIGGER_PADDING
                ),
            bigText = state.currentBudgetPretty,
            smallText = stringResource(R.string.feature_preferences_btn_change_budget),
            onClick = {
                onNewEvent(PreferencesEvent.ToBudgetPicker)
            })
        SimpleBudgetViews.SimpleBudgetSwitch(
            text = stringResource(R.string.feature_preferences_enable_comments),
            isChecked = state.isCommentsEnabled,
            textColor = MaterialTheme.colorScheme.onBackground,
            textStyle = MaterialTheme.typography.titleLarge,
            onCheckedChange = { onNewEvent(PreferencesEvent.EnableCommentsTweak(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(DefaultPadding.BIG_PADDING)
        )

    }
}

@Composable
private fun BoxItem(modifier: Modifier, bigText: String, smallText: String, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .clickableWithVibrationAndSound {
                onClick.invoke()
            }
            .padding(DefaultPadding.BIG_PADDING)) {
            SimpleBudgetViews.SimpleBudgetText(
                text = bigText,
                textStyle = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground,
                disablePadding = true
            )
            SimpleBudgetViews.SimpleBudgetText(
                text = smallText,
                disablePadding = true,
                textStyle = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Preview
@Composable
fun PreferencesMainPagePreview() {
    SimpleBudgetTheme {
        Column(
            Modifier
                .fillMaxSize()
        ) {
            PreferencesMainPage(
                state = PreferencesState.Display(
                    currentBudgetPretty = "5000",
                    currentBillingDatePretty = "12.04.1999",
                    isCommentsEnabled = true,
                )
            ) {
            }
        }
    }
}
