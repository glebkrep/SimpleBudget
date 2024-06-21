package com.glebkrep.simplebudget.feature.calculator.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class KaspressoCalculatorScreenBadBillingPage(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<KaspressoCalculatorScreenBadBillingPage>(
        semanticsProvider = semanticsProvider,
        // Screen in Kakao Compose can be a Node too due to 'viewBuilderAction' param.
        // 'viewBuilderAction' param is nullable.
        viewBuilderAction = { hasTestTag("calculator:bad-billing") }
    ) {

    val badBillingDatePageSubHeader: KNode = child {
        hasTestTag("calculator:bad-billing:sub-header")
    }
}