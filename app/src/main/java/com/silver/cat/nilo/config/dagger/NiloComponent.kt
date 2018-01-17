package com.silver.cat.nilo.config.dagger

import com.silver.cat.nilo.NiloApplication
import com.silver.cat.nilo.config.dagger.activity.ActivityModule
import com.silver.cat.nilo.config.dagger.activity.ActivitySubComponent
import com.silver.cat.nilo.service.NiloFirebaseInstanceIDService
import com.silver.cat.nilo.view.main.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by xiezhenyu on 2017/1/17.
 */

@Singleton
@Component(modules = arrayOf(NiloModule::class))
interface NiloComponent {

  fun inject(application: NiloApplication)
  fun newActivitySubComponent(activityModule: ActivityModule): ActivitySubComponent
  fun inject(application: NiloFirebaseInstanceIDService)
  fun inject(mainActivity: MainActivity)
}
