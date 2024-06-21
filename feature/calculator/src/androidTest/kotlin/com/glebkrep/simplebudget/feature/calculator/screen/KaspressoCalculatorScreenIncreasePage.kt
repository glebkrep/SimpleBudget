package com.glebkrep.simplebudget.feature.calculator.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class KaspressoCalculatorScreenIncreasePage(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<KaspressoCalculatorScreenIncreasePage>(
        semanticsProvider = semanticsProvider,
        // Screen in Kakao Compose can be a Node too due to 'viewBuilderAction' param.
        // 'viewBuilderAction' param is nullable.
        viewBuilderAction = { hasTestTag("calculator:increase") }
    ) {

    val increaseBudgetText: KNode = child {
        hasTestTag("calculator:increase:total_left")
    }

    val increaseDaysLeftText: KNode = child {
        hasTestTag("calculator:increase:days_left")
    }
}