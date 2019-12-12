package com.zhuzichu.android.mvvm.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.navigation.AnimBuilder
import androidx.navigation.NavDirections


open class ItemViewModel(
    private val viewModel: BaseViewModel
) : ViewModel(), IBaseCommon {

    override fun back() {
        viewModel.back()
    }

    override fun showLoading() {
        viewModel.showLoading()
    }

    override fun hideLoading() {
        viewModel.hideLoading()
    }

    override fun toast(text: String?) {
        viewModel.toast(text)
    }

    override fun toast(id: Int) {
        viewModel.toast(id)
    }

    override fun startActivity(
        clz: Class<*>,
        args: Bundle,
        isPop: Boolean,
        options: Bundle,
        requestCode: Int
    ) {
        viewModel.startActivity(clz, args, isPop, options, requestCode)
    }

    override fun startFragment(
        actionId: Int,
        args: Bundle?,
        animBuilder: AnimBuilder.() -> Unit
    ) {
        viewModel.startFragment(actionId, args, animBuilder)
    }

    override fun startFragment(
        navDirections: NavDirections,
        animBuilder: AnimBuilder.() -> Unit
    ) {
        viewModel.startFragment(navDirections, animBuilder)
    }
}