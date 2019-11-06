package com.zhuzichu.android.mvvm.base

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.navigation.AnimBuilder
import androidx.navigation.NavDirections
import com.zhuzichu.android.mvvm.R

interface IBaseCommon {

    fun back()

    fun showLoading()

    fun hideLoading()

    fun toast(text: String?)

    fun toast(@StringRes id: Int)

    fun startActivity(
        clz: Class<*>,
        isPop: Boolean = false,
        options: Bundle = bundleOf(),
        requestCode: Int = 0
    )

    fun startFragment(
        actionId: Int,
        args: Bundle? = null,
        animBuilder: AnimBuilder.() -> Unit = {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    )

    fun startFragment(
        navDirections: NavDirections,
        animBuilder: AnimBuilder.() -> Unit = {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    )

}