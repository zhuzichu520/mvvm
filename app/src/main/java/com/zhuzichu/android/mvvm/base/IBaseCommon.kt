package com.zhuzichu.android.mvvm.base

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.AnimBuilder
import androidx.navigation.NavDirections
import com.zhuzichu.android.mvvm.MvvmManager

interface IBaseCommon {

    fun back()

    fun showLoading()

    fun hideLoading()

    fun startActivity(
        clz: Class<*>,
        args: Bundle = bundleOf(),
        isPop: Boolean = false,
        options: Bundle = bundleOf(),
        requestCode: Int = 0
    )

    fun startFragment(
        actionId: Int,
        args: Bundle = bundleOf(),
        animBuilder: AnimBuilder.() -> Unit = MvvmManager.animBuilder
    )

    fun startFragment(
        navDirections: NavDirections,
        animBuilder: AnimBuilder.() -> Unit = MvvmManager.animBuilder
    )

}