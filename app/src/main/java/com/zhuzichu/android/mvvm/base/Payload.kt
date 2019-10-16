package com.zhuzichu.android.mvvm.base

import android.os.Bundle
import androidx.core.os.bundleOf

internal sealed class Payload {

    data class Activity(
        var clz: Class<*>,
        var argument: BaseArgument = DefaultArgument(),
        var isPop: Boolean = false,
        var options: Bundle = bundleOf(),
        var requestCode: Int = 0
    )

    data class Fragment(
        var actionId: Int,
        var argument: BaseArgument = DefaultArgument()
    )

}