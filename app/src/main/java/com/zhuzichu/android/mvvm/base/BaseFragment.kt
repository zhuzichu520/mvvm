package com.zhuzichu.android.mvvm.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.AnimBuilder
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.zhuzichu.android.libs.tool.closeKeyboard
import com.zhuzichu.android.libs.tool.startActivity4Result
import com.zhuzichu.android.libs.tool.toCast
import com.zhuzichu.android.mvvm.R
import com.zhuzichu.android.widget.dialog.loading.LoadingMaker
import com.zhuzichu.android.widget.toast.toast
import dagger.android.support.DaggerFragment
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

abstract class BaseFragment<TBinding : ViewDataBinding, TViewModel : BaseViewModel> :
    DaggerFragment(), IBaseFragment, IBaseCommon {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var binding: TBinding? = null
    lateinit var viewModel: TViewModel
    lateinit var activityCtx: Activity

    val navController by lazy { activityCtx.findNavController(R.id.delegate_container) }

    abstract fun setLayoutId(): Int
    abstract fun bindVariableId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            setLayoutId(),
            container,
            false
        )
        binding?.lifecycleOwner = this
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewDataBinding()
        registUIChangeLiveDataCallback()
        initVariable()
        initView()
        initViewObservable()
        initData()
        if (!viewModel.isInitData) {
            initFirstData()
            viewModel.isInitData = true
        }
    }

    private fun initViewDataBinding() {
        val type = this::class.java.genericSuperclass
        val modelClass = if (type is ParameterizedType) {
            type.actualTypeArguments[1]
        } else {
            BaseViewModel::class.java
        }
        viewModel = ViewModelProvider(this, viewModelFactory).get(modelClass.toCast())
        binding?.setVariable(bindVariableId(), viewModel)
        lifecycle.addObserver(viewModel)
    }

    private fun registUIChangeLiveDataCallback() {
        viewModel.uc.startActivityEvent.observe(viewLifecycleOwner, Observer {
            startActivity4Result(activityCtx, it.clz, it.requestCode, it.args, it.options)
            if (it.isPop) {
                activityCtx.finish()
            }
        })

        viewModel.uc.startFragmentByResIdEvent.observe(viewLifecycleOwner, Observer {
            navController.navigate(
                it.resId,
                it.args,
                getDefaultNavOptions(it.resId, it.animBuilder)
            )
        })

        viewModel.uc.startFragmentByNavDirectionsEvent.observe(viewLifecycleOwner, Observer {
            navController.navigate(
                it.navDirections,
                getDefaultNavOptions(it.navDirections.actionId, it.animBuilder)
            )
        })

        viewModel.uc.onBackPressedEvent.observe(viewLifecycleOwner, Observer {
            activityCtx.onBackPressed()
        })

        viewModel.uc.showLoadingEvent.observe(viewLifecycleOwner, Observer {
            closeKeyboard(requireContext())
            view?.postDelayed({
                LoadingMaker.showLoadingDialog(requireContext())
            }, 75)
        })

        viewModel.uc.hideLoadingEvent.observe(viewLifecycleOwner, Observer {
            view?.postDelayed({
                LoadingMaker.dismissLodingDialog()
            }, 75)
        })

        viewModel.uc.toastStringResEvent.observe(viewLifecycleOwner, Observer {
            it.toast(context = requireContext())
        })

        viewModel.uc.toastStringEvent.observe(viewLifecycleOwner, Observer {
            it.toast(context = requireContext())
        })

    }

    private fun getDefaultNavOptions(
        actionId: Int,
        animBuilder: AnimBuilder.() -> Unit
    ): NavOptions? {
        val navOptions = navController.currentDestination?.getAction(actionId)?.navOptions
        var options: NavOptions? = null
        navOptions?.let {
            options = androidx.navigation.navOptions {
                anim(animBuilder)
                launchSingleTop = navOptions.shouldLaunchSingleTop()
                popUpTo(navOptions.popUpTo) {
                    this.inclusive = navOptions.isPopUpToInclusive
                }
            }
        }
        return options
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityCtx = requireActivity()
    }

    override fun onResume() {
        super.onResume()
        if (!viewModel.isInitLazy) {
            initLazyData()
            viewModel.isInitLazy = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.unbind()
        binding = null
    }

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
        isPop: Boolean,
        options: Bundle,
        requestCode: Int
    ) {
        viewModel.startActivity(clz, isPop, options, requestCode)
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