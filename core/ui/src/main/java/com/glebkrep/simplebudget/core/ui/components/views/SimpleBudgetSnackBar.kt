package com.glebkrep.simplebudget.core.ui.components.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.glebkrep.simplebudget.core.ui.theme.DefaultPadding
import com.glebkrep.simplebudget.core.ui.theme.SimpleBudgetTheme

@Composable
fun SimpleBudgetSnackBar(snackbarData: SnackbarData) {
    Snackbar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = LocalContentColor.current,
        modifier = Modifier.padding(DefaultPadding.BIG)
    ) {
        Row(modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)) {
            SimpleBudgetViews.SimpleBudgetText(text = snackbarData.visuals.message)
        }
    }
}

@Composable
@Preview
fun SimpleBudgetSnackBarPreview() {
    SimpleBudgetTheme {
        SimpleBudgetSnackBar(
            snackbarData = object : SnackbarData {
                override val visuals: SnackbarVisuals
                    get() = object : SnackbarVisuals {
                        override val actionLabel: String?
                            get() = null
                        override val duration: SnackbarDuration
                            get() =
                                SnackbarDuration.Short
                        override val message: String
                            get() = "Test snackbar message"
                        override val withDismissAction: Boolean
                            get() = true

                    }

                override fun dismiss() = Unit
                override fun performAction() = Unit

            }
        )
    }
}
