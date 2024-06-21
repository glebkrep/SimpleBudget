package com.glebkrep.simplebudget.core.ui

interface EventHandler<T> {
    fun handleEvent(event: T)
}
