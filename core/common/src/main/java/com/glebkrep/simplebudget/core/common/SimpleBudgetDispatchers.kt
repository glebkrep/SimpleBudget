package com.glebkrep.simplebudget.core.common

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val simpleBudgetDispatcher: SimpleBudgetDispatcher)

enum class SimpleBudgetDispatcher {
    Default,
    IO,
}
