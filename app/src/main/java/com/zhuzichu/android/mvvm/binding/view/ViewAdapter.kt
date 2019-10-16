package com.zhuzichu.android.mvvm.binding.view

import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.jakewharton.rxbinding3.view.clicks
import com.uber.autodispose.android.autoDispose
import com.zhuzichu.android.libs.tool.showKeyboard
import com.zhuzichu.android.mvvm.binding.BindingCommand
import java.util.concurrent.TimeUnit

//防重复点击间隔(毫秒)
const val CLICK_INTERVAL = 500

@BindingAdapter(value = ["onClickCommand", "isThrottleFirst"], requireAll = false)
fun bindViewClick(view: View, clickCommand: BindingCommand<*>?, isThrottleFirst: Boolean = true) {
    view.post {
        if (isThrottleFirst) {
            view.clicks()
                .autoDispose(view)
                .subscribe {
                    clickCommand?.execute()
                }
        } else {
            view.clicks()
                .throttleFirst(CLICK_INTERVAL.toLong(), TimeUnit.MILLISECONDS)//1秒钟内只允许点击1次
                .autoDispose(view)
                .subscribe {
                    clickCommand?.execute()
                }
        }
    }
}

@BindingAdapter(value = ["showSoftInput"], requireAll = false)
fun bindViewSoftInput(editText: EditText, isShow: Boolean = false) {
    if (isShow) {
        editText.post {
            showKeyboard(editText.context, editText)
        }
    }
}