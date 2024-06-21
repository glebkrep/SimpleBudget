package com.glebkrep.simplebudget.feature.updatebilling.vm


sealed class BillingDateUpdateAction {
    data object None : BillingDateUpdateAction()
    data object GoBack : BillingDateUpdateAction()
    data class PostSnackBarAndGoBack(val message: String) : BillingDateUpdateAction()
}
