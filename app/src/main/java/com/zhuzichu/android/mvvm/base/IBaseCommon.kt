package com.zhuzichu.android.mvvm.base

import androidx.annotation.StringRes

interface IBaseCommon {

    fun back()

    fun showLoading()

    fun hideLoading()

    fun toast(text: String?)

    fun toast(@StringRes id: Int)

}