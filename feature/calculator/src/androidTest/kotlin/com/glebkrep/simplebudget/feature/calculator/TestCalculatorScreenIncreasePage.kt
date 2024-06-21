package com.glebkrep.simplebudget.feature.calculator

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.glebkrep.simplebudget.core.data.data.models.CalculatorScreenState
import com.glebkrep.simplebudget.core.ui.theme.SimpleBudgetTheme
import com.glebkrep.simplebudget.feature.calculator.screen.KaspressoCalculatorScreenBadBillingPage
import com.glebkrep.simplebudget.feature.calculator.screen.KaspressoCalculatorScreenIncreasePage
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.kakao.common.utilities.getResourceString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestCalculatorScreenIncreasePage : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun increaseVisibilityTest() = run {
        val budgetLeft = "123"
        val daysLeft = "10"
        composeTestRule.setContent {
            SimpleBudgetTheme {
                CalculatorScreen(
                    state = CalculatorScreenState.AskedToUpdateDailyOrTodayBudget(
                        dailyFromTo = Pair("12", "13"),
                        todayFromTo = Pair("12", "15"),
                        budgetLeft = budgetLeft,
                        daysLeft = daysLeft
                    ),
                    animationData = null,
                    oneEvent = {}
                )
            }
        }
        ComposeScreen.onComposeScreen<KaspressoCalculatorScreenIncreasePage>(composeTestRule) {
            step("Open Increase Page") {
                this.assertIsDisplayed()
            }

            step("Check budget value") {
                increaseBudgetText.assertIsDisplayed()
                increaseBudgetText.assertTextEquals(
                    "123 left"
                )
            }

            step("Check day left") {
                increaseDaysLeftText.assertIsDisplayed()
                increaseDaysLeftText.assertTextEquals(
//                    getResourceString(
//                        R.string.feature_calculator_days_left, daysLeft
//                    )
                    "for 10 days"
                )
            }
        }
    }

}