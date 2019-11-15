package com.zhuzichu.android.mvvm.base

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.AnimBuilder
import androidx.navigation.NavDirections
import com.zhuzichu.android.mvvm.MvvmManager
import com.zhuzichu.android.mvvm.R

internal sealed class Payload {

    internal data class PayloadActivity(
        var clz: Class<*>,
        var args: Bundle? = null,
        var isPop: Boolean = false,
        var options: Bundle = bundleOf(),
        var requestCode: Int = 0
    )

    internal data class PayloadFragmentId(
        var resId: Int,
        var args: Bundle? = null,
        var animBuilder: AnimBuilder.() -> Unit = MvvmManager.animBuilder
    )

    internal data class PlayloadFragmentDirections(
        var navDirections: NavDirections,
        var animBuilder: AnimBuilder.() -> Unit = MvvmManager.animBuilder
    )

}