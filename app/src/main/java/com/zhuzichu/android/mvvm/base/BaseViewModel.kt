package com.zhuzichu.android.mvvm.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.navigation.AnimBuilder
import androidx.navigation.NavDirections
import com.zhuzichu.android.mvvm.event.SingleLiveEvent

abstract class BaseViewModel : ViewModel(), LifecycleViewModel, IBaseCommon {

    val uc by lazy { UIChangeLiveData() }

    var isInitData = false
    var isInitLazy = false

    override fun startActivity(
        clz: Class<*>,
        args: Bundle,
        isPop: Boolean,
        options: Bundle,
        requestCode: Int
    ) {
        val playload = Payload.PayloadActivity(clz)
        playload.args=args
        playload.isPop = isPop
        playload.options = options
        playload.requestCode = requestCode
        uc.startActivityEvent.value = playload
    }

    override fun startFragment(
        actionId: Int,
        args: Bundle,
        animBuilder: AnimBuilder.() -> Unit
    ) {
        val playload = Payload.PayloadFragmentId(actionId)
        playload.args = args
        playload.animBuilder = animBuilder
        uc.startFragmentByResIdEvent.value = playload
    }

    override fun startFragment(
        navDirections: NavDirections,
        animBuilder: AnimBuilder.() -> Unit
    ) {
        val playload = Payload.PlayloadFragmentDirections(navDirections)
        playload.animBuilder = animBuilder
        uc.startFragmentByNavDirectionsEvent.value = playload
    }

    override fun back() {
        uc.onBackPressedEvent.postCall()
    }

    override fun showLoading() {
        uc.showLoadingEvent.postCall()
    }

    override fun hideLoading() {
        uc.hideLoadingEvent.postCall()
    }


    inner class UIChangeLiveData {
        internal val startActivityEvent: SingleLiveEvent<Payload.PayloadActivity> =
            SingleLiveEvent()
        internal val startFragmentByResIdEvent: SingleLiveEvent<Payload.PayloadFragmentId> =
            SingleLiveEvent()
        internal val startFragmentByNavDirectionsEvent: SingleLiveEvent<Payload.PlayloadFragmentDirections> =
            SingleLiveEvent()
        internal val onBackPressedEvent: SingleLiveEvent<Any> = SingleLiveEvent()
        internal val showLoadingEvent: SingleLiveEvent<Any> = SingleLiveEvent()
        internal val hideLoadingEvent: SingleLiveEvent<Any> = SingleLiveEvent()
    }
}