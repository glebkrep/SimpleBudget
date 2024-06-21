package com.glebkrep.simplebudget.lint

import com.android.tools.lint.checks.infrastructure.TestFile
import com.android.tools.lint.checks.infrastructure.TestFiles
import com.android.tools.lint.checks.infrastructure.TestLintTask
import com.glebkrep.simplebudget.lint.DesignSystemDetector.Companion.ISSUE
import com.glebkrep.simplebudget.lint.DesignSystemDetector.Companion.METHOD_NAMES
import kotlin.test.Test

class DesignSystemDetectorTest {

    @Test
    fun `detect replacements of Composable`() {
        TestLintTask.lint()
            .issues(ISSUE)
            .allowMissingSdk()
            .files(
                COMPOSABLE_STUB,
                STUBS,
                @Suppress("LintImplTrimIndent")
                TestFiles.kotlin(
                    """
                    |import androidx.compose.runtime.Composable
                    |
                    |@Composable
                    |fun App() {
                    ${METHOD_NAMES.keys.joinToString("\n") { "|    $it()" }}
                    |}
                    """.trimMargin(),
                ).indented(),
            )
            .run()
            .expect(
                """
                src/test2.kt:5: Error: Using Text instead of SimpleBudgetText [DesignSystem]
                    Text()
                    ~~~~~~
                src/test2.kt:6: Error: Using Switch instead of SimpleBudgetSwitch [DesignSystem]
                    Switch()
                    ~~~~~~~~
                src/test2.kt:7: Error: Using Checkbox instead of SimpleBudgetCheckbox [DesignSystem]
                    Checkbox()
                    ~~~~~~~~~~
                src/test2.kt:8: Error: Using Button instead of SimpleBudgetIconButton [DesignSystem]
                    Button()
                    ~~~~~~~~
                4 errors, 0 warnings
                """.trimIndent(),
            )
    }

    private companion object {

        private val COMPOSABLE_STUB: TestFile = TestFiles.kotlin(
            """
            package androidx.compose.runtime
            annotation class Composable
            """.trimIndent(),
        ).indented()

        private val STUBS: TestFile = TestFiles.kotlin(
            """
            |import androidx.compose.runtime.Composable
            |
            ${METHOD_NAMES.keys.joinToString("\n") { "|@Composable fun $it() = {}" }}
            |
            """.trimMargin(),
        ).indented()
    }
}
