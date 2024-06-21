package com.glebkrep.simplebudget.core.common

import android.util.Log

object Logger {
    fun log(any: Any?) {
        Log.e("MY_LOG:", any.toString())
    }
}
