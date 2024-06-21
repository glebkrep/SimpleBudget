package com.glebkrep.simplebudget.feature.calculator

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.glebkrep.simplebudget.core.data.data.models.CalculatorScreenState
import com.glebkrep.simplebudget.core.ui.theme.SimpleBudgetTheme
import com.glebkrep.simplebudget.feature.calculator.screen.KaspressoCalculatorScreenBadBillingPage
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
import io.github.kakaocup.kakao.common.utilities.getResourceString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestCalculatorScreenBadBillingPage : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun badBillingDatePageSavedGood() = run {
        val budgetLeft = "123"
        composeTestRule.setContent {
            SimpleBudgetTheme {
                CalculatorScreen(
                    state = CalculatorScreenState.BadBillingDate(budgetLeft),
                    animationData = null,
                    oneEvent = {}
                )
            }
        }
        onComposeScreen<KaspressoCalculatorScreenBadBillingPage>(composeTestRule) {
            step("Open BadBillingDatePage") {
                this.assertIsDisplayed()
            }

            step("Check has text with passed value") {
                composeTestRule.onNodeWithText(budgetLeft).assertIsDisplayed()
            }

            step("Check has good message") {
                badBillingDatePageSubHeader.assertIsDisplayed()
                badBillingDatePageSubHeader.assertTextEquals(getResourceString(R.string.feature_calculator_saved_good))
            }
        }
    }

    @Test
    fun badBillingDatePageSavedNone() = run {
        val budgetLeft = "0"
        composeTestRule.setContent {
            SimpleBudgetTheme {
                CalculatorScreen(
                    state = CalculatorScreenState.BadBillingDate(budgetLeft),
                    animationData = null,
                    oneEvent = {}
                )
            }
        }
        onComposeScreen<KaspressoCalculatorScreenBadBillingPage>(composeTestRule) {
            step("Open BadBillingDatePage") {
                this.assertIsDisplayed()
            }

            step("Check has text with passed value") {
                composeTestRule.onNodeWithText(budgetLeft).assertIsDisplayed()
            }

            step("Check has bad message") {
                badBillingDatePageSubHeader.assertIsDisplayed()
                badBillingDatePageSubHeader.assertTextEquals(getResourceString(R.string.feature_calculator_saved_bad))
            }
        }
    }
}