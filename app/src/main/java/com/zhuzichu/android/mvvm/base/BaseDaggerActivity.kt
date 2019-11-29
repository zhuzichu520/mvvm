package com.zhuzichu.android.mvvm.base

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.internal.Beta
import javax.inject.Inject

@Beta
abstract class BaseDaggerActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun attachBaseContext(newBase: Context?) {
        AndroidInjection.inject(this)
        super.attachBaseContext(newBase)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

}