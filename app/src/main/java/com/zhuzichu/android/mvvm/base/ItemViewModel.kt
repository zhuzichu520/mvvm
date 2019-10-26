package com.zhuzichu.android.mvvm.base

import android.os.Bundle
import androidx.lifecycle.ViewModel


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
        argument: BaseArgument,
        isPop: Boolean,
        options: Bundle,
        requestCode: Int
    ) {
        viewModel.startActivity(clz, argument, isPop, options, requestCode)
    }

    override fun startFragment(actionId: Int, argument: BaseArgument) {
        viewModel.startFragment(actionId, argument)
    }

}