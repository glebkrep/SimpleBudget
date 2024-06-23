package com.glebkrep.simplebudget.core.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class AbstractScreenVM<EVENT, STATE, ACTION>(private val defAction: ACTION) : ViewModel(),
    EventHandler<EVENT> {
    private val _state: MutableLiveData<STATE> = MutableLiveData()
    val state: LiveData<STATE> = _state

    private val _action: MutableLiveData<ACTION> = MutableLiveData()
    val action: LiveData<ACTION> = _action

    protected fun postAction(action: ACTION) {
        this@AbstractScreenVM._action.postValue(action)
        viewModelScope.launch {
            delay(POST_ACTION_DELAY)
            this@AbstractScreenVM._action.postValue(defAction)
        }
    }

    protected fun getCurrentState(): STATE? {
        return this@AbstractScreenVM._state.value
    }

    protected fun getCurrentStateNotNull(): STATE {
        return this@AbstractScreenVM._state.value
            ?: error("Current state is not expected to be null")
    }

    protected fun postState(state: STATE) {
        this@AbstractScreenVM._state.postValue(state)
    }

    internal companion object {
        private const val POST_ACTION_DELAY = 100L
    }
}
