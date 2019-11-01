package com.zhuzichu.android.mvvm.extension

import androidx.core.os.bundleOf
import com.zhuzichu.android.mvvm.base.BaseArgument
import com.zhuzichu.android.mvvm.base.BaseFragment
import com.zhuzichu.android.mvvm.base.BaseFragment.Companion.KEY_ARGUMENT


fun BaseFragment<*, *, *>.putArgument(argument: BaseArgument): BaseFragment<*, *, *> {
    var bundle = arguments
    if (bundle != null) {
        bundle.putParcelable(KEY_ARGUMENT, argument)
    } else {
        bundle = bundleOf(KEY_ARGUMENT to argument)
    }
    arguments = bundle
    return this
}