package com.zhuzichu.android.mvvm.base

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.zhuzichu.android.mvvm.R
import com.zhuzichu.android.mvvm.base.BaseFragment.Companion.KEY_ARGUMENT
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : DaggerAppCompatActivity() {

    abstract fun setNavGraph(): Int

    val navController by lazy { findNavController(R.id.delegate_container) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initContainer(savedInstanceState)
    }

    private fun initContainer(savedInstanceState: Bundle?) {
        val container = FrameLayout(this)
        container.id = R.id.delegate_container
        setContentView(container)
        if (savedInstanceState == null) {
            val fragment = createHostFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.delegate_container, fragment)
                .setPrimaryNavigationFragment(fragment)
                .commit()
        }
    }

    private fun createHostFragment(): Fragment {
        val argument = intent.getParcelableExtra<BaseArgument>(KEY_ARGUMENT)
            ?: ArgumentDefault()
        return NavHostFragment.create(setNavGraph(), bundleOf(KEY_ARGUMENT to argument))
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun startActivity(
        clz: Class<*>,
        argument: BaseArgument = ArgumentDefault(),
        isPop: Boolean = false,
        options: Bundle = bundleOf(),
        requestCode: Int = 0
    ) {
        val intent = Intent(baseContext, clz)
        intent.putExtra(KEY_ARGUMENT, argument)
        startActivityForResult(intent, requestCode, options)
        if (isPop) {
            finish()
        }
    }

}