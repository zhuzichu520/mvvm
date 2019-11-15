package com.zhuzichu.android.mvvm

import androidx.navigation.AnimBuilder

object MvvmManager {

    var animBuilder: AnimBuilder.() -> Unit = {
        enter = R.anim.default_enter
        exit = R.anim.default_exit
        popEnter = R.anim.default_pop_enter
        popExit = R.anim.default_pop_exit
    }

}