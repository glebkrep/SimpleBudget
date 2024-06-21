package com.glebkrep.simplebudget.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API

class SimpleBudgetIssueRegistry : IssueRegistry() {

    override val issues = listOf(
        DesignSystemDetector.ISSUE,
    )

    override val api: Int = CURRENT_API

    override val minApi: Int = 12

    override val vendor: Vendor = Vendor(
        vendorName = "Simple Budget",
        feedbackUrl = "",
        contact = "https://github.com/glebkrep",
    )
}
