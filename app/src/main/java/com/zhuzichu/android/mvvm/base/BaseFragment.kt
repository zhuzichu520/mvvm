package com.zhuzichu.android.mvvm.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.zhuzichu.android.mvvm.R
import dagger.android.support.DaggerFragment
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

abstract class BaseFragment<TArgument : BaseArgument, TBinding : ViewDataBinding, TViewModel : BaseViewModel> :
    DaggerFragment(), IBaseFragment {

    companion object {
        private const val KEY_ARGUMENT = "KEY_ARGUMENT"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var isInitData = false
    private var isInitLazy = false

    var binding: TBinding? = null
    lateinit var viewModel: TViewModel
    lateinit var argument: TArgument
    lateinit var activityCtx: Activity

    val navController by lazy { activityCtx.findNavController(R.id.delegate_container) }

    abstract fun setLayoutId(): Int
    abstract fun bindVariableId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            argument = it.getParcelable<BaseArgument>(KEY_ARGUMENT) as TArgument
        }
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
        if (!isInitData) {
            initData()
            isInitData = true
        }
    }

    private fun initViewDataBinding() {
        val type = this::class.java.genericSuperclass
        if (type is ParameterizedType) {
            viewModel = ViewModelProvider(this, viewModelFactory)
                .get(type.actualTypeArguments[2] as Class<TViewModel>)
        }
        binding?.setVariable(bindVariableId(), viewModel)
        lifecycle.addObserver(viewModel)
        viewModel.injectLifecycleOwner(viewLifecycleOwner)
    }

    private fun registUIChangeLiveDataCallback() {
        viewModel.uc.startActivityEvent.observe(this, Observer {
            val intent = Intent(activityCtx, it.clz)
            intent.putExtra(KEY_ARGUMENT, it.argument)
            startActivityForResult(intent, it.requestCode, it.options)
            if (it.isPop) {
                activityCtx.finish()
            }
        })

        viewModel.uc.startFragmentEvent.observe(this, Observer {
            navController.navigate(
                it.actionId,
                bundleOf(KEY_ARGUMENT to it.actionId),
                getDefaultNavOptions(it.actionId)
            )
        })

        viewModel.uc.onBackPressedEvent.observe(this, Observer {
            activityCtx.onBackPressed()
        })

    }

    private fun getDefaultNavOptions(actionId: Int): NavOptions? {
        val navOptions = navController.currentDestination?.getAction(actionId)?.navOptions
        var options: NavOptions? = null
        navOptions?.let {
            options = androidx.navigation.navOptions {
                anim {
                    enter = R.anim.slide_in_right
                    exit = R.anim.slide_out_left
                    popEnter = R.anim.slide_in_left
                    popExit = R.anim.slide_out_right
                }
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
        if (!isInitLazy) {
            initLazyData()
            isInitLazy = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.unbind()
        binding = null
    }
}