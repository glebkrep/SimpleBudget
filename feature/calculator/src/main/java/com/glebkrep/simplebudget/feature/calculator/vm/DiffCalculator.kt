package com.glebkrep.simplebudget.feature.calculator.vm

import com.glebkrep.simplebudget.core.domain.converters.ConvertStringToPrettyStringUseCase

internal class DiffCalculator(prettyStringUseCase: ConvertStringToPrettyStringUseCase) {

    private val totalDiffCalculator = OneDiffCalculator(prettyStringUseCase)
    private val todayDiffCalculator = OneDiffCalculator(prettyStringUseCase)
    private val dailyDiffCalculator = OneDiffCalculator(prettyStringUseCase)

    fun getDiff(
        totalBudget: String,
        dailyBudget: String,
        todayBudget: String,
    ): DiffAnimationState {
        val totalDiff = totalDiffCalculator.getDiff(totalBudget)
        val todayDiff = todayDiffCalculator.getDiff(todayBudget)
        val dailyDiff = dailyDiffCalculator.getDiff(dailyBudget)
        return DiffAnimationState(
            totalDiff = totalDiff,
            todayDiff = todayDiff,
            dailyDiff = dailyDiff
        )
    }

    private inner class OneDiffCalculator(private val prettyStringUseCase: ConvertStringToPrettyStringUseCase) {
        private var cachedPreviousValue: String? = null
        private var cachedPreviousDiff: String? = null
        private var cachedPreviousDiffExpiryTime: Long = 0
        fun getDiff(newValue: String): String? {
            val previousValue = cachedPreviousValue
            val previousDiff = cachedPreviousDiff
            val previousDiffExpiryTime = cachedPreviousDiffExpiryTime
            return when {
                previousValue == null -> {
                    cachedPreviousValue = newValue
                    null
                }

                previousValue != newValue -> {
                    val diffString = newValue.toDouble() - previousValue.toDouble()
                    val diffPretty =
                        "${if (diffString > 0) "+" else ""}${prettyStringUseCase.invoke(diffString.toString())}"
                    cachedPreviousValue = newValue
                    cachedPreviousDiff = diffPretty
                    cachedPreviousDiffExpiryTime = System.currentTimeMillis() + DIFF_EXPIRY_TIME
                    diffPretty
                }

                previousValue == newValue && System.currentTimeMillis() >= previousDiffExpiryTime -> {
                    null
                }

                previousValue == newValue && System.currentTimeMillis() < previousDiffExpiryTime -> {
                    previousDiff
                }

                else -> {
                    error("unexpected state")
                }
            }
        }
    }

    internal companion object {
        internal const val DIFF_EXPIRY_TIME = 1500L
    }
}
