package com.silver.cat.nilo

import android.app.Activity
import android.app.Application
import android.app.Service
import com.silver.cat.nilo.config.dagger.AppInjector
import com.silver.cat.nilo.config.dagger.NiloComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import javax.inject.Inject

/**
 * Created by xiezhenyu on 2017/1/17.
 */

class NiloApplication : Application(), HasActivityInjector, HasServiceInjector {


  @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

  @Inject lateinit var dispatchingServiceInjector:DispatchingAndroidInjector<Service>

  override fun onCreate() {
    super.onCreate()
    AppInjector.init(this)
  }

  override fun activityInjector(): AndroidInjector<Activity> {
    return dispatchingAndroidInjector
  }

  override fun serviceInjector(): AndroidInjector<Service> {
    return dispatchingServiceInjector
  }
}
