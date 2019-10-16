package com.zhuzichu.android.mvvm.base

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.zhuzichu.android.mvvm.event.SingleLiveEvent

open class BaseViewModel : ViewModel(), IBaseViewModel {

    val uc by lazy { UIChangeLiveData() }

    lateinit var lifecycleOwner: LifecycleOwner

    fun injectLifecycleOwner(viewLifecycleOwner: LifecycleOwner) {
        this.lifecycleOwner = viewLifecycleOwner
    }

    fun startActivity(
        clz: Class<*>,
        argument: BaseArgument = DefaultArgument(),
        isPop: Boolean = false,
        options: Bundle = bundleOf(),
        requestCode: Int = 0
    ) {
        val playload = Payload.Activity(clz)
        playload.argument = argument
        playload.isPop = isPop
        playload.options = options
        playload.requestCode = requestCode
        uc.startActivityEvent.value = playload
    }

    fun startFragment(actionId: Int, argument: BaseArgument = DefaultArgument()) {
        val playload = Payload.Fragment(actionId)
        playload.argument = argument
        uc.startFragmentEvent.value = playload
    }

    fun back() {
        uc.onBackPressedEvent.call()
    }

    inner class UIChangeLiveData {
        internal val startActivityEvent: SingleLiveEvent<Payload.Activity> = SingleLiveEvent()
        internal val startFragmentEvent: SingleLiveEvent<Payload.Fragment> = SingleLiveEvent()
        internal val onBackPressedEvent: SingleLiveEvent<Any> = SingleLiveEvent()
    }
}