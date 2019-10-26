package com.zhuzichu.android.mvvm.base

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.core.os.bundleOf

interface IBaseCommon {

    fun back()

    fun showLoading()

    fun hideLoading()

    fun toast(text: String?)

    fun toast(@StringRes id: Int)

    fun startActivity(
        clz: Class<*>,
        argument: BaseArgument = ArgumentDefault(),
        isPop: Boolean = false,
        options: Bundle = bundleOf(),
        requestCode: Int = 0
    )

    fun startFragment(
        actionId: Int,
        argument: BaseArgument = ArgumentDefault()
    )


}